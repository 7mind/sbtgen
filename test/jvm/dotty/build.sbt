

lazy val `test` = project.in(file("test"))
  .settings(
    crossScalaVersions := Seq(
      "0.23.0-RC1"
    ),
    scalaVersion := crossScalaVersions.value.head,
    Compile / unmanagedSourceDirectories ++= {
      val version = scalaVersion.value
      val crossVersions = crossScalaVersions.value
      import Ordering.Implicits._
      val olderVersions = crossVersions.map(CrossVersion.partialVersion).filter(_ <= CrossVersion.partialVersion(version)).flatten
      (Compile / unmanagedSourceDirectories).value.flatMap {
        case dir if dir.getPath.endsWith("scala") => olderVersions.map { case (m, n) => file(dir.getPath + s"-$m.$n+") }
        case _ => Seq.empty
      }
    },
    Test / unmanagedSourceDirectories ++= {
      val version = scalaVersion.value
      val crossVersions = crossScalaVersions.value
      import Ordering.Implicits._
      val olderVersions = crossVersions.map(CrossVersion.partialVersion).filter(_ <= CrossVersion.partialVersion(version)).flatten
      (Test / unmanagedSourceDirectories).value.flatMap {
        case dir if dir.getPath.endsWith("scala") => olderVersions.map { case (m, n) => file(dir.getPath + s"-$m.$n+") }
        case _ => Seq.empty
      }
    },
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
