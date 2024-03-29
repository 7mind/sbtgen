package izumi.sbtgen

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import izumi.sbtgen.model.{GenConfig, GlobalSettings, Project}
import scopt.OptionParser

case class Config(
  withJvm: Boolean = true,
  withSjs: Boolean = false,
  withSnat: Boolean = false,
  debug: Boolean = false,
  mergeTestScopes: Boolean = true,
  output: String = "test-out",
  groups: Set[String] = Set.empty,
  publishTests: Boolean = true,
  compactify: Boolean = false,
)

object Entrypoint {
  def main(
    project: Project,
    settings: GlobalSettings,
    args: Seq[String],
    renderer: (GenConfig, Project) => Renderer = new Renderer(_, _),
  ): Unit = {

    val parser1 = new OptionParser[Config]("sbtgen") {
      head("sbtgen")
      opt[Unit]("nojvm")
        .action((_, c) => c.copy(withJvm = false))
        .text("disable jvm projects")
      opt[Unit]("js")
        .action((_, c) => c.copy(withSjs = true))
        .text("enable js projects")
      opt[Unit]("native")
        .action((_, c) => c.copy(withSnat = true))
        .text("enable native projects")
      opt[Unit]("nta")
        .action((_, c) => c.copy(publishTests = false))
        .text("don't publish test artifacts")
      opt[Unit]('d', "debug")
        .action((_, c) => c.copy(debug = true))
        .text("enable debug output")
      opt[Unit]('c', "compactify")
        .action((_, c) => c.copy(compactify = true))
        .text("deduplicate repetative settings")
      opt[Unit]('t', "isolate-tests")
        .action((_, c) => c.copy(mergeTestScopes = false))
        .text("don't inherit test scopes")
      opt[String]('o', "output")
        .action((x, c) => c.copy(output = x))
        .text("output directory")
      opt[String]('u', "use")
        .unbounded()
        .action((x, c) => c.copy(groups = c.groups + x))
        .text("use only groups specified")
    }

    parser1.parse(args, Config()) match {
      case Some(config) =>
        val cfg = GenConfig(
          config.withJvm,
          config.withSjs,
          config.withSnat,
          config.debug,
          config.mergeTestScopes,
          settings,
          config.output,
          config.groups,
          config.publishTests,
          config.compactify,
        )

        try {
          run(cfg, project, renderer(cfg, project))
        } catch {
          case e: Throwable =>
            e.printStackTrace()
            System.exit(1)
        }
      case _ =>
        System.err.println("Cannot parse commandline")
        System.exit(1)

    }
  }

  final def run(config: GenConfig, project: Project, renderer: Renderer): Unit = {
    val artifacts = renderer.render()
    val buildSbtStr = Seq(doNotEditHeader) ++ (if (!config.jvmOnly) {
                                                 Seq(
                                                   "import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}"
                                                 ) ++ artifacts
                                               } else {
                                                 artifacts
                                               })

    val files = Map(
      "build.sbt" -> buildSbtStr.mkString("", "\n\n", "\n")
    ) ++ config
      .settings.sbtVersion.fold(Map.empty[String, String])(
        sbtVersion =>
          Map(
            "project/build.properties" -> s"sbt.version = $sbtVersion"
          )
      )

    val pluginsSbtStr = makePluginsSbt(config, project, renderer)

    val target = Paths.get(config.output)

    (files + ("project/plugins.sbt" -> pluginsSbtStr)).foreach {
      case (n, c) =>
        val targetFile = target.resolve(n)
        targetFile.getParent.toFile.mkdirs()
        Files.write(targetFile, c.getBytes(StandardCharsets.UTF_8))
        if (config.debug) {
          println(
            s"""$n:
               |
               |$c
               |""".stripMargin
          )
        }
    }
  }

  private def makePluginsSbt(config: GenConfig, project: Project, renderer: Renderer): String = {
    val b = new StringBuilder()

    b.append(doNotEditHeader)

    if (config.js) {
      b.append(
        s"""// https://www.scala-js.org/
           |addSbtPlugin("org.scala-js" % "sbt-scalajs" % ${renderer renderVersion config.settings.scalaJsVersion})
           |
           |// https://github.com/portable-scala/sbt-crossproject
           |addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % ${renderer renderVersion config.settings.crossProjectVersion})
           |""".stripMargin
      )

      config.settings.bundlerVersion.foreach {
        bv =>
          b.append(
            s"""|
                |// https://scalacenter.github.io/scalajs-bundler/
                |addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % ${renderer renderVersion bv})
                |""".stripMargin
          )
      }

      config.settings.sbtJsDependenciesVersion.foreach {
        bv =>
          b.append(
            s"""|
                |// https://github.com/scala-js/jsdependencies
                |addSbtPlugin("org.scala-js" % "sbt-jsdependencies" % ${renderer renderVersion bv})
                |""".stripMargin
          )
      }
    }

    if (config.native) {
      b.append(s"""addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % ${renderer renderVersion config.settings.crossProjectVersion})
                  |
                  |addSbtPlugin("org.scala-native"   % "sbt-scala-native"              % ${renderer renderVersion config.settings.scalaNativeVersion})
                  |""".stripMargin)
    }

    b.append('\n')
    b.append("/" * 80)
    b.append('\n')
    b.append('\n')

    project.appendPlugins.foreach {
      p =>
        b.append(s"""addSbtPlugin(${renderer.stringLit(p.group)} % ${renderer.stringLit(p.artifact)} % ${renderer.renderVersion(p.version)})""")
        b.append('\n')
        b.append('\n')
    }

    b.append(scoverageScalaXmlWorkaround)

    b.mkString
  }

  private def doNotEditHeader: String =
    """// DO NOT EDIT THIS FILE
      |// IT IS AUTOGENERATED BY `sbtgen.sc` SCRIPT
      |// ALL CHANGES WILL BE LOST
      |""".stripMargin

  private def scoverageScalaXmlWorkaround: String =
    """// Ignore scala-xml version conflict between scoverage where `coursier` requires scala-xml v2
      |// and scoverage requires scala-xml v1 on Scala 2.12,
      |// introduced when updating scoverage from 1.9.3 to 2.0.5
      |libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
      |""".stripMargin

  // scopt-4.0
  //    val builder = OParser.builder[Config]
  //    val parser1 = {
  //      import builder._
  //      OParser.sequence(
  //        programName("sbtgen"),
  //        head("sbtgen"),
  //
  //        opt[Unit]("nojvm")
  //          .action((_, c) => c.copy(withJvm = false))
  //          .text("disable jvm projects"),
  //        opt[Unit]("js")
  //          .action((_, c) => c.copy(withSjs = true))
  //          .text("enable js projects"),
  //        opt[Unit]("native")
  //          .action((_, c) => c.copy(withSnat = true))
  //          .text("enable native projects"),
  //        opt[Unit]("nta")
  //          .action((_, c) => c.copy(publishTests = false))
  //          .text("don't publish test artifacts"),
  //        opt[Unit]('d', "debug")
  //          .action((_, c) => c.copy(withSnat = true))
  //          .text("enable debug output"),
  //        opt[Unit]('t', "isolate-tests")
  //          .action((_, c) => c.copy(mergeTestScopes = false))
  //          .text("enable debug output"),
  //        opt[String]('o', "output")
  //          .action((x, c) => c.copy(output = x))
  //          .text("output directory"),
  //        opt[String]('u', "use")
  //          .action((x, c) => c.copy(groups = c.groups + Group(x)))
  //          .text("use only groups specified"),
  //      )
  //    }
}
