//package izumi.sbtgen.example
//
//import izumi.sbtgen.model._
//
//object TestProject1 {
//
//  final val catsVersion = Version("2.0.0-RC2")
//  final val catsCore = Library("org.typelevel", "cats-core", catsVersion, LibraryType.Auto)
//  final val catsXXX = Library("org.typelevel", "cats-core_2.12", catsVersion, LibraryType.Invariant)
//  final val projector = Library("org.typelevel", "kind-projector", Version("0.10.3"), LibraryType.Auto)
//
//  final val scala212 = ScalaVersion("2.12.9")
//  final val scala213 = ScalaVersion("2.13.0")
//
//  object Groups {
//    final val fundamentals = Set(Group("fundamentals"))
//    final val jvmonly = Set(Group("jvmonly"))
//  }
//
//  object Targets {
//    private val targetScala = Seq(scala212, scala213)
//    final val cross = Seq(PlatformEnv(Platform.Jvm, targetScala, plugins = Projects.root.plugins), PlatformEnv(Platform.Js, targetScala))
//    final val crossJvm = Seq(PlatformEnv(Platform.Jvm, targetScala))
//  }
//
//  object Projects {
//
//    object root {
//      final val id = ArtifactId("testproject")
//      final val settings = Seq(
//        "scalacOptions" ++= Seq("-Ypartial-unification"),
//        "scalacOptions" ++= Const.EmptySeq,
//        "scalacOptions" ++= Seq(
//          SettingKey(Some(scala212), None) := Seq("-Ypartial-unification"),
//          SettingKey.Default := Const.EmptySeq
//        )
//      )
//      final val plugins = Plugins(
//        Seq(
//          Plugin("TestEnabledPlugin")
//        ),
//        Seq(
//          Plugin("TestDisabledPlugin")
//        )
//      )
//    }
//
//    object fundamentals {
//      final val id = ArtifactId("fundamentals")
//      final val basePath = "fundamentals"
//    }
//
//  }
//
//
//  lazy val fundamentalsCollections: Artifact = Artifact(
//    ArtifactId("fundamentals-collections"),
//    Projects.fundamentals.basePath,
//    Seq(
//      catsCore in Scope.Runtime.all,
//      catsCore in Scope.Runtime.jvm,
//      catsCore in Scope.Runtime.js,
//      catsXXX in Scope.Runtime.jvm,
//    ),
//    Seq.empty,
//    Targets.cross,
//    Groups.fundamentals,
//    settings = Projects.root.settings,
//  )
//
//  lazy val fundamentalsPlatform: Artifact = Artifact(
//    ArtifactId("fundamentals-platform"),
//    Projects.fundamentals.basePath,
//    Seq.empty,
//    Seq(
//      fundamentalsCollections.name in Scope.Compile.jvm
//    ),
//    Targets.cross,
//    Groups.fundamentals,
//  )
//
//  lazy val fundamentalsFunctional: Artifact = Artifact(
//    ArtifactId("fundamentals-functional"),
//    Projects.fundamentals.basePath,
//    Seq.empty,
//    Seq(
//      fundamentalsCollections.name in Scope.Runtime.all,
//      fundamentalsFunctionalJvmOnly.name in Scope.Compile.jvm,
//      fundamentalsFunctionalJvmOnly.name in Scope.Runtime.all,
//    ),
//    Targets.cross,
//    Groups.fundamentals,
//  )
//
//  lazy val fundamentalsFunctionalJvmOnly: Artifact = Artifact(
//    ArtifactId("fundamentals-functional-jvmonly"),
//    Projects.fundamentals.basePath,
//    Seq.empty,
//    Seq(
//      fundamentalsCollections.name in Scope.Runtime.all,
//    ),
//    Targets.crossJvm,
//    Groups.jvmonly,
//    plugins = Projects.root.plugins,
//  )
//
//  val tgSdk: Project = Project(
//    Projects.root.id,
//    Seq(
//      Aggregate(
//        Projects.fundamentals.id,
//        Projects.fundamentals.basePath,
//        Seq(
//          fundamentalsCollections,
//          fundamentalsPlatform,
//          fundamentalsFunctional,
//          fundamentalsFunctionalJvmOnly
//        ),
//      )
//    ),
//    Seq(
//      "publishMavenStyle" in SettingScope.Build := true,
//    ),
//    Seq(
//      Import("sbt.Keys._")
//    ),
//    Seq(
//      ScopedLibrary(projector, FullDependencyScope(Scope.Compile, Platform.All), compilerPlugin = true),
//    ),
//    Projects.root.plugins,
//  )
//}
