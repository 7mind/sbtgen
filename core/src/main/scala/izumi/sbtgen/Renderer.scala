package izumi.sbtgen

import izumi.sbtgen.impl.{WithArtifactExt, WithBasicRenderers, WithProjectIndex}
import izumi.sbtgen.tools.IzString._
import izumi.sbtgen.model._
import izumi.sbtgen.output.PreparedAggregate



class Renderer(protected val config: GenConfig, project: Project)
  extends WithProjectIndex
    with WithBasicRenderers
    with WithArtifactExt {
  protected val index: Map[ArtifactId, Artifact] = makeIndex(project.aggregates)

  def render(): Seq[String] = {
    val artifacts = project.aggregates.flatMap(_.filteredArtifacts).map(renderArtifact)

    val aggs = project.aggregates.flatMap(renderAgg)

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
              Seq(".", ".agg-" + x.toLowerCase).mkString("/")
            case None =>
              "."
          }

          PreparedAggregate(ArtifactId(name), path, group.map(a => renderName(a.id)), p)
      }

    val allAggregates = (aggs ++ superAgg).filterNot(_.aggregatedNames.isEmpty)
    assert(allAggregates.nonEmpty, "All aggregates were filtered out")
    val aggDefs = renderAggregateProjects(allAggregates)

    Seq(artifacts, aggDefs)
      .flatten
  }

  protected def renderAgg(a: Aggregate): Seq[PreparedAggregate] = {
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
      Seq(output.PreparedAggregate(ArtifactId(a.name.value + "-jvm"), a.path + "/.agg-jvm", jvmAgg, Platform.Jvm))
    }

    val jsOnly = if (config.jvmOnly || !config.js) {
      Seq.empty
    } else {
      val jsAgg = a.filteredArtifacts.flatMap {
        a =>
          a.platforms.filter(_.platform == Platform.Js).map(p => a.nameOn(p.platform))

      }
      Seq(output.PreparedAggregate(ArtifactId(a.name.value + "-js"), a.path + "/.agg-js", jsAgg, Platform.Js))
    }

    val nativeOnly = if (config.jvmOnly || !config.native) {
      Seq.empty
    } else {
      val nativeAgg = a.filteredArtifacts.flatMap {
        a =>
          a.platforms.filter(_.platform == Platform.Native).map(p => a.nameOn(p.platform))
      }
      Seq(output.PreparedAggregate(ArtifactId(a.name.value + "-native"), a.path + "/.agg-native", nativeAgg, Platform.Native))
    }

    Seq(output.PreparedAggregate(a.name, a.path, fullAgg, Platform.All)) ++ jvmOnly ++ jsOnly ++ nativeOnly
  }


  protected def renderAggregateProjects(aggregates: Seq[PreparedAggregate]): Seq[String] = {
    val settings = Seq(
      "publish" := "{}".raw,
      "publishLocal" := "{}".raw,
      "skip in publish" := true,
    )

    val s = renderSettings(settings)

    val aggDefs = aggregates.map {
      case PreparedAggregate(name, path, agg, _) =>
        val header = s"""lazy val ${renderName(name.value)} = (project in file(${stringLit(path)}))"""
        val names = agg.map(_.shift(2)).mkString(".aggregate(\n", ",\n", "\n)").shift(2)
        Seq(header, s, names).mkString("\n")
    }
    aggDefs
  }

  protected def renderArtifact(a: Artifact): String = {
    val path = a.basePath + "/" + a.name.value

    val header = if (!a.isJvmOnly) {
      val platforms = a.platforms.map(p => platformName(p.platform).toUpperCase()).map(pn => s"${pn}Platform")
      s"""lazy val ${renderName(a.name)} = crossProject${platforms.mkString("(", ", ", ")")}.in(file(${stringLit(path)}))"""
    } else {
      s"""lazy val ${renderName(a.name)} = project.in(file(${stringLit(path)}))"""
    }

    val enabledPlatforms = a.platforms
      .filter(p => platformEnabled(p.platform))

    val platforms = if (!a.isJvmOnly) {
      enabledPlatforms
        .map {
          p =>
            val prefix = platformName(p.platform)
            val settings = Seq(
              "scalaVersion" := "crossScalaVersions.value.head".raw,
              "crossScalaVersions" := p.language.map(_.value)
            ) ++ p.settings
            renderSettings(settings, Some(prefix))
        }
    } else {
      Seq.empty
    }

    val platformProjects = if (!a.isJvmOnly) {
      enabledPlatforms.map {
        p =>
          val pname = platformName(p.platform)
          val out = Seq(
            Seq(s"""lazy val ${a.nameOn(p.platform)} = ${renderName(a.name)}.$pname"""),
            formatDeps(a, p.platform).map(_.shift(2)),
            formatLibDeps(a, p.platform)
          ).flatten
          out.mkString("\n")
      }
    } else {
      Seq.empty
    }

    val groupId = (Seq(config.settings.groupId) ++ a.subGroupId.toSeq).mkString(".")

    val more = if (a.isJvmOnly) {
      val p = a.platforms.filter(_.platform == Platform.Jvm).head
      Seq(
        "scalaVersion" := "crossScalaVersions.value.head".raw,
        "crossScalaVersions" := p.language.map(_.value)
      )
    } else {
      Seq.empty
    }

    val sharedSettings = Seq(
      "organization" := groupId,
    ) ++ more ++ a.settings

    val renderedSettings = renderSettings(sharedSettings)

    val out = Seq(
      Seq(header, renderedSettings),
      formatDeps(a, Platform.All),
      formatLibDeps(a, Platform.All),
      platforms,
      platformProjects
    ).flatten

    out.mkString("\n")
  }

  protected def renderSettings(settings: Seq[SettingDef], prefix: Option[String] = None): String = {
    val p = prefix match {
      case Some(value) =>
        s"${value}Settings"
      case None =>
        "settings"
    }
    settings.map(renderSetting).map(_.shift(2)).mkString(s".$p(\n", ",\n", "\n)").shift(2)
  }

  protected def renderSetting(settingDef: SettingDef): String = {
    val name = settingDef.name
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
    Seq(name, op, out).mkString(" ")
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
    val deps = a.libs

    val sharedArtDeps = deps.filter(_.scope.platform == targetPlatform)

    val artDeps = if (sharedArtDeps.nonEmpty) {
      val deps = sharedArtDeps
        .map {
          d =>
            val sep = d.dependency.kind match {
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
              case Scope.Compile =>
                Seq.empty
              case Scope.Test =>
                Seq("%", "Test")
            }

            (Seq(stringLit(d.dependency.group), sep, stringLit(d.dependency.artifact), "%", stringLit(d.dependency.version.value)) ++ suffix).mkString(" ").raw
        }
      val settings = Seq("libraryDependencies" ++= deps)

      Seq(renderSettings(settings))
    } else {
      Seq.empty
    }
    artDeps
  }

  protected def formatDeps(a: Artifact, p: Platform): Seq[String] = {
    val deps = a.depends
    val predicate = p match {
      case platform: Platform.BasePlatform =>
        (a: ScopedDependency) => a.scope.platform == platform
      case Platform.All =>
        (ad: ScopedDependency) => (ad.scope.platform == Platform.All && !index(ad.name).isJvmOnly) || (index(ad.name).isJvmOnly && ad.scope.platform == Platform.Jvm && a.isJvmOnly)
    }

    val sharedArtDeps = deps.filter(predicate)

    val artDeps = if (sharedArtDeps.nonEmpty) {
      Seq(sharedArtDeps
        .map {
          d =>
            val ad = index(d.name)
            p match {
              case Platform.All if a.isJvmOnly =>
                ad.nameOn(Platform.Jvm)
              case _ =>
                ad.nameOn(p)
            }
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
