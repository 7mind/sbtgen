package izumi.sbtgen.example

import izumi.sbtgen.model._

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

object TestProject {

  object Deps {
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

    final val fast_classpath_scanner = Library("io.github.classgraph", "classgraph", V.classgraph, LibraryType.Invariant) in Scope.Compile.jvm
    final val scala_java_time = Library("io.github.cquiroz", "scala-java-time", V.scala_java_time, LibraryType.Auto) in Scope.Compile.all
    final val shapeless = Library("com.chuusai", "shapeless", V.shapeless, LibraryType.Auto) in Scope.Compile.all

    final val slf4j_api = Library("org.slf4j", "slf4j-api", V.slf4j, LibraryType.Invariant) in Scope.Compile.jvm
    final val slf4j_simple = Library("org.slf4j", "slf4j-simple", V.slf4j, LibraryType.Invariant) in Scope.Test.jvm
  }

  import Deps._

  final val scala212 = ScalaVersion("2.12.9")
  final val scala213 = ScalaVersion("2.13.0")

  object Groups {
    final val fundamentals = Set(Group("fundamentals"))
    final val distage = Set(Group("distage"))
    final val logstage = Set(Group("logstage"))
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
        "coverageEnabled" := false,
        "scalaJSModuleKind" in(SettingScope.Project, Platform.Js) := "ModuleKind.CommonJSModule".raw,
      ),
      plugins = Plugins(Seq(Plugin("ScalaJSBundlerPlugin", Platform.Js)))
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

    object logstage {
      final val id = ArtifactId("logstage")
      final val basePath = "logstage"
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
      "npmDependencies" in(SettingScope.Compile, Platform.Js) ++= Seq("hash.js" -> "1.1.7"),
    ),
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
    Groups.distage,
  )

  final lazy val distageProxyCglib = Artifact(
    ArtifactId("distage-proxy-cglib"),
    Projects.distage.basePath,
    Seq(cglib_nodep),
    fundamentalsBasics ++ Seq(distageModel).map(_.name in Scope.Compile.all),
    Targets.jvm,
    Groups.distage,
  )

  final lazy val distageCore  = Artifact(
    ArtifactId("distage-core"),
    Projects.distage.basePath,
    Seq(cglib_nodep),
    Seq(distageModel, distageProxyCglib).map(_.name in Scope.Compile.all),
    Targets.jvm,
    Groups.distage,
  )

  final lazy val distageConfig  = Artifact(
    ArtifactId("distage-config"),
    Projects.distage.basePath,
    Seq(typesafe_config),
    Seq(distageModel, fundamentalsTypesafeConfig).map(_.name in Scope.Compile.all) ++ Seq(distageCore).map(_.name in Scope.Test.all),
    Targets.jvm,
    Groups.distage,
    settings = Seq(
      "fork" in SettingScope.Test := true,
    )
  )

  final lazy val distageRolesApi  = Artifact(
    ArtifactId("distage-roles-api"),
    Projects.distage.basePath,
    Seq.empty,
    Seq(distageModel).map(_.name in Scope.Compile.all),
    Targets.jvm,
    Groups.distage,
  )

  final lazy val distagePlugins  = Artifact(
    ArtifactId("distage-plugins"),
    Projects.distage.basePath,
    Seq(fast_classpath_scanner),
    Seq(distageModel).map(_.name in Scope.Compile.all) ++ Seq(distageCore, distageConfig, logstageCore).map(_.name in Scope.Test.all),
    Targets.jvm,
    Groups.distage,
    settings = Seq(
      "fork" in SettingScope.Test := true,
    )
  )

  final val allMonads = (cats_all ++ Seq(zio_core)).map(_ in Scope.Optional.all)

  final lazy val distageRoles  = Artifact(
    ArtifactId("distage-roles"),
    Projects.distage.basePath,
    allMonads,
    Seq(distageRolesApi, distageCore, distagePlugins, distageConfig, logstageDi, logstageAdapterSlf4j, logstageRenderingCirce).map(_.name in Scope.Compile.all),
    Targets.jvm,
    Groups.distage,
  )

  final lazy val distageStatic  = Artifact(
    ArtifactId("distage-static"),
    Projects.distage.basePath,
    Seq(shapeless),
    Seq(distageCore).map(_.name in Scope.Compile.all) ++ Seq(distageRoles).map(_.name in Scope.Test.all),
    Targets.jvm,
    Groups.distage,
  )

  final lazy val distageTestkit  = Artifact(
    ArtifactId("distage-testkit"),
    Projects.distage.basePath,
    Seq(scalatest.dependency in Scope.Compile.all),
    Seq(distageCore, distagePlugins, distageConfig, distageRoles, logstageDi).map(_.name in Scope.Compile.all),
    Targets.jvm,
    Groups.distage,
    settings = Seq(
      "classLoaderLayeringStrategy" in SettingScope.Test := "ClassLoaderLayeringStrategy.Flat".raw,
    )
  )

  final lazy val distage = Aggregate(
    Projects.distage.id,
    Projects.distage.basePath,
    Seq(
      distageModel,
      distageProxyCglib,
      distageCore,
      distageConfig,
      distageRolesApi,
      distagePlugins,
      distageRoles,
      distageStatic,
      distageTestkit,
    ),
  )

  final lazy val logstageApi  = Artifact(
    ArtifactId("logstage-api"),
    Projects.logstage.basePath,
    Seq(scala_reflect in Scope.Provided.all) ++ Seq(scala_java_time),
    Seq(fundamentalsReflection).map(_.name in Scope.Compile.all),
    Targets.cross,
    Groups.logstage,
  )

  final lazy val logstageCore  = Artifact(
    ArtifactId("logstage-core"),
    Projects.logstage.basePath,
    Seq(scala_reflect in Scope.Provided.all) ++ Seq(cats_core, zio_core).map(_ in Scope.Optional.all) ++ (cats_all ++ Seq(zio_core)).map(_ in Scope.Test.all),
    Seq(fundamentalsBio, logstageApi).map(_.name in Scope.Compile.all),
    Targets.cross,
    Groups.logstage,
  )

  final lazy val logstageRenderingCirce  = Artifact(
    ArtifactId("logstage-rendering-circe"),
    Projects.logstage.basePath,
    Seq.empty,
    Seq(fundamentalsJsonCirce, logstageCore).map(_.name in Scope.Compile.all),
    Targets.cross,
    Groups.logstage,
  )

  final lazy val logstageDi  = Artifact(
    ArtifactId("logstage-di"),
    Projects.logstage.basePath,
    Seq.empty,
    Seq(logstageCore, logstageConfig, distageConfig, distageModel).map(_.name in Scope.Compile.all) ++ Seq(distageCore).map(_.name in Scope.Test.all),
    Targets.jvm,
    Groups.logstage ++ Groups.distage,
  )

  final lazy val logstageConfig  = Artifact(
    ArtifactId("logstage-config"),
    Projects.logstage.basePath,
    Seq.empty,
    Seq(fundamentalsTypesafeConfig, logstageCore).map(_.name in Scope.Compile.all),
    Targets.jvm,
    Groups.logstage,
  )

  final lazy val logstageAdapterSlf4j  = Artifact(
    ArtifactId("logstage-adapter-slf4j"),
    Projects.logstage.basePath,
    Seq(slf4j_api),
    Seq(logstageCore).map(_.name in Scope.Compile.all),
    Targets.jvm,
    Groups.logstage,
    settings = Seq(
      "compileOrder" in SettingScope.Compile := "CompileOrder.Mixed".raw,
      "compileOrder" in SettingScope.Test := "CompileOrder.Mixed".raw,
      "classLoaderLayeringStrategy" in SettingScope.Test := "ClassLoaderLayeringStrategy.Flat".raw,
    )
  )

  final lazy val logstageSinkSlf4j  = Artifact(
    ArtifactId("logstage-sink-slf4j"),
    Projects.logstage.basePath,
    Seq(slf4j_api, slf4j_simple),
    Seq(logstageApi).map(_.name in Scope.Compile.all) ++  Seq(logstageCore).map(_.name in Scope.Test.all),
    Targets.jvm,
    Groups.logstage,
  )


  final lazy val logstage = Aggregate(
    Projects.logstage.id,
    Projects.logstage.basePath,
    Seq(
      logstageApi,
      logstageCore,
      logstageRenderingCirce,
      logstageDi,
      logstageConfig,
      logstageAdapterSlf4j,
      logstageSinkSlf4j
    ),
  )


  val tgSdk: Project = Project(
    Projects.root.id,
    Seq(
      fundamentals,
      distage,
      logstage,
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
