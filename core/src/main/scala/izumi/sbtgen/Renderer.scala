package izumi.sbtgen

import izumi.sbtgen.impl.{WithArtifactExt, WithBasicRenderers, WithProjectIndex}
import izumi.sbtgen.tools.IzString._
import izumi.sbtgen.model._
import izumi.sbtgen.output.PreparedAggregate


class Renderer(protected val config: GenConfig, project: Project)
  extends WithProjectIndex
    with WithBasicRenderers
    with WithArtifactExt {
  private val aggregates: Seq[Aggregate] = project.aggregates.map(_.merge)
  protected val index: Map[ArtifactId, Artifact] = makeIndex(aggregates)

  def render(): Seq[String] = {
    val artifacts = aggregates.flatMap(_.filteredArtifacts).map(renderArtifact)

    val aggs = aggregates.flatMap(renderAgg)

    val superAgg = aggs
      .groupBy(_.target)
      .toSeq
      .map {
        case (p, group) =>
          val id = p match {
            case platform: Platform.BasePlatform =>
              Some(platformName(platform).toLowerCase)
            case Platform.All =>
              None
          }
          val name = (project.name.value +: id.toSeq).mkString("-")
          val path = id match {
            case Some(x) =>
              Seq(".", ".agg-" + x.toLowerCase)
            case None =>
              Seq(".")
          }

          val aggregatedIds = group.filterNot(_.dontIncludeInSuperAgg).map(a => renderName(a.id))
          PreparedAggregate(ArtifactId(name), path, aggregatedIds, p, project.globalPlugins, isRoot = p == Platform.All)
      }

    val allAggregates = (aggs ++ superAgg).filterNot(_.aggregatedNames.isEmpty)
    assert(allAggregates.nonEmpty, "All aggregates were filtered out")
    val aggDefs = renderAggregateProjects(allAggregates)

    val unexpected = project.settings.filterNot(_.scope.platform == Platform.All)
    assert(unexpected.isEmpty, "Global settings cannot be scoped to a platform")

    val settings = project.settings.filter(_.scope.platform == Platform.All).map(renderSetting)

    val imports = Seq(project.imports.map(i => s"import ${i.value}").mkString("\n"))
    val plugins = renderPlugins(project.rootPlugins, Platform.All, dot = false, inclusive = true)

    Seq(
      imports,
      plugins,
      settings,
      artifacts,
      aggDefs
    )
      .flatten
  }

  protected def renderAgg(a: Aggregate): Seq[PreparedAggregate] = {
    val es = a.enableSharedSettings
    val di = a.dontIncludeInSuperAgg
    val fullAgg = a.filteredArtifacts.flatMap {
      a =>
        if (a.isJvmOnly) {
          Seq(renderName(a.name))
        } else {
          a.platforms.map(p => a.nameOn(p.platform))
        }
    }

    val jvmOnly = if (config.jvmOnly) {
      Seq.empty
    } else {
      val jvmAgg = a.filteredArtifacts.flatMap {
        a =>
          if (a.isJvmOnly) {
            Seq(renderName(a.name))
          } else {
            a.platforms.filter(_.platform == Platform.Jvm).map(p => a.nameOn(p.platform))
          }
      }
      if (jvmAgg.nonEmpty) {
        Seq(output.PreparedAggregate(
          ArtifactId(a.name.value + "-jvm"),
          a.pathPrefix ++ Seq(".agg-jvm"),
          jvmAgg,
          Platform.Jvm,
          project.globalPlugins,
          enableSharedSettings = es,
          dontIncludeInSuperAgg = di,
        ))
      } else {
        Seq.empty
      }
    }

    val jsOnly = if (config.jvmOnly || !config.js) {
      Seq.empty
    } else {
      val jsAgg = a.filteredArtifacts.flatMap {
        a =>
          a.platforms.filter(_.platform == Platform.Js).map(p => a.nameOn(p.platform))

      }
      if (jsAgg.nonEmpty) {
        Seq(output.PreparedAggregate(
          ArtifactId(a.name.value + "-js"),
          a.pathPrefix ++ Seq(".agg-js"),
          jsAgg,
          Platform.Js,
          project.globalPlugins,
          enableSharedSettings = es,
          dontIncludeInSuperAgg = di,
        ))
      } else {
        Seq.empty
      }
    }

    val nativeOnly = if (config.jvmOnly || !config.native) {
      Seq.empty
    } else {
      val nativeAgg = a.filteredArtifacts.flatMap {
        a =>
          a.platforms.filter(_.platform == Platform.Native).map(p => a.nameOn(p.platform))
      }
      if (nativeAgg.nonEmpty) {
        Seq(output.PreparedAggregate(
          ArtifactId(a.name.value + "-native"),
          a.pathPrefix ++ Seq(".agg-native"),
          nativeAgg,
          Platform.Native,
          project.globalPlugins,
          enableSharedSettings = es,
          dontIncludeInSuperAgg = di,
        ))
      } else {
        Seq.empty
      }
    }

    Seq(output.PreparedAggregate(
      a.name,
      a.pathPrefix,
      fullAgg,
      Platform.All,
      project.globalPlugins,
      enableSharedSettings = es,
      dontIncludeInSuperAgg = di,
    )) ++ jvmOnly ++ jsOnly ++ nativeOnly
  }


  protected def renderAggregateProjects(aggregates: Seq[PreparedAggregate]): Seq[String] = {
    val settings = Seq(
      //      "publish" := "{}".raw,
      //      "publishLocal" := "{}".raw,
      //      "skip in publish" := true,
      "skip" in SettingScope.Raw("publish") := true,
    )


    val aggDefs = aggregates.map {
      case PreparedAggregate(name, path, agg, platform, plugins, isRoot, enableSharedSettings, _) =>
        val hack = if (isRoot) {
          project.sharedRootSettings
        } else if (enableSharedSettings) {
          project.sharedAggSettings
        } else {
          Seq.empty
        }

        val s = renderSettings(settings ++ hack, Platform.All)

        val header = s"""lazy val ${renderName(name.value)} = (project in file(${stringLit(path.mkString("/"))}))"""
        val names = agg.map(_.shift(2)).mkString(".aggregate(\n", ",\n", "\n)").shift(2)
        val p = renderPlugins(plugins, platform, dot = true, inclusive = true)
        (Seq(header, s) ++ p ++ Seq(names)).mkString("\n")
    }
    aggDefs
  }

  protected def renderArtifact(a: Artifact): String = {
    val path = (a.pathPrefix :+ a.name.value).mkString("/")

    val header = if (!a.isJvmOnly) {
      val platforms = a.platforms.map(p => platformName(p.platform).toUpperCase()).map(pn => s"${pn}Platform")
      s"""lazy val ${renderName(a.name)} = crossProject${platforms.mkString("(", ", ", ")")}.crossType(CrossType.Pure).in(file(${stringLit(path)}))"""
    } else {
      s"""lazy val ${renderName(a.name)} = project.in(file(${stringLit(path)}))"""
    }

    val enabledPlatforms = a.platforms
      .filter(p => platformEnabled(p.platform))

    val platforms = if (!a.isJvmOnly) {
      enabledPlatforms
        .flatMap {
          p =>
            val prefix = platformName(p.platform)
            val settings = Seq(
              "scalaVersion" := "crossScalaVersions.value.head".raw,
              "crossScalaVersions" := p.language.map(_.value)
            ) ++ p.settings ++ a.settings
            Seq(renderSettings(settings, p.platform, Some(prefix)))
        }
    } else {
      Seq.empty
    }

    val platformProjects = if (!a.isJvmOnly) {
      enabledPlatforms.map {
        p =>
          val pname = platformName(p.platform)
          val plugins = renderPlugins(a.plugins ++ p.plugins ++ project.globalPlugins, p.platform, dot = true)

          Seq(
            Seq(s"""lazy val ${a.nameOn(p.platform)} = ${renderName(a.name)}.$pname"""),
            formatDeps(a, p.platform).map(_.shift(2)),
            formatLibDeps(a, p.platform),
            plugins
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
        "unmanagedSourceDirectories" in SettingScope.Compile += """baseDirectory.value / ".jvm/src/main" """.raw,
        "unmanagedSourceDirectories" in SettingScope.Test += """baseDirectory.value / ".jvm/src/main/test" """.raw,
      )
    } else {
      Seq.empty
    }

    val sharedSettings = Seq(
      "organization" := groupId,
    ) ++ more ++ jvmOnlyFix ++ a.settings ++ project.sharedSettings

    val renderedSettings = renderSettings(sharedSettings, Platform.All)
    val plugins = renderPlugins(a.plugins ++ project.globalPlugins, Platform.All, dot = true, inclusive = a.isJvmOnly)

    val out = Seq(
      Seq(header),
      plugins,
      Seq(renderedSettings),
      formatDeps(a, Platform.All),
      formatLibDeps(a, Platform.All),
      platforms,
      platformProjects
    ).flatten

    out.mkString("\n")
  }

  protected def renderPlugins(plugins: Plugins, platform: Platform, dot: Boolean, inclusive: Boolean = false): Seq[String] = {
    val predicate = (p: Plugin) => (inclusive && platform == Platform.All) || p.platform == platform

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


    val enabled = if (enabledPlugins0.nonEmpty) {
      Seq(enabledPlugins0.map(_.name).distinct.mkString("enablePlugins(", ", ", ")"))
    } else {
      Seq.empty
    }

    val disabled = if (disabledPlugins0.nonEmpty) {
      Seq(disabledPlugins0.map(_.name).distinct.mkString("disablePlugins(", ", ", ")"))
    } else {
      Seq.empty
    }


    val out = enabled ++ disabled
    if (dot) {
      out.map(s => '.' + s).map(_.shift(2))
    } else {
      out
    }
  }

  protected def renderSettings(settings: Seq[SettingDef], platform: Platform, prefix: Option[String] = None): String = {
    val p = prefix match {
      case Some(value) =>
        s"${value}Settings"
      case None =>
        "settings"
    }
    settings
      .filter(s => s.scope.platform == platform || s.scope.platform == Platform.All)
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
        renderConst(u.value)
      case s: SettingDef.ScopedSettingDef =>


        s
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
            "{ (isSnapshot.value, scalaVersion.value) match {\n", "\n", "\n} }"
          )
    }
    val all = Seq(name) ++ in ++ Seq(op, out)
    all.mkString(" ")
  }

  protected def renderConst(const: Const): String = {
    const match {
      case scalar: Const.Scalar =>
        scalar match {
          case Const.CInt(value) =>
            value.toString
          case Const.CString(value) =>
            stringLit(value)
          case Const.CBoolean(value) =>
            value.toString
          case Const.CRaw(value) =>
            value
        }
      case Const.CSeq(value) =>
        value.map(renderConst).map(_.shift(2)).mkString("Seq(\n", ",\n", "\n)")
      case Const.CTuple(value) =>
        value.map(renderConst).map(_.shift(2)).mkString("(", ",", ")")
      case Const.CMap(value) =>
        value
          .toSeq
          .map {
            case (k, v) =>
              s"${renderConst(k)} -> ${renderConst(v)}"
          }
          .map(_.shift(2))
          .mkString("Map(\n", ",\n", "\n)")
      case Const.EmptySeq =>
        "Seq.empty"
      case Const.EmptyMap =>
        "Map.empty"
    }
  }

  protected def formatLibDeps(a: Artifact, targetPlatform: Platform): Seq[String] = {
    val deps = project.globalLibs ++ a.libs

    val sharedArtDeps = deps.filter(d => (a.isJvmOnly && targetPlatform == Platform.All) || d.scope.platform == targetPlatform)

    val artDeps = if (sharedArtDeps.nonEmpty) {
      val deps = sharedArtDeps
        .map {
          d =>
            val sep = d.dependency.kind match {
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
                  case Platform.All if a.isJvmOnly =>
                    "%%"
                  case Platform.All =>
                    "%%%"
                }
            }

            val suffix = d.scope.scope match {
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
            }

            val out = (Seq(stringLit(d.dependency.group), sep, stringLit(d.dependency.artifact), "%", renderVersion(d.dependency.version)) ++ suffix).mkString(" ")

            if (d.compilerPlugin) {
              s"compilerPlugin($out)".raw
            } else {
              out.raw
            }
        }
      val settings = Seq("libraryDependencies" ++= deps)

      Seq(renderSettings(settings, targetPlatform))
    } else {
      Seq.empty
    }
    artDeps
  }

  def renderVersion(v: Version): String = {
    v match {
      case Version.VConst(value) =>
        stringLit(value)
      case Version.VExpr(value) =>
        value
    }
  }

  protected def formatDeps(a: Artifact, targetPlatform: Platform): Seq[String] = {
    val deps = a.depends
    val predicate = targetPlatform match {
      case platform: Platform.BasePlatform =>
        (d: ScopedDependency) => d.scope.platform == platform
      case Platform.All =>
        (d: ScopedDependency) => a.isJvmOnly || (d.scope.platform == Platform.All && !index(d.name).isJvmOnly) || (index(d.name).isJvmOnly && d.scope.platform == Platform.Jvm && a.isJvmOnly)
    }

    val sharedArtDeps = deps.filter(predicate)

    val artDeps = if (sharedArtDeps.nonEmpty) {
      Seq(sharedArtDeps
        .map {
          d =>
            val ad = index(d.name)
            val name = targetPlatform match {
              case Platform.All if a.isJvmOnly =>
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
        .map(_.shift(2))
        .mkString(s".dependsOn(\n", ",\n", "\n)").shift(2)
      )
    } else {
      Seq.empty
    }
    artDeps
  }


}
