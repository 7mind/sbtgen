import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}



lazy val `test` = crossProject(JVMPlatform, JSPlatform).crossType(CrossType.Pure).in(file("test"))
  .settings(
    organization := "io.7mind"
  )
  .jvmSettings(
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
    }
  )
  .jsSettings(
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
    }
  )
lazy val `testJVM` = `test`.jvm
lazy val `testJS` = `test`.js

lazy val `test-agg` = (project in file(".agg/test-agg"))
  .settings(
    publish / skip := true
  )
  .aggregate(
    `testJVM`,
    `testJS`
  )

lazy val `test-agg-jvm` = (project in file(".agg/test-agg-jvm"))
  .settings(
    publish / skip := true
  )
  .aggregate(
    `testJVM`
  )

lazy val `test-agg-js` = (project in file(".agg/test-agg-js"))
  .settings(
    publish / skip := true
  )
  .aggregate(
    `testJS`
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
