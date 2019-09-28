package izumi.sbtgen

import izumi.sbtgen.PreparedPlatform._
import izumi.sbtgen.impl.{WithArtifactExt, WithBasicRenderers, WithProjectIndex}
import izumi.sbtgen.model.Platform.BasePlatform
import izumi.sbtgen.model._
import izumi.sbtgen.sbtmeta.SbtgenMeta
import izumi.sbtgen.tools.IzString._

import scala.collection.mutable

sealed trait PreparedPlatform {
  final def fold[A](jvmOnly: Platform.Jvm.type => A)(cross: BasePlatform => A): Seq[A] = {
    this match {
      case JvmOnly => Seq(jvmOnly(Platform.Jvm))
      case Cross(platforms) => platforms.map(cross)
    }
  }
  final def jvmOnly: Boolean = this == JvmOnly
}
object PreparedPlatform {
  case object JvmOnly extends PreparedPlatform
  case class Cross(platforms: Seq[BasePlatform]) extends PreparedPlatform
}

case class PreparedArtifactHeader(
                                   name: ArtifactId,
                                   path: String,
                                   platform: PreparedPlatform,
                                 )
object PreparedArtifactHeader

case class PreparedCrossArtifact(
                                  header: PreparedArtifactHeader,
                                  settings: Seq[SettingDef],
                                  deps: Seq[ScopedDependency],
                                  libs: Seq[ScopedLibrary],
                                  sbtPlugins: PreparedPlugins,
                                  platformArtifacts: Seq[PreparedArtifact],
                                ) {
  def platform: PreparedPlatform = header.platform
  def jvmOnly: Boolean = platform.jvmOnly
  //
  //  trait Render[+A] {
  //    def apply(artifact: PreparedArtifact)(
  //      implicit
  //      preparedArtifactHeader: PreparedArtifactHeader.Render[A],
  //      settingDef: SettingDef.Render[A],
  //      scopedDependency: ScopedDependency.Render[A],
  //      scopedLibrary: ScopedLibrary.Render[A],
  //      preparedPlugins: PreparedPlugins.Render[A],
  //      platformArtifacts: PreparedArtifact.Render[A]
  //    ): A = {
  //
  //    }
  //  }
  //
  //  implicit val renderString: Render[String] = new Render[String] {
  //
  //  }
}

case class PreparedArtifact(
                             name: ArtifactId,
                             platform: BasePlatform,
                             parentName: ArtifactId,
                             settings: Seq[SettingDef],
                             deps: Seq[ScopedDependency],
                             libs: Seq[ScopedLibrary],
                             sbtPlugins: PreparedPlugins,
                           )

case class PreparedPlugins(
                            enabled: Seq[Plugin],
                            disabled: Seq[Plugin],
                          )

class Renderer(
                protected val config: GenConfig,
                project: Project,
              )
  extends WithProjectIndex
    with WithBasicRenderers
    with WithArtifactExt {

  private val aggregates: Seq[Aggregate] = project.aggregates.map(_.merge)
  protected val index: Map[ArtifactId, Artifact] = makeIndex(aggregates)
  protected val settingsCache = new mutable.HashMap[String, Int]()

  case class PreparedAggregate(
                                id: ArtifactId,
                                pathPrefix: Seq[String],
                                aggregatedNames: Seq[String],
                                target: Platform,
                                plugins: Plugins,
                                isRoot: Boolean = false,
                                enableSharedSettings: Boolean = true,
                                dontIncludeInSuperAgg: Boolean = false,
                                settings: Seq[SettingDef] = Seq.empty,
                              )

  def render(): Seq[String] = {
    val artifacts = aggregates.flatMap(_.filteredArtifacts).map(renderArtifact)

    val filteredAggs = aggregates.flatMap(renderAgg).filterNot(_.aggregatedNames.isEmpty)

    val superAgg = filteredAggs
      .groupBy(_.target)
      .toSeq
      .map {
        case (p, group) =>
          val id = p match {
            case platform: Platform.BasePlatform =>
              Some(platformName(platform))
            case Platform.All =>
              None
          }
          val name = (project.name.value +: id.toSeq).mkString("-")
          val path = id match {
            case Some(x) =>
              Seq(".agg", ".agg-" + x.toLowerCase)
            case None =>
              Seq(".")
          }

          val aggregatedIds = group.filterNot(_.dontIncludeInSuperAgg).map(a => renderName(a.id))
          PreparedAggregate(ArtifactId(name), path, aggregatedIds, p, project.globalPlugins, isRoot = p == Platform.All)
      }

    val allAggregates = (filteredAggs ++ superAgg).filterNot(_.aggregatedNames.isEmpty)
    assert(allAggregates.nonEmpty, "All aggregates were filtered out")
    val aggDefs = renderAggregateProjects(allAggregates)

    val unexpected = project.settings.filterNot(_.scope.platform == Platform.All)
    assert(unexpected.isEmpty, "Global settings cannot be scoped to a platform")

    val settings = project.settings.filter(_.scope.platform == Platform.All).map(renderSetting)

    val imports = Seq(project.imports.map(i => s"import ${i.value}").mkString("\n"))
    val plugins = formatPlugins(project.rootPlugins, Platform.All, dot = false, inclusive = true)

    val sc = settingsCache.toSeq.sortBy(_._2).map {
      case (s, idx) =>
        s"lazy val ${cacheIdxName(idx)} = Def.setting { $s }"
    }

    Seq(
      imports,
      plugins,
      settings,
      artifacts,
      aggDefs,
      sc,
    ).flatten
  }

  protected def renderAgg(aggregate: Aggregate): Seq[PreparedAggregate] = {
    val es = aggregate.enableSharedSettings
    val di = aggregate.dontIncludeInSuperAgg
    val fullAgg = aggregate.filteredArtifacts.flatMap {
      a =>
        if (a.isJvmOnly) {
          Seq(renderName(a.name))
        } else {
          a.platforms.filter(p => platformEnabled(p.platform)).map(p => a.nameOn(p.platform))
        }
    }

    val jvmOnly = mkAgg(aggregate, Platform.Jvm, es, di)
    val jsOnly = mkAgg(aggregate, Platform.Js, es, di)
    val nativeOnly = mkAgg(aggregate, Platform.Native, es, di)

    PreparedAggregate(
      aggregate.name,
      Seq(".agg", (aggregate.pathPrefix :+ aggregate.name.value).mkString("-")),
      fullAgg,
      Platform.All,
      project.globalPlugins,
      enableSharedSettings = es,
      dontIncludeInSuperAgg = di,
      settings = aggregate.settings,
    ) +: jvmOnly ++: jsOnly ++: nativeOnly
  }

  private def mkAgg(agg: Aggregate, platform: BasePlatform, sharedSettings: Boolean, disableSuperAgg: Boolean): Seq[PreparedAggregate] = {
    if (!isPlatformEnabled(platform)) {
      Seq.empty
    } else {
      val jvmAgg = agg.filteredArtifacts.flatMap {
        a =>
          if (a.isJvmOnly) {
            Seq(renderName(a.name))
          } else {
            a.platforms.filter(_.platform == platform).map(p => a.nameOn(p.platform))
          }
      }
      if (jvmAgg.nonEmpty) {
        val artname = Seq(agg.name.value, platformName(platform))
        val id = ArtifactId(artname.mkString("-"))
        Seq(PreparedAggregate(
          id,
          Seq(".agg", (agg.pathPrefix ++ artname).mkString("-")),
          jvmAgg,
          platform,
          project.globalPlugins,
          enableSharedSettings = sharedSettings,
          dontIncludeInSuperAgg = disableSuperAgg,
          settings = agg.settings,
        ))
      } else {
        Seq.empty
      }
    }
  }

  protected def renderAggregateProjects(aggregates: Seq[PreparedAggregate]): Seq[String] = {
    val settings = Seq(
      "skip" in SettingScope.Raw("publish") := true,
    )

    val aggDefs = aggregates.map {
      case PreparedAggregate(name, path, agg, platform, plugins, isRoot, enableSharedSettings, _, localSettings) =>
        val hack = if (isRoot) {
          project.sharedRootSettings
        } else if (enableSharedSettings) {
          project.sharedAggSettings
        } else {
          Seq.empty
        }

        val s = formatSettings(settings ++ localSettings ++ hack, Platform.All, false)

        val header = s"""lazy val ${renderName(name.value)} = (project in file(${stringLit(path.mkString("/"))}))"""
        val names = agg.map(_.shift(2)).mkString(".aggregate(\n", ",\n", "\n)").shift(2)
        val p = formatPlugins(plugins, platform, dot = true, inclusive = true)
        (Seq(header, s) ++ p ++ Seq(names)).mkString("\n")
    }
    aggDefs
  }

  //
  // NEW
  //

  protected def renderArtifact(a: Artifact): String = {
    renderArtifact1(prepareArtifact(project, a))
    //    renderArtifact0(a)
  }

  def prepareArtifact(project: Project, artifact: Artifact): PreparedCrossArtifact = {
    val enabledPlatforms = artifact.platforms.filter(platformEnabled)
    val platform = if (artifact.isJvmOnly) JvmOnly else Cross(enabledPlatforms.map(_.platform))
    val jvmOnly = platform.jvmOnly

    val header = {
      val path = (artifact.pathPrefix :+ artifact.name.value).mkString("/")
      PreparedArtifactHeader(artifact.name, path, platform)
    }
    val settings = prepareCrossArtifactSettings(project, artifact.subGroupId, artifact.settings, enabledPlatforms, jvmOnly)
    val deps = filterDeps(artifact.depends, jvmOnly, Platform.All)
    val libs = filterLibDeps(project, artifact.libs, jvmOnly, Platform.All)
    val plugins = preparePlugins(project, artifact.plugins ++ project.globalPlugins, Platform.All, inclusive = jvmOnly)
    val platformArtifacts = preparePlatformArtifacts(project, artifact, jvmOnly)(enabledPlatforms)

    PreparedCrossArtifact(
      header = header,
      settings = settings,
      deps = deps,
      libs = libs,
      sbtPlugins = plugins,
      platformArtifacts = platformArtifacts
    )
  }

  private def prepareCrossArtifactSettings(project: Project, subGroupId: Option[String], artifactSettings: Seq[SettingDef], enabledPlatforms: Seq[PlatformEnv], jvmOnly: Boolean): Seq[SettingDef] = {
    val groupId = (config.settings.groupId :: subGroupId.toList).mkString(".")

    val jvmOnlyFix = if (config.jvmOnly) {
      Seq(
        "unmanagedSourceDirectories" in SettingScope.Compile += """baseDirectory.value / ".jvm/src/main/scala" """.raw,
        "unmanagedResourceDirectories" in SettingScope.Compile += """baseDirectory.value / ".jvm/src/main/resources" """.raw,
        "unmanagedSourceDirectories" in SettingScope.Test += """baseDirectory.value / ".jvm/src/test/scala" """.raw,
        "unmanagedResourceDirectories" in SettingScope.Test += """baseDirectory.value / ".jvm/src/test/resources" """.raw,
      )
    } else Seq.empty

    val platformSettings = {
      enabledPlatforms.flatMap {
        penv =>
          val psettings = Seq(
            "scalaVersion" := "crossScalaVersions.value.head".raw,
            "crossScalaVersions" := penv.language.map(_.value),
          ) ++ penv.settings ++ {
            // FIXME:
            if (penv.platform == Platform.Jvm && jvmOnly) Seq(
              "publishArtifact" in SettingScope.Raw("(Test, packageBin)") := true,
              "publishArtifact" in SettingScope.Raw("(Test, packageDoc)") := false, // FIXME: packageDoc one day caused spurious failures, remove later???
              "publishArtifact" in SettingScope.Raw("(Test, packageSrc)") := true,
            ) else Seq.empty
          } // ++ artifact.settings

          filterSettings(psettings, penv.platform)
      }
    }

    val settings = Seq(
      Seq("organization" := groupId),
      jvmOnlyFix,
      artifactSettings,
      project.sharedSettings,
      platformSettings,
    ).flatten

    filterSettings(settings, Platform.All)
  }

  private def preparePlatformArtifacts(project: Project, artifact: Artifact, jvmOnly: Boolean)(enabledPlatforms: Seq[PlatformEnv]): Seq[PreparedArtifact] = {
    if (!jvmOnly) {
      enabledPlatforms.map {
        penv =>
          val plugins = preparePlugins(project, artifact.plugins ++ penv.plugins ++ project.globalPlugins, penv.platform, inclusive = false)

          PreparedArtifact(
            name = ArtifactId(artifact.nameOn0(penv.platform)),
            platform = penv.platform,
            parentName = artifact.name,
            settings = Nil,
            deps = filterDeps(artifact.depends, jvmOnly, penv.platform),
            libs = filterLibDeps(project, artifact.libs, jvmOnly, penv.platform),
            sbtPlugins = plugins,
          )
      }
    } else {
      Seq.empty
    }
  }
  def renderArtifact1(crossArtifact: PreparedCrossArtifact): String = {
    crossArtifact match {
      case PreparedCrossArtifact(header, settings, deps, libs, sbtPlugins, platformArtifacts) =>
        val headerStr = renderHeader(header)

        val settingsStr = crossArtifact.platform.fold(
          renderSettings(_, platformPrefix = false)(settings)
        )(renderSettings(_, platformPrefix = true)(settings))

        val depsStr = renderDeps(crossArtifact.jvmOnly, Platform.All)(deps)
        val libsStr = renderLibDeps(crossArtifact.jvmOnly, Platform.All)(libs)
        val pluginsStr = renderPlugins(dot = true)(sbtPlugins)

        val platformProjects = platformArtifacts.map(renderPlatformArtifact)

        Seq(
          Seq(headerStr),
          settingsStr,
          depsStr,
          libsStr,
          pluginsStr,
          platformProjects,
        ).flatten.mkString("\n")
    }
  }

  def renderPlatformArtifact(artifact: PreparedArtifact): String = {
    artifact match {
      case PreparedArtifact(name, platform, parentName, settings, deps, libs, sbtPlugins) =>
        val headerStr = s"""lazy val ${renderName(name)} = ${renderName(parentName)}.${platformName(platform)}"""

        val settingsStr = renderSettings(platform, platformPrefix = false)(settings)
        val depsStr = renderDeps(isJvmOnly = false, platform)(deps)
        val libsStr = renderLibDeps(isJvmOnly = false, platform)(libs)
        val pluginsStr = renderPlugins(true)(sbtPlugins)

        Seq(
          Seq(headerStr),
          Seq(settingsStr),
          depsStr,
          libsStr,
          pluginsStr,
        ).flatten.mkString("\n")
    }
  }

  def renderHeader(h: PreparedArtifactHeader): String = {
    h match {
      case PreparedArtifactHeader(name, path, JvmOnly) =>
        s"""lazy val ${renderName(name)} = project.in(file(${stringLit(path)}))"""

      case PreparedArtifactHeader(name, path, Cross(platforms)) =>
        val platformsStr = platforms.map(p => s"${platformName(p).toUpperCase}Platform").mkString(", ")
        s"""lazy val ${renderName(name)} = crossProject($platformsStr).crossType(CrossType.Pure).in(file(${stringLit(path)}))"""
    }
  }

  //
  // OLD
  //

  protected def renderArtifact0(a: Artifact): String = {
    val path = (a.pathPrefix :+ a.name.value).mkString("/")

    val enabledPlatforms = a.platforms
      .filter(p => platformEnabled(p.platform))

    val header = if (!a.isJvmOnly) {
      val platforms = enabledPlatforms.map(p => platformName(p.platform).toUpperCase()).map(pn => s"${pn}Platform")
      s"""lazy val ${renderName(a.name)} = crossProject${platforms.mkString("(", ", ", ")")}.crossType(CrossType.Pure).in(file(${stringLit(path)}))"""
    } else {
      s"""lazy val ${renderName(a.name)} = project.in(file(${stringLit(path)}))"""
    }

    val platforms = if (!a.isJvmOnly) {
      enabledPlatforms
        .flatMap {
          p =>
            val settings = Seq(
              "scalaVersion" := "crossScalaVersions.value.head".raw,
              "crossScalaVersions" := p.language.map(_.value),
            ) ++ p.settings ++ a.settings
            Some(formatSettings(settings, p.platform, platformPrefix = true))
        }
    } else {
      None
    }

    val platformProjects: Seq[String] = if (!a.isJvmOnly) {
      enabledPlatforms.map {
        p =>
          val pname = platformName(p.platform)
          val plugins = formatPlugins(a.plugins ++ p.plugins ++ project.globalPlugins, p.platform, dot = true, inclusive = false)

          Seq(
            Seq(s"""lazy val ${a.nameOn(p.platform)} = ${renderName(a.name)}.$pname"""),
            formatDeps(a, p.platform).map(_.shift(2)),
            formatLibDeps(a, p.platform),
            plugins,
          )
            .flatten
            .mkString("\n")
      }
    } else {
      Seq.empty
    }

    val groupId = (Seq(config.settings.groupId) ++ a.subGroupId.toSeq).mkString(".")

    val more = if (a.isJvmOnly) {
      val p = a.platforms.filter(_.platform == Platform.Jvm).head
      Seq(
        "scalaVersion" := "crossScalaVersions.value.head".raw,
        "crossScalaVersions" := p.language.map(_.value),
        "publishArtifact" in SettingScope.Raw("(Test, packageBin)") := true,
        "publishArtifact" in SettingScope.Raw("(Test, packageDoc)") := false,
        "publishArtifact" in SettingScope.Raw("(Test, packageSrc)") := true,
      )
    } else {
      Seq.empty
    }

    val jvmOnlyFix = if (config.jvmOnly) {
      Seq(
        "unmanagedSourceDirectories" in SettingScope.Compile += """baseDirectory.value / ".jvm/src/main/scala" """.raw,
        "unmanagedResourceDirectories" in SettingScope.Compile += """baseDirectory.value / ".jvm/src/main/resources" """.raw,
        "unmanagedSourceDirectories" in SettingScope.Test += """baseDirectory.value / ".jvm/src/test/scala" """.raw,
        "unmanagedResourceDirectories" in SettingScope.Test += """baseDirectory.value / ".jvm/src/test/resources" """.raw,
      ) ++ a.platforms.filter(_.platform == Platform.Jvm).flatMap(_.settings)
    } else {
      Seq.empty
    }

    val sharedSettings: Seq[SettingDef] = Seq(
      "organization" := groupId,
    ) ++ more ++ jvmOnlyFix ++ a.settings ++ project.sharedSettings

    val renderedSettings = formatSettings(sharedSettings, Platform.All, false)
    val plugins = formatPlugins(a.plugins ++ project.globalPlugins, Platform.All, dot = true, inclusive = a.isJvmOnly)

    val out = Seq(
      Seq(header),
      plugins,
      Seq(renderedSettings),
      formatDeps(a, Platform.All),
      formatLibDeps(a, Platform.All),
      platforms,
      platformProjects,
    ).flatten

    out.mkString("\n")
  }

  def isPlatformEnabled(p: Platform): Boolean = {
    p match {
      case Platform.All =>
        true
      case Platform.Jvm =>
        config.jvm
      case Platform.Js =>
        config.js
      case Platform.Native =>
        config.native
    }
  }

  protected def formatPlugins(plugins: Plugins, platform: Platform, dot: Boolean, inclusive: Boolean): Seq[String] = {
    renderPlugins(dot)(preparePlugins(project, plugins, platform, inclusive))
  }

  private def renderPlugins(dot: Boolean)(preparedPlugins: PreparedPlugins): Seq[String] = {
    val PreparedPlugins(enabledPlugins, disabledPlugins) = preparedPlugins

    val enabled = if (enabledPlugins.nonEmpty) {
      Some(enabledPlugins.map(_.name).distinct.mkString("enablePlugins(", ", ", ")"))
    } else {
      None
    }

    val disabled = if (disabledPlugins.nonEmpty) {
      Some(disabledPlugins.map(_.name).distinct.mkString("disablePlugins(", ", ", ")"))
    } else {
      None
    }

    val out = enabled.toList ++ disabled
    if (dot) {
      out.map(s => s".$s").map(_.shift(2))
    } else {
      out
    }
  }
  private def preparePlugins(project: Project, plugins: Plugins, platform: Platform, inclusive: Boolean): PreparedPlugins = {
    val predicate = (p: Plugin) => (inclusive && platform == Platform.All && isPlatformEnabled(p.platform)) || p.platform == platform

    val enabledPlugins = plugins.enabled.filter(predicate).distinct
    val disabledPlugins = plugins.disabled.filter(predicate).distinct

    val conflictingNames = enabledPlugins.map(_.name).toSet.intersect(disabledPlugins.map(_.name).toSet).map {
      name =>
        name -> project.pluginConflictRules(name)
    }.toMap

    val enabledPlugins0 = enabledPlugins.filter(p => conflictingNames.get(p.name) match {
      case Some(value) =>
        value
      case None =>
        true
    })
    val disabledPlugins0 = disabledPlugins.filter(p => conflictingNames.get(p.name) match {
      case Some(value) =>
        !value
      case None =>
        true
    })

    PreparedPlugins(enabledPlugins0, disabledPlugins0)
  }

  protected def filterSettings(settings: Seq[SettingDef], platform: Platform): Seq[SettingDef] = {
    settings
      .filter(s => s.scope.platform == platform || s.scope.platform == Platform.All || (config.jvmOnly && platform == Platform.All && s.scope.platform == Platform.Jvm))
  }

  protected def formatSettings(settings: Seq[SettingDef], platform: Platform, platformPrefix: Boolean): String = {
    renderSettings(platform, platformPrefix)(filterSettings(settings, platform))
  }

  protected def renderSettings(platform: Platform, platformPrefix: Boolean)(settings: Seq[SettingDef]): String = {
    val p = (platformPrefix, platform) match {
      case (true, bp: BasePlatform) =>
        s"${platformName(bp)}Settings"
      case _ =>
        "settings"
    }

    settings
      .map(renderSetting)
      .map(_.shift(2))
      .mkString(s".$p(\n", ",\n", "\n)")
      .shift(2)
  }

  private def cached(s: String): String = {
    if (config.compactify) {
      val idx = settingsCache.getOrElseUpdate(s, settingsCache.size)
      cacheIdxName(idx) + ".value"
    } else {
      s
    }
  }

  private def cacheIdxName(idx: Int) = {
    s"setting_${idx}"
  }

  protected def formatLibDeps(artifact: Artifact, targetPlatform: Platform): Option[String] = {
    val sharedArtDeps = filterLibDeps(project, artifact.libs, artifact.isJvmOnly, targetPlatform)
    renderLibDeps(artifact.isJvmOnly, targetPlatform)(sharedArtDeps)
  }

  protected def formatDeps(artifact: Artifact, targetPlatform: Platform): Option[String] = {
    val sharedArtDeps = filterDeps(artifact.depends, artifact.isJvmOnly, targetPlatform)
    renderDeps(artifact.isJvmOnly, targetPlatform)(sharedArtDeps)
  }

  //
  // FILTER
  //

  private def filterDeps(deps: Seq[ScopedDependency], isJvmOnly: Boolean, targetPlatform: Platform): Seq[ScopedDependency] = {
    val predicate = targetPlatform match {
      case platform: BasePlatform =>
        (d: ScopedDependency) => d.scope.platform == platform
      case Platform.All =>
        (d: ScopedDependency) => isJvmOnly || (d.scope.platform == Platform.All && !index(d.name).isJvmOnly)
    }

    deps.filter(predicate)
  }

  protected def filterLibDeps(project: Project, libs: Seq[ScopedLibrary], isJvmOnly: Boolean, targetPlatform: Platform): Seq[ScopedLibrary] = {
    (project.globalLibs ++ libs)
      .filter(d => (isJvmOnly && targetPlatform == Platform.All) || d.scope.platform == targetPlatform)
  }

  //
  // RENDER
  //

  protected def renderSetting(settingDef: SettingDef): String = {
    settingDef match {
      case settingDef: SettingDef.KVSettingDef =>
        renderKVSetting(settingDef)
      case SettingDef.RawSettingDef(value, _) =>
        value
    }
  }

  protected def renderKVSetting(settingDef: SettingDef.KVSettingDef): String = {
    val name = settingDef.name

    val in = settingDef.scope.scope match {
      case SettingScope.Project =>
        Seq.empty
      case SettingScope.Build =>
        Seq("in", "ThisBuild")
      case SettingScope.Test =>
        Seq("in", "Test")
      case SettingScope.Compile =>
        Seq("in", "Compile")
      case SettingScope.Raw(value) =>
        Seq("in", value)
    }

    val op = settingDef.op match {
      case SettingOp.Append =>
        "+="
      case SettingOp.Assign =>
        ":="
      case SettingOp.Extend =>
        "++="
    }

    val out = settingDef match {
      case u: SettingDef.UnscopedSettingDef =>

        if (u.value.isInstanceOf[Const.CRaw]) {
          renderConst(u.value)
        } else {
          cached(renderConst(u.value))
        }

      case s: SettingDef.ScopedSettingDef =>

        val r = s
          .defs
          .toSeq
          .map {
            case (key, v) =>
              val language = key.language match {
                case Some(value) =>
                  stringLit(value.value)
                case None =>
                  "_"
              }

              val snapshot = key.release match {
                case Some(value) =>
                  (!value).toString
                case None =>
                  "_"
              }
              s"case ($snapshot, $language) => ${renderConst(v)}"
          }
          .map(_.shift(2))
          .mkString(
            "{ (isSnapshot.value, scalaVersion.value) match {\n", "\n", "\n} }",
          )

        if (s.defs.exists(_._2.isInstanceOf[Const.CRaw])) {
          r
        } else {
          cached(r)
        }
    }

    val all = Seq(name) ++ in ++ Seq(op, out)
    all.mkString(" ")
  }

  protected def renderLibDeps(isJvmOnly: Boolean, targetPlatform: Platform)(sharedArtDeps: Seq[ScopedLibrary]): Option[String] = {
    if (sharedArtDeps.nonEmpty) {
      val libDeps: Seq[Const] = sharedArtDeps.map(renderLib(isJvmOnly, targetPlatform))
      val settings = Seq("libraryDependencies" ++= libDeps)

      //      Some(formatSettings(settings, targetPlatform, false))
      Some(renderSettings(targetPlatform, platformPrefix = false)(settings))
    } else {
      None
    }
  }

  protected def renderLib(isJvmOnly: Boolean, targetPlatform: Platform)(lib: ScopedLibrary): Const.CRaw = {
    val sep = lib.dependency.kind match {
      case LibraryType.AutoJvm =>
        "%%"
      case LibraryType.Invariant =>
        "%"
      case LibraryType.Auto =>
        targetPlatform match {
          case Platform.Jvm =>
            "%%"
          case Platform.Js =>
            "%%%"
          case Platform.Native =>
            "%%%"
          case Platform.All if isJvmOnly =>
            "%%"
          case Platform.All =>
            "%%%"
        }
    }

    val suffix = lib.scope.scope match {
      case Scope.Runtime =>
        Seq("%", "Runtime")
      case Scope.Optional =>
        Seq("%", "Optional")
      case Scope.Provided =>
        Seq("%", "Provided")
      case Scope.Compile =>
        Seq.empty
      case Scope.Test =>
        Seq("%", "Test")
      case Scope.Raw(s) =>
        Seq("%", stringLit(s))
    }

    val exclusionsOrRaw = lib.dependency.more.flatMap {
      value =>
        value match {
          case LibSetting.Exclusions(exclusions) =>
            exclusions.map(e => s"exclude(${stringLit(e.group)}, ${stringLit(e.artifact)})")
          case LibSetting.Classifier(value) =>
            Seq(s"classifier ${stringLit(value)}")
          case LibSetting.Raw(value) =>
            Seq(value)
        }
    }

    val depStr = Seq(stringLit(lib.dependency.group), sep, stringLit(lib.dependency.artifact), "%", renderVersion(lib.dependency.version))

    val out = Seq(
      depStr,
      suffix,
      exclusionsOrRaw,
    ).flatten.mkString(" ")

    if (lib.compilerPlugin) {
      s"compilerPlugin($out)".raw
    } else {
      out.raw
    }
  }

  protected[sbtgen] def renderVersion(v: Version): String = {
    v match {
      case Version.VConst(value) =>
        stringLit(value)
      case Version.VExpr(value) =>
        value
      case Version.SbtGen =>
        stringLit(SbtgenMeta.extractSbtProjectVersion().getOrElse("UNKNOWN-SBTGEN"))
    }
  }

  private def renderDeps(isJvmOnly: Boolean, targetPlatform: Platform)(sharedArtDeps: Seq[ScopedDependency]): Option[String] = {
    if (sharedArtDeps.nonEmpty) {
      val res = sharedArtDeps
        .map(renderDep(targetPlatform, isJvmOnly))
        .map(_.shift(2))
        .mkString(s".dependsOn(\n", ",\n", "\n)").shift(2)

      Some(res)
    } else {
      None
    }
  }

  private def renderDep(targetPlatform: Platform, isJvmOnly: Boolean)(d: ScopedDependency): String = {
    val ad = index.get(d.name) match {
      case Some(value) =>
        value
      case None =>
        throw new RuntimeException(s"Unknown dependency: ${
          d.name
        } ")
    }
    val name = targetPlatform match {
      case Platform.All if isJvmOnly =>
        ad.nameOn(Platform.Jvm)
      case _ =>
        ad.nameOn(targetPlatform)
    }

    val scope = d.scope.scope match {
      case Scope.Test =>
        if (config.mergeTestScopes && d.mergeTestScopes) {
          "test->compile,test"
        } else {
          "test->compile"
        }
      case _ =>
        if (config.mergeTestScopes && d.mergeTestScopes) {
          "test->test;compile->compile"
        } else {
          "test->compile;compile->compile"
        }
    }
    s"$name % ${
      stringLit(scope)
    } "
  }

  protected def renderConst(const: Const): String = {
    const match {
      case Const.CInt(value) =>
        value.toString
      case Const.CString(value) =>
        stringLit(value)
      case Const.CBoolean(value) =>
        value.toString
      case Const.CRaw(value) =>
        value
      case Const.CSeq(value) =>
        value.map(renderConst).map(_.shift(2)).mkString("Seq(\n", ",\n", "\n)")
      case Const.CTuple(value) =>
        value.map(renderConst).map(_.shift(2)).mkString("(", ",", ")")
      case Const.CMap(value) =>
        value
          .toSeq
          .map {
            case (k, v) =>
              s"${
                renderConst(k)
              } -> ${
                renderConst(v)
              } "
          }
          .map(_.shift(2))
          .mkString("Map(\n", ",\n", "\n)")
      case Const.EmptySeq =>
        "Seq.empty"
      case Const.EmptyMap =>
        "Map.empty"
    }
  }

}
