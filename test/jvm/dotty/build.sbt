

lazy val `test` = project.in(file("test"))
  .settings(
    crossScalaVersions := Seq(
      "0.23.0-RC1"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    unmanagedSourceDirectories in Compile += baseDirectory.value / ".jvm/src/main/scala" ,
    unmanagedResourceDirectories in Compile += baseDirectory.value / ".jvm/src/main/resources" ,
    unmanagedSourceDirectories in Test += baseDirectory.value / ".jvm/src/test/scala" ,
    unmanagedResourceDirectories in Test += baseDirectory.value / ".jvm/src/test/resources" 
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

lazy val `test-dotty-jvm` = (project in file(".agg/.agg-jvm"))
  .settings(
    skip in publish := true
  )
  .aggregate(
    `test-agg-jvm`
  )

lazy val `test-dotty` = (project in file("."))
  .settings(
    skip in publish := true
  )
  .aggregate(
    `test-agg`
  )
