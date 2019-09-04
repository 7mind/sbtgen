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
      //plugins = Plugins(Seq(Plugin("ScalaJSBundlerPlugin", Platform.Js))),
    )
    final val cross = Seq(jvmPlatform, jsPlatform)
    final val jvm = Seq(jvmPlatform)
  }

  object Projects {

    object root {
      final val id = ArtifactId("izumi")
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
      final val basePath = Seq("fundamentals")

      final val fundamentalsCollections = ArtifactId("fundamentals-collections")
      final val fundamentalsPlatform = ArtifactId("fundamentals-platform")
      final val functional = ArtifactId("fundamentals-functional")
      final val bio = ArtifactId("fundamentals-bio")

      final val typesafeConfig = ArtifactId("fundamentals-typesafe-config")
      final val reflection = ArtifactId("fundamentals-reflection")
      final val fundamentalsJsonCirce = ArtifactId("fundamentals-json-circe")

      final lazy val basics = Seq(
        fundamentalsPlatform,
        functional,
        fundamentalsCollections,
      ).map(_ in Scope.Runtime.all)
    }

    object distage {
      final val id = ArtifactId("distage")
      final val basePath = Seq("distage")

      final lazy val model = ArtifactId("distage-model")
      final lazy val proxyCglib = ArtifactId("distage-proxy-cglib")
      final lazy val core = ArtifactId("distage-core")
      final lazy val config = ArtifactId("distage-config")
      final lazy val rolesApi = ArtifactId("distage-roles-api")
      final lazy val plugins = ArtifactId("distage-plugins")
      final lazy val roles = ArtifactId("distage-roles")
      final lazy val static = ArtifactId("distage-static")
      final lazy val testkit = ArtifactId("distage-testkit")
    }

    object logstage {
      final val id = ArtifactId("logstage")
      final val basePath = Seq("logstage")

      final lazy val api = ArtifactId("logstage-api")
      final lazy val core = ArtifactId("logstage-core")
      final lazy val renderingCirce = ArtifactId("logstage-rendering-circe")
      final lazy val di = ArtifactId("logstage-di")
      final lazy val config = ArtifactId("logstage-config")
      final lazy val adapterSlf4j = ArtifactId("logstage-adapter-slf4j")
      final lazy val sinkSlf4j = ArtifactId("logstage-sink-slf4j")
    }

  }

  final lazy val fundamentals = Aggregate(
    Projects.fundamentals.id,
    Seq(
      Artifact(
        Projects.fundamentals.fundamentalsCollections,
        Seq.empty,
        Seq.empty,
      ),
      Artifact(
        Projects.fundamentals.fundamentalsPlatform,
        Seq.empty,
        Seq(
          Projects.fundamentals.fundamentalsCollections in Scope.Compile.all
        ),
        settings = Seq(
          "npmDependencies" in(SettingScope.Compile, Platform.Js) ++= Seq("hash.js" -> "1.1.7"),
        ),
        plugins = Plugins(Seq(Plugin("ScalaJSBundlerPlugin", Platform.Js))),
      ),
      Artifact(
        Projects.fundamentals.functional,
        Seq.empty,
        Seq.empty,
      ),
      Artifact(
        Projects.fundamentals.bio,
        (cats_all ++ Seq(zio_core)).map(_ in Scope.Optional.all),
        Seq(Projects.fundamentals.functional in Scope.Runtime.all),
      ),
      Artifact(
        Projects.fundamentals.typesafeConfig,
        Seq(typesafe_config, scala_reflect in Scope.Compile.jvm),
        Projects.fundamentals.basics ++ Seq(Projects.fundamentals.reflection in Scope.Runtime.jvm),
        platforms = Targets.jvm,
      ),
      Artifact(
        Projects.fundamentals.reflection,
        Seq(boopickle, scala_reflect in Scope.Provided.all),
        Projects.fundamentals.basics,
      ),
      Artifact(
        Projects.fundamentals.fundamentalsJsonCirce,
        circe ++ Seq(jawn in Scope.Compile.js),
        Projects.fundamentals.basics,
      ),
    ),
    pathPrefix = Projects.fundamentals.basePath,
    groups = Groups.fundamentals,
    defaultPlatforms = Targets.cross,
  )


  final val allMonads = (cats_all ++ Seq(zio_core)).map(_ in Scope.Optional.all)
  final val allMonadsTest = (cats_all ++ Seq(zio_core)).map(_ in Scope.Test.all)


  final lazy val distage = Aggregate(
    Projects.distage.id,
    Seq(
      Artifact(
        Projects.distage.model,
        (cats_all).map(_ in Scope.Optional.all) ++ Seq(scala_reflect in Scope.Compile.all),
        Projects.fundamentals.basics ++ Seq(Projects.fundamentals.bio, Projects.fundamentals.reflection).map(_ in Scope.Compile.all),
      ),
      Artifact(
        Projects.distage.proxyCglib,
        Seq(cglib_nodep),
        Projects.fundamentals.basics ++ Seq(Projects.distage.model).map(_ in Scope.Compile.all),
      ),
      Artifact(
        Projects.distage.core,
        Seq(cglib_nodep),
        Seq(Projects.distage.model, Projects.distage.proxyCglib).map(_ in Scope.Compile.all),
      ),
      Artifact(
        Projects.distage.config,
        Seq(typesafe_config),
        Seq(Projects.distage.model, Projects.fundamentals.typesafeConfig).map(_ in Scope.Compile.all) ++
          Seq(Projects.distage.core).map(_ in Scope.Test.all),
        settings = Seq(
          "fork" in SettingScope.Test := true,
        )
      ),
      Artifact(
        Projects.distage.rolesApi,
        Seq.empty,
        Seq(Projects.distage.model).map(_ in Scope.Compile.all),
      ),
      Artifact(
        Projects.distage.plugins,
        Seq(fast_classpath_scanner),
        Seq(Projects.distage.model).map(_ in Scope.Compile.all) ++
          Seq(Projects.distage.core).map(_ tin Scope.Test.all) ++
          Seq(Projects.distage.config, Projects.logstage.core).map(_ in Scope.Test.all),
        settings = Seq(
          "fork" in SettingScope.Test := true,
        )
      ),
      Artifact(
        Projects.distage.roles,
        allMonads,
        Seq(Projects.distage.rolesApi, Projects.logstage.di, Projects.logstage.adapterSlf4j, Projects.logstage.renderingCirce).map(_ in Scope.Compile.all) ++
          Seq(Projects.distage.core, Projects.distage.plugins, Projects.distage.config).map(_ tin Scope.Compile.all),
      ),
      Artifact(
        Projects.distage.static,
        Seq(shapeless),
        Seq(Projects.distage.core).map(_ tin Scope.Compile.all) ++ Seq(Projects.distage.roles).map(_ tin Scope.Test.all),
      ),
      Artifact(
        Projects.distage.testkit,
        Seq(scalatest.dependency in Scope.Compile.all) ++ allMonadsTest,
        Seq(Projects.distage.config, Projects.distage.roles, Projects.logstage.di).map(_ in Scope.Compile.all) ++ Seq(Projects.distage.core, Projects.distage.plugins).map(_ tin Scope.Compile.all),
        settings = Seq(
          "classLoaderLayeringStrategy" in SettingScope.Test := "ClassLoaderLayeringStrategy.Flat".raw,
        )
      ),
    ),
    pathPrefix = Projects.distage.basePath,
    defaultPlatforms = Targets.jvm,
    groups = Groups.distage,
  )

  final lazy val logstage = Aggregate(
    Projects.logstage.id,
    Seq(
      Artifact(
        Projects.logstage.api,
        Seq(scala_reflect in Scope.Provided.all) ++ Seq(scala_java_time),
        Seq(Projects.fundamentals.reflection).map(_ in Scope.Compile.all),
      ),
      Artifact(
        Projects.logstage.core,
        Seq(scala_reflect in Scope.Provided.all) ++
          Seq(cats_core, zio_core).map(_ in Scope.Optional.all) ++
          allMonadsTest,
        Seq(Projects.fundamentals.bio).map(_ in Scope.Compile.all) ++ Seq(Projects.logstage.api).map(_ tin Scope.Compile.all),
      ),
      Artifact(
        Projects.logstage.renderingCirce,
        Seq.empty,
        Seq(Projects.fundamentals.fundamentalsJsonCirce).map(_ in Scope.Compile.all) ++ Seq(Projects.logstage.core).map(_ tin Scope.Compile.all),
      ),
      Artifact(
        Projects.logstage.di,
        Seq.empty,
        Seq(Projects.logstage.config, Projects.distage.config, Projects.distage.model).map(_ in Scope.Compile.all) ++
          Seq(Projects.distage.core).map(_ in Scope.Test.all) ++
          Seq(Projects.logstage.core).map(_ tin Scope.Compile.all),
        platforms = Targets.jvm,
        groups = Groups.distage,
      ),
      Artifact(
        Projects.logstage.config,
        Seq.empty,
        Seq(Projects.fundamentals.typesafeConfig, Projects.logstage.core).map(_ in Scope.Compile.all),
        platforms = Targets.jvm,
      ),
      Artifact(
        Projects.logstage.adapterSlf4j,
        Seq(slf4j_api),
        Seq(Projects.logstage.core).map(_ tin Scope.Compile.all),
        platforms = Targets.jvm,
        settings = Seq(
          "compileOrder" in SettingScope.Compile := "CompileOrder.Mixed".raw,
          "compileOrder" in SettingScope.Test := "CompileOrder.Mixed".raw,
          "classLoaderLayeringStrategy" in SettingScope.Test := "ClassLoaderLayeringStrategy.Flat".raw,
        )
      ),
      Artifact(
        Projects.logstage.sinkSlf4j,
        Seq(slf4j_api, slf4j_simple),
        Seq(Projects.logstage.api).map(_ in Scope.Compile.all) ++ Seq(Projects.logstage.core).map(_ tin Scope.Test.all),
        platforms = Targets.jvm,
      )
    ),
    pathPrefix = Projects.logstage.basePath,
    groups = Groups.logstage,
    defaultPlatforms = Targets.cross,
  )


  val izumi: Project = Project(
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
