package izumi.sbtgen.example

import izumi.sbtgen.model._

object TestProject {

  object Deps {

    object V {
      // foundation
      val scala_212 = "2.12.9"
      val scala_213 = "2.13.0"

      val collection_compat = "2.1.2"

      val kind_projector = "0.10.3"
      val scalatest = "3.0.8"

      val shapeless = "2.3.3"

      val cats = "2.0.0-RC1"
      val cats_effect = "2.0.0-RC1"
      val zio = "1.0.0-RC11-1"
      val zio_interop_cats = "2.0.0.0-RC2"

      val circe = "0.12.0-RC4"
      val circe_derivation = "0.12.0-M5"
      val jawn = "0.14.2"

      val http4s = "0.21.0-M4"

      val scalameta = "4.2.3"
      val fastparse = "2.1.3"

      val scala_xml = "1.2.0"

      // java-only dependencies below
      // java, we need it bcs http4s ws client isn't ready yet
      val asynchttpclient = "2.10.1"

      val classgraph = "4.8.47"
      val slf4j = "1.7.28"
      val typesafe_config = "1.3.4"

      // good to drop - java
      val cglib_nodep = "3.3.0"

      val scala_java_time = "2.0.0-RC3"
    }

    final val collection_compat = Library("org.scala-lang.modules", "scala-collection-compat", V.collection_compat)
    final val scalatest = Library("org.scalatest", "scalatest", V.scalatest) in Scope.Test.all


    final val cats_core = Library("org.typelevel", "cats-core", V.cats)
    final val cats_effect = Library("org.typelevel", "cats-effect", V.cats_effect)
    final val cats_all = Seq(
      cats_core
      , cats_effect
    )

    final val circe = Seq(
      Library("io.circe", "circe-core", V.circe),
      Library("io.circe", "circe-parser", V.circe),
      Library("io.circe", "circe-literal", V.circe),
      Library("io.circe", "circe-generic-extras", V.circe),
      Library("io.circe", "circe-derivation", V.circe_derivation),
    ).map(_ in Scope.Compile.all)

    final val zio_core = Library("dev.zio", "zio", V.zio)

    final val typesafe_config = Library("com.typesafe", "config", V.typesafe_config, LibraryType.Invariant) in Scope.Compile.all
    final val boopickle = Library("io.suzaku", "boopickle", "1.3.1") in Scope.Compile.all
    final val jawn = Library("org.typelevel", "jawn-parser", V.jawn, LibraryType.AutoJvm)

    final val scala_compiler = Library("org.scala-lang", "scala-compiler", Version.VExpr("scalaVersion.value"), LibraryType.Invariant)
    final val scala_library = Library("org.scala-lang", "scala-library", Version.VExpr("scalaVersion.value"), LibraryType.Invariant)
    final val scala_reflect = Library("org.scala-lang", "scala-reflect", Version.VExpr("scalaVersion.value"), LibraryType.Invariant)

    final val cglib_nodep = Library("cglib", "cglib-nodep", V.cglib_nodep, LibraryType.Invariant) in Scope.Compile.jvm


    final val projector = Library("org.typelevel", "kind-projector", "0.10.3", LibraryType.AutoJvm)
  }

  import Deps._

  final val scala212 = ScalaVersion("2.12.9")
  final val scala213 = ScalaVersion("2.13.0")

  object Groups {
    final val fundamentals = Set(Group("fundamentals"))
    final val jvmonly = Set(Group("jvmonly"))
  }

  object Targets {
    val targetScala = Seq(scala212, scala213)
    private val jvmPlatform = PlatformEnv(
      Platform.Jvm,
      targetScala,
      //plugins = Projects.root.plugins
    )
    private val jsPlatform = PlatformEnv(
      Platform.Js,
      targetScala,
      settings = Seq(
        "coverageEnabled" := false
      )
    )
    final val cross = Seq(jvmPlatform, jsPlatform)
    final val jvm = Seq(jvmPlatform)
  }

  object Projects {

    object root {
      final val id = ArtifactId("testproject")
      final val settings = Seq(
        "publishMavenStyle" in SettingScope.Build := true,
        "scalaVersion" in SettingScope.Build := "crossScalaVersions.value.head".raw,
        "crossScalaVersions" in SettingScope.Build := Targets.targetScala.map(_.value),
        "scalacOptions" in SettingScope.Build ++= Seq(
          "-language:higherKinds",
        ),
        "scalacOptions" in SettingScope.Build ++= Seq(
          SettingKey(Some(scala212), None) := Seq("-Ypartial-unification", "-Xsource:2.13", "-Yno-adapted-args"),
          SettingKey(Some(scala213), None) := Const.EmptySeq,
          SettingKey.Default := Const.EmptySeq
        ),
      )
      final val plugins = Plugins(
        Seq.empty,
        Seq.empty,
      )
    }

    object fundamentals {
      final val id = ArtifactId("fundamentals")
      final val basePath = "fundamentals"
    }

    object distage {
      final val id = ArtifactId("distage")
      final val basePath = "distage"
    }

  }


  final lazy val fundamentalsCollections = Artifact(
    ArtifactId("fundamentals-collections"),
    Projects.fundamentals.basePath,
    Seq.empty,
    Seq.empty,
    Targets.cross,
    Groups.fundamentals,
  )

  final lazy val fundamentalsPlatform = Artifact(
    ArtifactId("fundamentals-platform"),
    Projects.fundamentals.basePath,
    Seq.empty,
    Seq(
      fundamentalsCollections.name in Scope.Compile.all
    ),
    Targets.cross,
    Groups.fundamentals,
    settings = Seq(
      "npmDependencies" in(SettingScope.Compile, Platform.Js) ++= Seq("hash.js" -> "1.1.7")
    ),
    plugins = Plugins(Seq(Plugin("ScalaJSBundlerPlugin")), Seq.empty)
  )

  final lazy val fundamentalsFunctional = Artifact(
    ArtifactId("fundamentals-functional"),
    Projects.fundamentals.basePath,
    Seq.empty,
    Seq.empty,
    Targets.cross,
    Groups.fundamentals,
  )

  final lazy val fundamentalsBio = Artifact(
    ArtifactId("fundamentals-bio"),
    Projects.fundamentals.basePath,
    (cats_all ++ Seq(zio_core)).map(_ in Scope.Optional.all),
    Seq(fundamentalsFunctional.name in Scope.Runtime.all),
    Targets.cross,
    Groups.fundamentals,
  )

  final lazy val fundamentalsBasics = Seq(
    fundamentalsPlatform,
    fundamentalsFunctional,
    fundamentalsCollections,
  ).map(_.name in Scope.Runtime.all)

  final lazy val fundamentalsTypesafeConfig = Artifact(
    ArtifactId("fundamentals-typesafe-config"),
    Projects.fundamentals.basePath,
    Seq(typesafe_config, scala_reflect in Scope.Compile.jvm),
    fundamentalsBasics ++ Seq(fundamentalsReflection.name in Scope.Runtime.jvm),
    Targets.jvm,
    Groups.fundamentals,
  )

  final lazy val fundamentalsReflection = Artifact(
    ArtifactId("fundamentals-reflection"),
    Projects.fundamentals.basePath,
    Seq(boopickle, scala_reflect in Scope.Provided.all),
    fundamentalsBasics,
    Targets.cross,
    Groups.fundamentals,
  )

  final lazy val fundamentalsJsonCirce = Artifact(
    ArtifactId("fundamentals-json-circe"),
    Projects.fundamentals.basePath,
    circe ++ Seq(jawn in Scope.Compile.js),
    fundamentalsBasics,
    Targets.cross,
    Groups.fundamentals,
  )

  final lazy val fundamentals = Aggregate(
    Projects.fundamentals.id,
    Projects.fundamentals.basePath,
    Seq(
      fundamentalsCollections,
      fundamentalsPlatform,
      fundamentalsFunctional,
      fundamentalsBio,
      fundamentalsTypesafeConfig,
      fundamentalsReflection,
      fundamentalsJsonCirce,
    ),
  )

  final lazy val distageModel = Artifact(
    ArtifactId("distage-model"),
    Projects.distage.basePath,
    (cats_all).map(_ in Scope.Optional.all) ++ Seq(scala_reflect in Scope.Compile.all),
    fundamentalsBasics ++ Seq(fundamentalsBio, fundamentalsReflection).map(_.name in Scope.Compile.all),
    Targets.jvm,
    Groups.fundamentals,
  )

  final lazy val distageProxyCglib = Artifact(
    ArtifactId("distage-proxy-cglib"),
    Projects.distage.basePath,
    Seq(cglib_nodep),
    fundamentalsBasics ++ Seq(distageModel).map(_.name in Scope.Compile.all),
    Targets.jvm,
    Groups.fundamentals,
  )

  final lazy val distage = Aggregate(
    Projects.distage.id,
    Projects.distage.basePath,
    Seq(
      distageModel,
      distageProxyCglib,
    ),
  )

  val tgSdk: Project = Project(
    Projects.root.id,
    Seq(
      fundamentals,
      distage,
    ),
    Projects.root.settings,
    Seq(
      Import("sbt.Keys._")
    ),
    Seq(
      ScopedLibrary(projector, FullDependencyScope(Scope.Compile, Platform.All), compilerPlugin = true),
      collection_compat in Scope.Compile.all,
      scalatest,
    ),
    Projects.root.plugins,
  )
}
