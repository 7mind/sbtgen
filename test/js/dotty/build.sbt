import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}



lazy val `test` = project.in(file("test"))
  .settings(
    crossScalaVersions := Seq(
      "0.23.0-RC1"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind"
  )

lazy val `test-agg` = (project in file(".agg/test-agg"))
  .settings(
    publish / skip := true
  )
  .aggregate(
    `test`
  )

lazy val `test-agg-jvm` = (project in file(".agg/test-agg-jvm"))
  .settings(
    publish / skip := true
  )
  .aggregate(
    `test`
  )

lazy val `test-agg-js` = (project in file(".agg/test-agg-js"))
  .settings(
    publish / skip := true
  )
  .aggregate(
    `test`
  )

lazy val `test-dotty-jvm` = (project in file(".agg/.agg-jvm"))
  .settings(
    publish / skip := true
  )
  .aggregate(
    `test-agg-jvm`
  )

lazy val `test-dotty-js` = (project in file(".agg/.agg-js"))
  .settings(
    publish / skip := true
  )
  .aggregate(
    `test-agg-js`
  )

lazy val `test-dotty` = (project in file("."))
  .settings(
    publish / skip := true
  )
  .aggregate(
    `test-agg`
  )
