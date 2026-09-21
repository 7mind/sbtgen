package izumi.sbtgen

import izumi.sbtgen.model._

/**
  * A small JVM-only project used to check that the output generated for
  * [[SbtTarget.Sbt2]] actually loads under sbt 2.x.
  *
  * It deliberately avoids `appendPlugins`: almost no third-party sbt plugin the
  * other test projects use has an sbt 2.x release yet, so the generated
  * `project/plugins.sbt` has to stay empty for the build to be loadable.
  */
object TestSbt2Project {

  private val scala213 = ScalaVersion("2.13.18")
  private val scala3 = ScalaVersion("3.3.7")

  private val jvm = PlatformEnv(
    platform = Platform.Jvm,
    language = Seq(scala3, scala213),
  )

  private val core = Artifact(
    name = ArtifactId("sbt2-core"),
    libs = Nil,
    depends = Nil,
    platforms = Seq(jvm),
  )

  private val api = Artifact(
    name = ArtifactId("sbt2-api"),
    libs = Nil,
    depends = Seq(ArtifactId("sbt2-core") in Scope.Compile.all),
    platforms = Seq(jvm),
  )

  val project: Project = Project(
    name = ArtifactId("test-sbt2"),
    aggregates = Seq(
      Aggregate(
        name = ArtifactId("sbt2-agg"),
        artifacts = Seq(core, api),
        pathPrefix = Seq("lib"),
      )
    ),
    // exercises the sbt 2.x rule that bare statements become common settings:
    // both of these must end up attached to the root project instead
    topLevelSettings = Seq(
      "publishMavenStyle" in SettingScope.Build := true
    ),
    rootPlugins = Plugins(
      Seq.empty,
      Seq(Plugin("sbt.plugins.JUnitXmlReportPlugin", Platform.All)),
    ),
    rootSettings = Seq(
      "crossScalaVersions" := "Nil".raw,
      "scalaVersion" := scala3.value,
      "organization" in SettingScope.Build := "io.7mind",
    ),
    sharedSettings = Seq(
      "testOptions" in SettingScope.Test += """Tests.Argument("-oDF")""".raw
    ),
  )
}
