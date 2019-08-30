turbo in ThisBuild := true
crossScalaVersions in ThisBuild := Seq("2.13.0", "2.12.9")
scalaVersion in ThisBuild := crossScalaVersions.value.head

lazy val core = (project in file("core"))
  .settings(
    libraryDependencies += "com.github.scopt" %% "scopt" % "4.0.0-RC2",
    libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
  )

lazy val example = (project in file("example"))
  .dependsOn(core)
  .settings(
    mainClass := Some("izumi.sbtgen.Main")
  )

lazy val sbtgen = (project in file("sbtgen"))
  .dependsOn(core)
  .settings(
    libraryDependencies += "com.lihaoyi" % "ammonite" % "1.6.9-19-827dffe" cross CrossVersion.full
  )

lazy val `sbtgen-root` = (project in file("."))
  .aggregate(core, example, sbtgen)
  .settings(
    publish := {},
    publishLocal := {}
  )
