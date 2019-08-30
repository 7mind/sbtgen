package izumi.sbtgen.example

import izumi.sbtgen.model._

object TestProject {

  final val catsVersion = Version("2.0.0-RC2")
  final val catsCore = Library("org.typelevel", "cats-core", catsVersion, LibraryType.Auto)
  final val catsXXX = Library("org.typelevel", "cats-core_2.12", catsVersion, LibraryType.Invariant)

  final val scala212 = ScalaVersion("2.12.9")
  final val scala213 = ScalaVersion("2.13.0")

  object Groups {
    final val fundamentals = Set(Group("fundamentals"))
    final val jvmonly = Set(Group("jvmonly"))
  }

  object Targets {
    private val targetScala = Vector(scala212, scala213)
    final val cross = Vector(PlatformEnv(Platform.Jvm, targetScala), PlatformEnv(Platform.Js, targetScala))
    final val crossJvm = Vector(PlatformEnv(Platform.Jvm, targetScala))
  }

  object Projects {
    object root {
      final val id = ArtifactId("testproject")
      final val settings  = Seq(
        "scalacOptions" ++= Seq("-Ypartial-unification"),
        "scalacOptions" ++= Const.EmptySeq,
        "scalacOptions" ++= Seq(
          SettingKey(Some(scala212), None) := Seq("-Ypartial-unification"),
          SettingKey.Default := Const.EmptySeq
        )
      )
    }

    object fundamentals {
      final val id = ArtifactId("fundamentals")
      final val basePath = "fundamentals"
    }
  }


  lazy val fundamentalsCollections: Artifact = Artifact(
    ArtifactId("fundamentals-collections"),
    Projects.fundamentals.basePath,
    Vector(
      catsCore in Scope.Runtime.all,
      catsCore in Scope.Runtime.jvm,
      catsCore in Scope.Runtime.js,
      catsXXX in Scope.Runtime.jvm,
    ),
    Vector.empty,
    Targets.cross,
    Groups.fundamentals,
    settings = Projects.root.settings,
  )

  lazy val fundamentalsPlatform: Artifact = Artifact(
    ArtifactId("fundamentals-platform"),
    Projects.fundamentals.basePath,
    Vector.empty,
    Vector(
      fundamentalsCollections.name in Scope.Compile.jvm
    ),
    Targets.cross,
    Groups.fundamentals,
  )

  lazy val fundamentalsFunctional: Artifact = Artifact(
    ArtifactId("fundamentals-functional"),
    Projects.fundamentals.basePath,
    Vector.empty,
    Vector(
      fundamentalsCollections.name in Scope.Runtime.all,
      fundamentalsFunctionalJvmOnly.name in Scope.Compile.jvm,
      fundamentalsFunctionalJvmOnly.name in Scope.Runtime.all,
    ),
    Targets.cross,
    Groups.fundamentals,
  )

  lazy val fundamentalsFunctionalJvmOnly: Artifact = Artifact(
    ArtifactId("fundamentals-functional-jvmonly"),
    Projects.fundamentals.basePath,
    Vector.empty,
    Vector(
      fundamentalsCollections.name in Scope.Runtime.all,
    ),
    Targets.crossJvm,
    Groups.jvmonly,
  )

  val tgSdk: Project = Project(
    Projects.root.id,
    Vector(
      Aggregate(
        Projects.fundamentals.id,
        Projects.fundamentals.basePath,
        Vector(
          fundamentalsCollections,
          fundamentalsPlatform,
          fundamentalsFunctional,
          fundamentalsFunctionalJvmOnly
        ),
      )
    )
  )
}
