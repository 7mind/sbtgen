//package izumi.sbtgen.example
//
//import java.nio.charset.StandardCharsets
//import java.nio.file.{Files, Paths}
//
//import izumi.sbtgen.{GenConfig, Renderer}
//
//object Main {
//  def main(args: Array[String]): Unit = {
//    val config = GenConfig(jvm = true, js = true, native = false, "io.7mind")
//    val renderer = new Renderer(config, TestProject.tgSdk)
//    val artifacts = renderer.render()
//
//
//    val main =if (!config.jvmOnly) {
//      Seq("import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}") ++ artifacts
//    } else {
//      artifacts
//    }
//
//    val files = Map(
//      "build.sbt" -> main.mkString("\n\n"),
//      "project/build.properties" -> s"sbt.version = ${config.sbtVersion}"
//    )
//
//    val moreFiles = if (config.jvmOnly) {
//      Map.empty
//    } else {
//      val b = new StringBuilder()
//
//      if (config.js) {
//        b.append(
//          s"""// https://www.scala-js.org/
//             |addSbtPlugin("org.scala-js" % "sbt-scalajs" % "${config.scalaJsVersion}")
//             |
//             |// https://github.com/portable-scala/sbt-crossproject
//             |addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "${config.crossProjectVersion}")
//             |
//             |// https://scalacenter.github.io/scalajs-bundler/
//             |addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "${config.bundlerVersion}")
//             |""".stripMargin)
//      }
//
//      if (config.native) {
//        b.append(
//          s"""addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "${config.crossProjectVersion}")
//             |
//             |addSbtPlugin("org.scala-native"   % "sbt-scala-native"              % "${config.scalaNativeVersion}")
//             |""".stripMargin)
//      }
//
//      Map("project/plugins.sbt" -> b.mkString)
//    }
//
//    val target = Paths.get(config.output)
//
//    (files ++ moreFiles).foreach {
//      case (n, c) =>
//        val targetFile = target.resolve(n)
//        targetFile.getParent.toFile.mkdirs()
//        Files.write(targetFile, c.getBytes(StandardCharsets.UTF_8))
//        println(
//          s"""$n:
//             |
//             |$c
//             |""".stripMargin)
//
//    }
//
//
//  }
//}
