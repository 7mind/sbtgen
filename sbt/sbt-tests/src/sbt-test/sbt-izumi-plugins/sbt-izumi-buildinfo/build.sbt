lazy val checkBuildInfo = taskKey[Unit]("Check generated build-info version constants")

lazy val root = (project in file("."))
  .settings(
    name := "sbt-izumi-buildinfo",
    crossScalaVersions := Seq("3.3.7"),
    scalaVersion := "3.3.7",
  )
  .aggregate(lib, bom)

lazy val lib = (project in file("lib"))
  .settings(
    crossScalaVersions := Seq("3.3.7"),
    scalaVersion := "3.3.7",
  )

lazy val bom = (project in file("bom"))
  .settings(
    withBuildInfo("izumi.sbt.deps", "Izumi")
  )
  .settings(
    checkBuildInfo := {
      val generated = (Compile / sourceManaged).value / "izumi" / "sbt" / "deps" / "Izumi.scala"
      val content = IO.read(generated)
      assert(content.contains("""final val scalaVersion = "3.3.7""""), content)
    }
  )
