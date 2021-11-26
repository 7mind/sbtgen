package izumi.sbtgen

import izumi.sbtgen.PreparedPlatform._
import izumi.sbtgen.impl.{WithArtifactExt, WithBasicRenderers, WithProjectIndex}
import izumi.sbtgen.model.Platform.BasePlatform
import izumi.sbtgen.model._
import izumi.sbtgen.tools.IzString._

import scala.collection.immutable.SortedMap
import scala.collection.mutable
import scala.collection.compat._

final case class PreparedAggregate(
  id: ArtifactId,
  pathPrefix: Seq[String],
  aggregatedNames: Seq[String],
  platform: Platform,
  plugins: Plugins,
  isRoot: Boolean,
  enableProjectSharedAggSettings: Boolean = true,
  dontIncludeInSuperAgg: Boolean = false,
  settings: Seq[SettingDef] = Seq.empty
)

final case class PreparedCrossArtifact(
  header: PreparedArtifactHeader,
  settings: Seq[SettingDef],
  // FIXME: add, remove platform logic from renderer
  //                                        settings: Map[Platform, Seq[PreparedSettingDef]],
  deps: Seq[ScopedDependency],
  libs: Seq[ScopedLibrary],
  sbtPlugins: PreparedPlugins,
  platformArtifacts: Seq[PreparedArtifact]
) {
  def platform: PreparedPlatform = header.platform
  def jvmOnly: Boolean = platform.jvmOnly
}

final case class PreparedArtifact(
  name: ArtifactId,
  platform: BasePlatform,
  parentName: ArtifactId,
  settings: Seq[SettingDef],
  deps: Seq[ScopedDependency],
  libs: Seq[ScopedLibrary],
  sbtPlugins: PreparedPlugins
)

sealed trait PreparedPlatform {
  final def fold[A](jvmOnly: Platform.Jvm.type => A)(crossAll: Platform => A): Seq[A] = {
    this match {
      case JvmOnly => Seq(jvmOnly(Platform.Jvm))
      case Cross(platforms) => crossAll(Platform.All) +: platforms.map(crossAll)
    }
  }
  final def jvmOnly: Boolean = this == JvmOnly
}
object PreparedPlatform {
  case object JvmOnly extends PreparedPlatform
  final case class Cross(platforms: Seq[BasePlatform]) extends PreparedPlatform
}

final case class PreparedArtifactHeader(
  name: ArtifactId,
  path: String,
  platform: PreparedPlatform
)

final case class PreparedPlugins(
  enabled: Seq[Plugin],
  disabled: Seq[Plugin]
)

trait WithSettingsCache {
  protected def cached(s: String): String
}

class Renderer(
  protected val config: GenConfig,
  project: Project
) extends WithProjectIndex
  with WithBasicRenderers
  with WithArtifactExt
  with WithSettingsCache
  with Renderers {

  private val aggregates: Seq[Aggregate] = project.aggregates.map(_.merge)
  protected val index: Map[ArtifactId, Artifact] = makeIndex(aggregates)
  protected val settingsCache = new mutable.LinkedHashMap[String, Int]()

  protected val configuredGroups: Set[Group] = {
    def unpackGroup(group: Group): Set[Group] = {
      Set(group) ++ group.deps.flatMap(unpackGroup)
    }

    val allGroups = aggregates.flatMap(agg => agg.groups ++ agg.artifacts.flatMap(_.groups)).toSet

    allGroups
      .filter(config.onlyGroups contains _.name)
      .flatMap(unpackGroup)
  }

  override protected def cached(s: String): String = {
    if (config.compactify) {
      val idx = settingsCache.getOrElseUpdate(s, settingsCache.size)
      cacheIdxName(idx) + ".value"
    } else {
      s
    }
  }

  def render(): Seq[String] = {
    val artifacts = aggregates.flatMap(_.filteredArtifacts).map(a => renderArtifact(prepareArtifact(project, a)))

    val filteredAggs = aggregates.flatMap(prepareCrossAggregate).filterNot(_.aggregatedNames.isEmpty)

    val superAgg = filteredAggs
      .groupBy(_.platform)
      .toSeq
      .sortBy(_._1 == Platform.All) // place Platform.All super-aggregate last
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
          PreparedAggregate(
            id = ArtifactId(name),
            pathPrefix = path,
            aggregatedNames = aggregatedIds,
            platform = p,
            plugins = project.globalPlugins,
            isRoot = p == Platform.All
          )
      }

    val allAggregates = (filteredAggs ++ superAgg).filterNot(_.aggregatedNames.isEmpty)
    assert(allAggregates.nonEmpty, "All aggregates were filtered out")
    val aggDefs = allAggregates.map(renderAggregateProject(project))

    val unexpected = project.topLevelSettings.filterNot(_.scope.platform == Platform.All)
    assert(unexpected.isEmpty, "Global settings cannot be scoped to a platform")

    val settings = project.topLevelSettings.filter(_.scope.platform == Platform.All).map(renderSetting)

    val imports = Seq(project.imports.filter(p => platformEnabled(p.platform)).map(i => s"import ${i.value}").mkString("\n"))
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
      sc
    ).flatten
  }

  protected def prepareCrossAggregate(aggregate: Aggregate): Seq[PreparedAggregate] = {
    val enableSharedSettings = aggregate.enableProjectSharedAggSettings
    val noSuperAgg = aggregate.dontIncludeInSuperAgg

    val allAggregate = {
      val fullAgg = aggregate.filteredArtifacts.flatMap {
        a =>
          a.platforms
            .filter(p => platformEnabled(p.platform))
            .map(p => if (a.isJvmOnly) renderName(a.name) else a.nameOn(p.platform))
      }

      PreparedAggregate(
        id = aggregate.name,
        pathPrefix = Seq(".agg", (aggregate.pathPrefix :+ aggregate.name.value).mkString("-")),
        aggregatedNames = fullAgg,
        platform = Platform.All,
        plugins = project.globalPlugins,
        isRoot = false,
        enableProjectSharedAggSettings = enableSharedSettings,
        dontIncludeInSuperAgg = noSuperAgg,
        settings = aggregate.settings
      )
    }

    val jvmOnly = preparePlatformAggregate(aggregate, Platform.Jvm, enableSharedSettings, noSuperAgg)
    val jsOnly = preparePlatformAggregate(aggregate, Platform.Js, enableSharedSettings, noSuperAgg)
    val nativeOnly = preparePlatformAggregate(aggregate, Platform.Native, enableSharedSettings, noSuperAgg)

    Seq(
      Some(allAggregate),
      jvmOnly,
      jsOnly,
      nativeOnly
    ).flatten
  }

  protected def preparePlatformAggregate(agg: Aggregate, platform: BasePlatform, sharedSettings: Boolean, disableSuperAgg: Boolean): Option[PreparedAggregate] = {
    if (!isPlatformEnabled(platform)) {
      None
    } else {
      val platformAgg = agg.filteredArtifacts.flatMap {
        a =>
          if (a.isJvmOnly) {
            Seq(renderName(a.name))
          } else {
            a.platforms.filter(_.platform == platform).map(a nameOn _.platform)
          }
      }
      if (platformAgg.nonEmpty) {
        val artname = Seq(agg.name.value, platformName(platform))
        val id = ArtifactId(artname.mkString("-"))
        Some(
          PreparedAggregate(
            id = id,
            pathPrefix = Seq(".agg", (agg.pathPrefix ++ artname).mkString("-")),
            aggregatedNames = platformAgg,
            platform = platform,
            plugins = project.globalPlugins,
            isRoot = false,
            enableProjectSharedAggSettings = sharedSettings,
            dontIncludeInSuperAgg = disableSuperAgg,
            settings = agg.settings
          )
        )
      } else {
        None
      }
    }
  }

  protected def renderAggregateProject(/* FIXME: move logic to prepare */ project: Project)(aggregate: PreparedAggregate): String = {
    aggregate match {
      case PreparedAggregate(name, path, agg, platform, plugins, isRoot, enableSharedSettings, _, localSettings) =>
        // FIXME: move logic to prepare
        val hack: Seq[SettingDef] = if (isRoot) {
          project.rootSettings
        } else if (enableSharedSettings) {
          project.sharedAggSettings
        } else {
          Seq.empty
        }

        val header = s"""lazy val ${renderName(name.value)} = (project in file(${stringLit(path.mkString("/"))}))"""
        val settingsStr: String = formatSettings(
          settings = Seq(
            // FIXME: move logic to prepare
            "skip" in SettingScope.Raw("publish") := true
          ) ++ localSettings ++ hack,
          platform = Platform.All,
          platformPrefix = false
        )
        val pluginsStr = formatPlugins(plugins, platform, dot = true, inclusive = true)
        val aggregateStr = agg.map(_.shift(2)).mkString(".aggregate(\n", ",\n", "\n)").shift(2)

        Seq(
          Seq(header),
          Seq(settingsStr),
          pluginsStr,
          Seq(aggregateStr)
        ).flatten.mkString("\n")
    }
  }

  //
  // NEW
  //

  protected def prepareArtifact(project: Project, artifact: Artifact): PreparedCrossArtifact = {
    val enabledPlatforms = artifact.platforms.filter(platformEnabled)
    val platform = if (artifact.isJvmOnly) JvmOnly else Cross(enabledPlatforms.map(_.platform))
    val jvmOnly = platform.jvmOnly

    val header = {
      val path = (artifact.pathPrefix :+ artifact.name.value).mkString("/")
      PreparedArtifactHeader(artifact.name, path, platform)
    }
    val settings = prepareCrossArtifactSettings(project, artifact.subGroupId, artifact.settings, enabledPlatforms)
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

  protected def prepareCrossArtifactSettings(
    project: Project,
    subGroupId: Option[String],
    artifactSettings: Seq[SettingDef],
    enabledPlatforms: Seq[PlatformEnv]
  ): Seq[SettingDef] = {
    val groupId = (config.settings.groupId :: subGroupId.toList).mkString(".")

    val jvmOnlyFix = if (config.jvmOnly) {
      Seq(
        "unmanagedSourceDirectories" in SettingScope.Compile += """baseDirectory.value / ".jvm/src/main/scala" """.raw,
        "unmanagedResourceDirectories" in SettingScope.Compile += """baseDirectory.value / ".jvm/src/main/resources" """.raw,
        "unmanagedSourceDirectories" in SettingScope.Test += """baseDirectory.value / ".jvm/src/test/scala" """.raw,
        "unmanagedResourceDirectories" in SettingScope.Test += """baseDirectory.value / ".jvm/src/test/resources" """.raw
      )
    } else Seq.empty

    val platformSettings = {
      enabledPlatforms.flatMap {
        penv =>
          val psettings = Seq(
            "crossScalaVersions" := penv.language.map(_.value),
            "scalaVersion" := "crossScalaVersions.value.head".raw
          ) ++ penv.settings

          filterSettings(psettings.map(_.withPlatform(penv.platform)), penv.platform) ++
          filterSettings(artifactSettings, penv.platform) ++
          filterSettings(project.sharedSettings, penv.platform)
      }
    }

    val settingsAll = Seq(
      Seq("organization" := groupId),
      jvmOnlyFix,
      project.sharedSettings,
      artifactSettings
    ).flatten

    platformSettings ++
    filterSettings(settingsAll, Platform.All)
  }

  protected def preparePlatformArtifacts(project: Project, artifact: Artifact, jvmOnly: Boolean)(enabledPlatforms: Seq[PlatformEnv]): Seq[PreparedArtifact] = {
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
            sbtPlugins = plugins
          )
      }
    } else {
      Seq.empty
    }
  }

  protected def preparePlugins(project: Project, plugins: Plugins, platform: Platform, inclusive: Boolean): PreparedPlugins = {
    val predicate = (p: Plugin) => (inclusive && platform == Platform.All && isPlatformEnabled(p.platform)) || p.platform == platform

    val enabledPlugins = plugins.enabled.filter(predicate).distinct
    val disabledPlugins = plugins.disabled.filter(predicate).distinct

    val conflictingNames = enabledPlugins
      .map(_.name).toSet.intersect(disabledPlugins.map(_.name).toSet).map {
        name =>
          name -> project.pluginConflictRules(name)
      }.toMap

    val enabledPlugins0 = enabledPlugins.filter(
      p =>
        conflictingNames.get(p.name) match {
          case Some(value) =>
            value
          case None =>
            true
        }
    )
    val disabledPlugins0 = disabledPlugins.filter(
      p =>
        conflictingNames.get(p.name) match {
          case Some(value) =>
            !value
          case None =>
            true
        }
    )

    PreparedPlugins(enabledPlugins0, disabledPlugins0)
  }

  //
  // OLD
  //

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

  @deprecated(".", ".")
  protected def formatPlugins(plugins: Plugins, platform: Platform, dot: Boolean, inclusive: Boolean): Seq[String] = {
    renderPlugins(dot)(preparePlugins(project, plugins, platform, inclusive))
  }

  @deprecated(".", ".")
  protected def formatSettings(settings: Seq[SettingDef], platform: Platform, platformPrefix: Boolean): String = {
    val filteredSettings = settings.filter {
      s =>
        s.scope.platform == platform ||
        s.scope.platform == Platform.All ||
        (config.jvmOnly && platform == Platform.All && s.scope.platform == Platform.Jvm)
    }
    renderSettings(platform, platformPrefix)(filteredSettings)
  }

  protected def cacheIdxName(idx: Int): String = {
    s"setting_$idx"
  }

  //
  // FILTER
  //

  protected def filterSettings(settings: Seq[SettingDef], platform: Platform): Seq[SettingDef] = {
    settings.filter(_.scope.platform == platform)
  }

  protected def filterDeps(deps: Seq[ScopedDependency], isJvmOnly: Boolean, targetPlatform: Platform): Seq[ScopedDependency] = {
    val predicate = targetPlatform match {
      case _: BasePlatform =>
        (d: ScopedDependency) => d.scope.platform == targetPlatform
      case Platform.All =>
        (d: ScopedDependency) => isJvmOnly && d.scope.platform.supportsPlatform(Platform.Jvm) || (d.scope.platform == Platform.All && !index(d.name).isJvmOnly)
    }

    deps.filter(predicate)
  }

  protected def filterLibDeps(project: Project, libs: Seq[ScopedLibrary], isJvmOnly: Boolean, targetPlatform: Platform): Seq[ScopedLibrary] = {
    val predicate = targetPlatform match {
      case _: BasePlatform =>
        (d: ScopedLibrary) => d.scope.platform == targetPlatform
      case Platform.All =>
        (d: ScopedLibrary) => isJvmOnly && d.scope.platform.supportsPlatform(Platform.Jvm) || d.scope.platform == targetPlatform
    }

    (project.globalLibs ++ libs).filter(predicate)
  }

}

//
// RENDER
//

trait Renderers extends WithArtifactExt with WithBasicRenderers with WithProjectIndex {
  this: WithSettingsCache =>

  protected def renderArtifact(crossArtifact: PreparedCrossArtifact): String = {
    crossArtifact match {
      case PreparedCrossArtifact(header, settings, deps, libs, sbtPlugins, platformArtifacts) =>
        val headerStr = renderHeader(header)

        val settingsStr = crossArtifact
          .platform.fold(jvmOnly => nonEmpty(renderSettings(jvmOnly, platformPrefix = false))(settings))(
            p => nonEmpty(renderSettings(p, platformPrefix = true))(settings.filter(_.scope.platform == p))
          ).flatten

        val depsStr = renderDeps(crossArtifact.jvmOnly, Platform.All)(deps)
        val libsStr = renderLibDeps(crossArtifact.jvmOnly, Platform.All)(libs)
        val pluginsStr = renderPlugins(dot = true)(sbtPlugins)

        val platformProjects = platformArtifacts.map(renderPlatformArtifact)

        Seq[Seq[String]](
          Seq(headerStr),
          depsStr.toSeq,
          libsStr.toSeq,
          settingsStr,
          pluginsStr,
          platformProjects
        ).flatten.mkString("\n")
    }
  }

  protected def renderPlatformArtifact(artifact: PreparedArtifact): String = {
    artifact match {
      case PreparedArtifact(name, platform, parentName, settings, deps, libs, sbtPlugins) =>
        val headerStr = s"""lazy val ${renderName(name)} = ${renderName(parentName)}.${platformName(platform)}"""

        val settingsStr = nonEmpty(renderSettings(platform, platformPrefix = false))(settings)
        val depsStr = renderDeps(isJvmOnly = false, platform)(deps)
        val libsStr = renderLibDeps(isJvmOnly = false, platform)(libs)
        val pluginsStr = renderPlugins(dot = true)(sbtPlugins)

        Seq[Seq[String]](
          Seq(headerStr),
          depsStr.toSeq,
          libsStr.toSeq,
          settingsStr,
          pluginsStr
        ).flatten.mkString("\n")
    }
  }

  protected def renderHeader(h: PreparedArtifactHeader): String = {
    h match {
      case PreparedArtifactHeader(name, path, JvmOnly) =>
        s"""lazy val ${renderName(name)} = project.in(file(${stringLit(path)}))"""

      case PreparedArtifactHeader(name, path, Cross(platforms)) =>
        val platformsStr = platforms.map(p => s"${platformTypeName(p)}Platform").mkString(", ")
        s"""lazy val ${renderName(name)} = crossProject($platformsStr).crossType(CrossType.Pure).in(file(${stringLit(path)}))"""
    }
  }

  protected def renderPlugins(dot: Boolean)(preparedPlugins: PreparedPlugins): Seq[String] = {
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
        Seq("ThisBuild", "/")
      case SettingScope.Test =>
        Seq("Test", "/")
      case SettingScope.Compile =>
        Seq("Compile", "/")
      case SettingScope.Raw(value) =>
        Seq(value, "/")
    }

    val op = settingDef.op match {
      case SettingOp.Assign =>
        ":="
      case SettingOp.Modify =>
        "%="
      case SettingOp.Append =>
        "+="
      case SettingOp.Extend =>
        "++="
      case SettingOp.Remove =>
        "-="
      case SettingOp.Subtract =>
        "--="
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
            "{ (isSnapshot.value, scalaVersion.value) match {\n",
            "\n",
            "\n} }"
          )

        if (s.defs.exists(_._2.isInstanceOf[Const.CRaw])) {
          r
        } else {
          cached(r)
        }
    }

    val all = in ++ Seq(name) ++ Seq(op, out)
    all.mkString(" ")
  }

  protected def renderLibDeps(isJvmOnly: Boolean, targetPlatform: Platform)(sharedArtDeps: Seq[ScopedLibrary]): Option[String] = {
    if (sharedArtDeps.nonEmpty) {
      val settings = renderLibs(isJvmOnly, targetPlatform)(sharedArtDeps)
      Some(renderSettings(targetPlatform, platformPrefix = false)(settings))
    } else {
      None
    }
  }

  protected def renderLibs(isJvmOnly: Boolean, targetPlatform: Platform)(allLibs: Seq[ScopedLibrary]): Seq[SettingDef] = {
    def setting(c: Const) = Seq("libraryDependencies" ++= c)
    def libConsts(libs: Seq[ScopedLibrary]): Seq[Const.CRaw] = libs.map(renderLib(isJvmOnly, targetPlatform))
    val _ = IterableOnce // prevent unused import compat warning

    allLibs
      .groupBy(_.scope.scalaVersionScope).to(SortedMap).flatMap {
        case (scope, libs) =>
          scope match {
            case ScalaVersionScope.AllVersions =>
              setting(libConsts(libs))

            case ScalaVersionScope.AllScala2 =>
              setting(s"""{ if (scalaVersion.value.startsWith("2.")) ${renderConst(libConsts(libs))} else Seq.empty }""".raw)

            case ScalaVersionScope.AllScala3 =>
              setting(s"""{
                         |  val version = scalaVersion.value
                         |  if (version.startsWith("0.") || version.startsWith("3.")) {
                         |${renderConst(libConsts(libs)).shift(4)}
                         |  } else Seq.empty
                         |}""".stripMargin.raw)

            case ScalaVersionScope.Versions(versions) =>
              setting(
                s"""{ if (${renderConst(versions.map(Const.CString apply _.value))} contains scalaVersion.value) ${renderConst(libConsts(libs))} else Seq.empty }""".raw
              )
          }
      }.toSeq
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
            exclusions.map(e => s"exclude (${stringLit(e.group)}, ${stringLit(e.artifact)})")
          case LibSetting.Classifier(value) =>
            Seq(s"classifier ${stringLit(value)}")
          case LibSetting.Raw(value) =>
            Seq(value)
        }
    }

    val libLiteral = Seq(stringLit(lib.dependency.group), sep, stringLit(lib.dependency.artifact), "%", renderVersion(lib.dependency.version))

    val out = Seq(libLiteral, suffix, exclusionsOrRaw).flatten.mkString(" ")

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
        stringLit(Version.SbtGen.value)
    }
  }

  protected def renderDeps(isJvmOnly: Boolean, targetPlatform: Platform)(sharedArtDeps: Seq[ScopedDependency]): Option[String] = {
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

  protected def renderDep(targetPlatform: Platform, isJvmOnly: Boolean)(d: ScopedDependency): String = {
    val ad = index.get(d.name) match {
      case Some(value) =>
        value
      case None =>
        throw new RuntimeException(s"Unknown dependency: ${d.name} ")
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
    s"$name % ${stringLit(scope)}"
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
              s"${renderConst(k)} -> ${renderConst(v)} "
          }
          .map(_.shift(2))
          .mkString("Map(\n", ",\n", "\n)")
      case Const.EmptySeq =>
        "Seq.empty"
      case Const.EmptyMap =>
        "Map.empty"
    }
  }

  private[this] def nonEmpty[A](f: Seq[A] => String)(c: Seq[A]): Seq[String] = {
    if (c.isEmpty) Nil else Seq(f(c))
  }

}
