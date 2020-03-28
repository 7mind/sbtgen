import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}



lazy val `test` = project.in(file("test"))
  .settings(
    organization := "io.7mind",
    unmanagedSourceDirectories in Compile += { (isSnapshot.value, scalaVersion.value) match {
      case (_, "0.23.0-RC1") => baseDirectory.value / "src/main/scala-3" 
      case (_, _) => baseDirectory.value / "src/main/scala-2" 
    } },
    unmanagedSourceDirectories in Test += { (isSnapshot.value, scalaVersion.value) match {
      case (_, "0.23.0-RC1") => baseDirectory.value / "src/test/scala-3" 
      case (_, _) => baseDirectory.value / "src/test/scala-2" 
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "0.23.0-RC1"
    )
  )

lazy val `test-agg` = (project in file(".agg/test-agg"))
  .settings(
    skip in publish := true
  )
  .aggregate(
    `test`
  )

lazy val `test-agg-jvm` = (project in file(".agg/test-agg-jvm"))
  .settings(
    skip in publish := true
  )
  .aggregate(
    `test`
  )

lazy val `test-agg-js` = (project in file(".agg/test-agg-js"))
  .settings(
    skip in publish := true
  )
  .aggregate(
    `test`
  )

lazy val `test-dotty-jvm` = (project in file(".agg/.agg-jvm"))
  .settings(
    skip in publish := true
  )
  .aggregate(
    `test-agg-jvm`
  )

lazy val `test-dotty-js` = (project in file(".agg/.agg-js"))
  .settings(
    skip in publish := true
  )
  .aggregate(
    `test-agg-js`
  )

lazy val `test-dotty` = (project in file("."))
  .settings(
    skip in publish := true
  )
  .aggregate(
    `test-agg`
  )
