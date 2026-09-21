package izumi.sbtgen

import izumi.sbtgen.model._

/**
  * A small cross-platform project used to check that the output generated for
  * [[SbtTarget.Sbt2]] actually loads under sbt 2.x.
  *
  * It deliberately avoids `appendPlugins`: almost no third-party sbt plugin the
  * other test projects use has an sbt 2.x release yet, so the generated
  * `project/plugins.sbt` has to stay loadable.
  *
  * It covers, in one build, the three shapes that differ under sbt 2.x:
  *   - a cross-platform artifact with a [[LibraryType.Auto]] dependency (`%%%` is gone)
  *   - the same artifact with a [[LibraryType.AutoJvm]] dependency (`%%` is platform-aware,
  *     so a JVM-only artifact has to opt out via `.platform(Platform.jvm)`)
  *   - an `sbtPlugin := true` artifact (its Scala version must be sbt's, not the model's)
  */
object TestSbt2Project {

  private val scala3 = ScalaVersion("3.3.7")

  private val jvm = PlatformEnv(Platform.Jvm, Seq(scala3))
  private val js = PlatformEnv(Platform.Js, Seq(scala3))

  private val collectionCompat = Library("org.scala-lang.modules", "scala-collection-compat", "2.14.0", LibraryType.Auto)
  private val compilerJvmOnly = Library("org.scala-lang", "scala3-compiler", scala3.value, LibraryType.AutoJvm)

  private val core = Artifact(
    name = ArtifactId("sbt2-core"),
    libs = Seq(
      collectionCompat in Scope.Compile.all,
      compilerJvmOnly in Scope.Compile.all,
    ),
    depends = Nil,
    platforms = Seq(jvm, js),
  )

  private val api = Artifact(
    name = ArtifactId("sbt2-api"),
    libs = Nil,
    depends = Seq(ArtifactId("sbt2-core") in Scope.Compile.all),
    platforms = Seq(jvm, js),
  )

  private val sbtPlugin = Artifact(
    name = ArtifactId("sbt2-plugin"),
    libs = Nil,
    depends = Nil,
    platforms = Seq(jvm),
    settings = Seq(
      "sbtPlugin" := true,
      "sbtPluginPublishLegacyMavenStyle" := false,
    ),
  )

  val project: Project = Project(
    name = ArtifactId("test-sbt2"),
    aggregates = Seq(
      Aggregate(
        name = ArtifactId("sbt2-agg"),
        artifacts = Seq(core, api),
        pathPrefix = Seq("lib"),
      ),
      Aggregate(
        name = ArtifactId("sbt2-plugins"),
        artifacts = Seq(sbtPlugin),
        pathPrefix = Seq("sbt-plugins"),
        defaultPlatforms = Seq(jvm),
        dontIncludeInSuperAgg = true,
        enableProjectSharedAggSettings = false,
      ),
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
