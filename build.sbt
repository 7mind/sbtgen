import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

turbo in ThisBuild := true
crossScalaVersions in ThisBuild := Seq("2.13.0", "2.12.9")
scalaVersion in ThisBuild := crossScalaVersions.value.head

name := "izumi-sbtgen"
organization in ThisBuild := "io.7mind.izumi.sbtgen"
publishMavenStyle in ThisBuild := true
homepage in ThisBuild := Some(url("https://izumi.7mind.io"))
licenses in ThisBuild := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php"))
developers in ThisBuild := List(
  Developer(id = "7mind", name = "Septimal Mind", url = url("https://github.com/7mind"), email = "team@7mind.io"),
)
scmInfo in ThisBuild := Some(ScmInfo(url("https://github.com/7mind/sbtgen"), "scm:git:https://github.com/7mind/sbtgen.git"))

credentials in ThisBuild += Credentials(file(".secrets/credentials.sonatype-nexus.properties"))
sonatypeProfileName := "io.7mind"
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies, // : ReleaseStep
  inquireVersions, // : ReleaseStep
  runClean, // : ReleaseStep
  runTest, // : ReleaseStep
  setReleaseVersion, // : ReleaseStep
  commitReleaseVersion, // : ReleaseStep, performs the initial git checks
  tagRelease, // : ReleaseStep
  //publishArtifacts,                       // : ReleaseStep, checks whether `publishTo` is properly set up
  setNextVersion, // : ReleaseStep
  commitNextVersion, // : ReleaseStep
  pushChanges // : ReleaseStep, also checks that an upstream branch is properly configured
)

// publishTo in ThisBuild := sonatypePublishTo.value
publishTo in ThisBuild := (if (!isSnapshot.value) {
  Some(Resolver.file("local-publish", new File("target/local-repo")))
} else {
  Some(Opts.resolver.sonatypeSnapshots)
})

lazy val core = (project in file("core"))
  .settings(
    libraryDependencies += "com.github.scopt" %% "scopt" % "4.0.0-RC2",
    libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
  )

lazy val sbtgen = (project in file("sbtgen"))
  .dependsOn(core)
  .settings(
    libraryDependencies += "com.lihaoyi" % "ammonite" % "1.6.9-19-827dffe" cross CrossVersion.full
  )

lazy val example = (project in file("example"))
  .dependsOn(core)
  .settings(
    mainClass := Some("izumi.sbtgen.Main"),
    skip in publish := true,
  )

lazy val `sbtgen-root` = (project in file("."))
  .aggregate(core, example, sbtgen)
  .settings(
    skip in publish := true,
  )
