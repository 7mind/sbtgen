

lazy val `test` = project.in(file("test"))
  .settings(
    crossScalaVersions := Seq(
      "0.23.0-RC1"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" 
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

lazy val `test-dotty-jvm` = (project in file(".agg/.agg-jvm"))
  .settings(
    publish / skip := true
  )
  .aggregate(
    `test-agg-jvm`
  )

lazy val `test-dotty` = (project in file("."))
  .settings(
    publish / skip := true
  )
  .aggregate(
    `test-agg`
  )
