package izumi.sbtgen

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import izumi.sbtgen.model.{GenConfig, Group, Project, GlobalSettings}
import scopt.OParser

case class Config(
                   withJvm: Boolean = true,
                   withSjs: Boolean = false,
                   withSnat: Boolean = false,
                   debug: Boolean = true,
                   mergeTestScopes: Boolean = true,
                   output: String = "test-out",
                   groups: Set[Group] = Set.empty,
                 )



object Entrypoint {
  def main(project: Project, settings: GlobalSettings, args: Seq[String]): Unit = {
    val builder = OParser.builder[Config]
    val parser1 = {
      import builder._
      OParser.sequence(
        programName("sbtgen"),
        head("sbtgen"),

        opt[Unit]("nojvm")
          .action((_, c) => c.copy(withJvm = false))
          .text("disable jvm projects"),
        opt[Unit]("js")
          .action((_, c) => c.copy(withSjs = true))
          .text("enable js projects"),
        opt[Unit]("native")
          .action((_, c) => c.copy(withSnat = true))
          .text("enable native projects"),
        opt[Unit]('d', "debug")
          .action((_, c) => c.copy(withSnat = true))
          .text("enable debug output"),
        opt[Unit]('t', "isolate-tests")
          .action((_, c) => c.copy(mergeTestScopes = false))
          .text("enable debug output"),
        opt[String]('o', "output")
          .action((x, c) => c.copy(output = x))
          .text("output directory"),
        opt[String]('u', "use")
          .action((x, c) => c.copy(groups = c.groups + Group(x)))
          .text("use only groups specified"),
      )
    }

    OParser.parse(parser1, args, Config()) match {
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
        )

        try {
          run(cfg, project)
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

  final def run(config: GenConfig, project: Project): Unit = {
    val renderer = makeRenderer(config, project)
    val artifacts = renderer.render()
    val main = if (!config.jvmOnly) {
      Seq("import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}") ++ artifacts
    } else {
      artifacts
    }

    val files = Map(
      "build.sbt" -> main.mkString("\n\n"),
      "project/build.properties" -> s"sbt.version = ${config.settings.sbtVersion}"
    )

    val moreFiles = makeMoreBoilerplate(config)

    val target = Paths.get(config.output)

    (files ++ moreFiles).foreach {
      case (n, c) =>
        val targetFile = target.resolve(n)
        targetFile.getParent.toFile.mkdirs()
        Files.write(targetFile, c.getBytes(StandardCharsets.UTF_8))
        println(
          s"""$n:
             |
             |$c
             |""".stripMargin)

    }
  }

  protected def makeMoreBoilerplate(config: GenConfig): Map[String, String] = {
    if (config.jvmOnly) {
      Map.empty
    } else {
      val b = new StringBuilder()

      if (config.js) {
        b.append(
          s"""// https://www.scala-js.org/
             |addSbtPlugin("org.scala-js" % "sbt-scalajs" % "${config.settings.scalaJsVersion}")
             |
             |// https://github.com/portable-scala/sbt-crossproject
             |addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "${config.settings.crossProjectVersion}")
             |
             |// https://scalacenter.github.io/scalajs-bundler/
             |addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "${config.settings.bundlerVersion}")
             |""".stripMargin)
      }

      if (config.native) {
        b.append(
          s"""addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "${config.settings.crossProjectVersion}")
             |
             |addSbtPlugin("org.scala-native"   % "sbt-scala-native"              % "${config.settings.scalaNativeVersion}")
             |""".stripMargin)
      }

      Map("project/plugins.sbt" -> b.mkString)
    }
  }

  protected def makeRenderer(config: GenConfig, project: Project) = {
    new Renderer(config, project)
  }
}
