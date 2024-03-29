package izumi.sbtgen

import izumi.sbtgen.model.LibSetting.Exclusion
import izumi.sbtgen.model._

object V {
  // foundation
  val scala_212 = "2.12.9"
  val scala_213 = "2.13.0"

  val collection_compat = "2.1.2"

  val kind_projector = "0.10.3"
  val scalatest = "3.1.2"

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

object PV {
  val sbt_mdoc = "2.3.2"
  val sbt_paradox_material_theme = "0.6.0"
  val sbt_ghpages = "0.7.0"
  val sbt_site = "1.4.1"
  val sbt_unidoc = "0.4.3"
  val sbt_scoverage = "2.0.0"
  val sbt_pgp = "2.1.1"
  val sbt_assembly = "0.14.9"
}

object Izumi {

  object Deps {
    final val collection_compat = Library("org.scala-lang.modules", "scala-collection-compat", V.collection_compat)
    final val scalatest = Library("org.scalatest", "scalatest", V.scalatest) in Scope.Test.all

    final val cats_core = Library("org.typelevel", "cats-core", V.cats)
    final val cats_effect = Library("org.typelevel", "cats-effect", V.cats_effect)
    final val cats_all = Seq(
      cats_core,
      cats_effect,
    )

    final val circe = Seq(
      Library("io.circe", "circe-core", V.circe),
      Library("io.circe", "circe-parser", V.circe),
      Library("io.circe", "circe-literal", V.circe),
      Library("io.circe", "circe-generic-extras", V.circe),
      Library("io.circe", "circe-derivation", V.circe_derivation),
    ).map(_ in Scope.Compile.all)

    final val zio_core = Library("dev.zio", "zio", V.zio)
    final val zio_interop_cats = Library("dev.zio", "zio-interop-cats", V.zio_interop_cats)
    final val zio_all = Seq(
      zio_core,
      zio_interop_cats,
    )

    final val typesafe_config = Library("com.typesafe", "config", V.typesafe_config, LibraryType.Invariant) in Scope.Compile.all
    final val boopickle = Library("io.suzaku", "boopickle", "1.3.1") in Scope.Compile.all
    final val jawn = Library("org.typelevel", "jawn-parser", V.jawn, LibraryType.AutoJvm)

    final val scala_sbt = Library("org.scala-sbt", "sbt", Version.VExpr("sbtVersion.value"), LibraryType.Invariant)
    final val scala_compiler = Library("org.scala-lang", "scala-compiler", Version.VExpr("scalaVersion.value"), LibraryType.Invariant)
    final val scala_library = Library("org.scala-lang", "scala-library", Version.VExpr("scalaVersion.value"), LibraryType.Invariant)
    final val scala_reflect = Seq(
      Library("org.scala-lang", "scala-reflect", Version.VExpr("scalaVersion.value"), LibraryType.Invariant) in Scope
        .Provided.all
        .scalaVersion(ScalaVersionScope.AllScala2),
      Library("ch.epfl.lamp", "tasty-reflect", Version.VExpr("scalaVersion.value"), LibraryType.Auto) in Scope.Provided.all.scalaVersion(ScalaVersionScope.AllScala3),
    )
    final val scala_xml = Library("org.scala-lang.modules", "scala-xml", V.scala_xml) in Scope.Compile.all
    final val scalameta = Library("org.scalameta", "scalameta", V.scalameta) in Scope.Compile.all

    final val cglib_nodep =
      Library("cglib", "cglib-nodep", V.cglib_nodep, LibraryType.Invariant).more(LibSetting.Exclusions(Seq(Exclusion("xxx", "yyy")))) in Scope.Compile.jvm

    final val projector = Library("org.typelevel", "kind-projector", "0.10.3", LibraryType.AutoJvm)

    final val fast_classpath_scanner = Library("io.github.classgraph", "classgraph", V.classgraph, LibraryType.Invariant) in Scope.Compile.jvm
    final val scala_java_time = Library("io.github.cquiroz", "scala-java-time", V.scala_java_time) in Scope.Compile.all
    final val shapeless = Library("com.chuusai", "shapeless", V.shapeless) in Scope.Compile.all

    final val slf4j_api = Library("org.slf4j", "slf4j-api", V.slf4j, LibraryType.Invariant) in Scope.Compile.jvm
    final val slf4j_simple = Library("org.slf4j", "slf4j-simple", V.slf4j, LibraryType.Invariant) in Scope.Test.jvm

    final val fastparse = Library("com.lihaoyi", "fastparse", V.fastparse) in Scope.Compile.all

    final val http4s_client = Seq(
      Library("org.http4s", "http4s-blaze-client", V.http4s)
    )

    val http4s_server = Seq(
      Library("org.http4s", "http4s-dsl", V.http4s),
      Library("org.http4s", "http4s-circe", V.http4s),
      Library("org.http4s", "http4s-blaze-server", V.http4s),
    )

    val http4s_all = http4s_server ++ http4s_client

    val asynchttpclient = Library("org.asynchttpclient", "async-http-client", V.asynchttpclient, LibraryType.Invariant)
  }

  import Deps._

  final val scala212 = ScalaVersion("2.12.9")
  final val scala212doc = ScalaVersion("2.12.8")
  final val scala213 = ScalaVersion("2.13.0")

  object Groups {
    final val fundamentals = Set(Group("fundamentals"))
    final val distage = Set(Group("distage"))
    final val logstage = Set(Group("logstage"))
    final val docs = Set(Group("docs"))
  }

  object Targets {
    val targetScala = Seq(scala212, scala213)
    private val jvmPlatform = PlatformEnv(
      Platform.Jvm,
      targetScala,
    )
    private val jvmPlatform212 = PlatformEnv(
      Platform.Jvm,
      Seq(scala212doc),
    )
    private val jsPlatform = PlatformEnv(
      Platform.Js,
      targetScala,
      settings = Seq(
        "coverageEnabled" := false,
        "scalaJSLinkerConfig".in(SettingScope.Project, Platform.Js) := "{ scalaJSLinkerConfig.value.withModuleKind(ModuleKind.CommonJSModule) }".raw,
      ),
    )
    final val cross = Seq(jvmPlatform, jsPlatform)
    final val jvm = Seq(jvmPlatform)
    final val jvmDocs = Seq(jvmPlatform212)
  }

  final val assemblyPluginJvm = Plugin("AssemblyPlugin", Platform.Jvm)
  final val assemblyPluginJs = Plugin("AssemblyPlugin", Platform.Js)

  object Projects {

    final val plugins = Plugins(
      Seq.empty,
      Seq(assemblyPluginJs, assemblyPluginJvm),
    )

    object root {
      final val id = ArtifactId("izumi")
      final val plugins = Plugins(
        Seq.empty,
        Seq(Plugin("AssemblyPlugin", Platform.All)),
      )
      final val settings = Seq()

      final val sharedAggSettings = Seq(
        "crossScalaVersions" := Targets.targetScala.map(_.value),
        "scalaVersion" := "crossScalaVersions.value.head".raw,
      )

      final val docSettings = Seq(
        "crossScalaVersions" := Seq(scala212doc.value),
        "scalaVersion" := "crossScalaVersions.value.head".raw,
      )

      final val sharedRootSettings = Defaults.RootOptions ++ Defaults.SbtMetaRootOptions ++ Seq(
        "crossScalaVersions" := "Nil".raw,
        "scalaVersion" := Targets.targetScala.head.value,
        "organization" in SettingScope.Build := "io.7mind.izumi",
        "homepage" in SettingScope.Build := """Some(url("https://izumi.7mind.io"))""".raw,
        "licenses" in SettingScope.Build := """Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php"))""".raw,
        "developers" in SettingScope.Build :=
          """List(
          Developer(id = "7mind", name = "Septimal Mind", url = url("https://github.com/7mind"), email = "team@7mind.io"),
        )""".raw,
        "scmInfo" in SettingScope.Build := """Some(ScmInfo(url("https://github.com/7mind/izumi"), "scm:git:https://github.com/7mind/izumi.git"))""".raw,
        "scalacOptions" in SettingScope.Build += s"""${"\"" * 3}-Xmacro-settings:scalatest-version=${V.scalatest}${"\"" * 3}""".raw,
      )

      final val sharedSettings = Defaults.SbtMetaSharedOptions ++ Seq(
        "testOptions" in SettingScope.Test += """Tests.Argument("-oDF")""".raw,
        "scalacOptions" ++= Seq(
          SettingKey(Some(scala212), None) := Defaults.Scala212Options,
          SettingKey(Some(scala212doc), None) := Defaults.Scala212Options,
          SettingKey(Some(scala213), None) := Defaults.Scala213Options,
          SettingKey.Default := Const.EmptySeq,
        ),
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

    object idealingua {
      final val id = ArtifactId("idealingua")
      final val basePath = Seq("idealingua-v1")

      final val model = ArtifactId("idealingua-v1-model")
      final val core = ArtifactId("idealingua-v1-core")
      final val runtimeRpcScala = ArtifactId("idealingua-v1-runtime-rpc-scala")
      final val testDefs = ArtifactId("idealingua-v1-test-defs")
      final val transpilers = ArtifactId("idealingua-v1-transpilers")
      final val runtimeRpcHttp4s = ArtifactId("idealingua-v1-runtime-rpc-http4s")
      final val runtimeRpcTypescript = ArtifactId("idealingua-v1-runtime-rpc-typescript")
      final val runtimeRpcCSharp = ArtifactId("idealingua-v1-runtime-rpc-csharp")
      final val runtimeRpcGo = ArtifactId("idealingua-v1-runtime-rpc-go")
      final val compiler = ArtifactId("idealingua-v1-compiler")
    }

    object docs {
      final val id = ArtifactId("doc")
      final val basePath = Seq("doc")

      final lazy val microsite = ArtifactId("microsite")
    }

    object sbtplugins {
      final val id = ArtifactId("sbt-plugins")
      final val basePath = Seq("sbt-plugins")

      final val settings = Seq(
        "sbtPlugin" := true
      )

      final lazy val izumi_deps = ArtifactId("sbt-izumi-deps")
    }

  }

  final val forkTests = Seq(
    "fork".in(SettingScope.Test, Platform.Jvm) := true
  )

  final lazy val fundamentals = Aggregate(
    Projects.fundamentals.id,
    Seq(
      Artifact(
        name = Projects.fundamentals.fundamentalsCollections,
        libs = Seq.empty,
        depends = Seq.empty,
      ),
      Artifact(
        name = Projects.fundamentals.fundamentalsPlatform,
        libs = Seq.empty,
        depends = Seq(
          Projects.fundamentals.fundamentalsCollections in Scope.Compile.all
        ),
        settings = Seq(
          "npmDependencies".in(SettingScope.Compile, Platform.Js) ++= Seq("hash.js" -> "1.1.7")
        ),
        plugins = Plugins(Seq(Plugin("ScalaJSBundlerPlugin", Platform.Js))),
      ),
      Artifact(
        name = Projects.fundamentals.functional,
        libs = Seq.empty,
        depends = Seq.empty,
      ),
      Artifact(
        name = Projects.fundamentals.bio,
        libs = (cats_all ++ Seq(zio_core)).map(_ in Scope.Optional.all),
        depends = Seq(Projects.fundamentals.functional in Scope.Runtime.all),
      ),
      Artifact(
        name = Projects.fundamentals.typesafeConfig,
        libs = Seq(typesafe_config) ++ scala_reflect,
        depends = Projects.fundamentals.basics ++ Seq(Projects.fundamentals.reflection in Scope.Runtime.jvm),
        platforms = Targets.jvm,
      ),
      Artifact(
        name = Projects.fundamentals.reflection,
        libs = Seq(boopickle) ++ scala_reflect,
        depends = Projects.fundamentals.basics,
      ),
      Artifact(
        name = Projects.fundamentals.fundamentalsJsonCirce,
        libs = circe ++ Seq(jawn in Scope.Compile.js),
        depends = Projects.fundamentals.basics,
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
        cats_all.map(_ in Scope.Optional.all) ++ scala_reflect,
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
        settings = forkTests,
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
        settings = forkTests,
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
        Seq(Projects.distage.config, Projects.distage.roles, Projects.logstage.di).map(_ in Scope.Compile.all) ++ Seq(Projects.distage.core, Projects.distage.plugins)
          .map(_ tin Scope.Compile.all),
        settings = Seq(
          "classLoaderLayeringStrategy" in SettingScope.Test := "ClassLoaderLayeringStrategy.Flat".raw
        ),
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
        scala_reflect ++ Seq(scala_java_time),
        Seq(Projects.fundamentals.reflection).map(_ in Scope.Compile.all),
      ),
      Artifact(
        Projects.logstage.core,
        scala_reflect ++
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
        ),
      ),
      Artifact(
        Projects.logstage.sinkSlf4j,
        Seq(slf4j_api, slf4j_simple),
        Seq(Projects.logstage.api).map(_ in Scope.Compile.all) ++ Seq(Projects.logstage.core).map(_ tin Scope.Test.all),
        platforms = Targets.jvm,
      ),
    ),
    pathPrefix = Projects.logstage.basePath,
    groups = Groups.logstage,
    defaultPlatforms = Targets.cross,
  )

  final lazy val idealingua = Aggregate(
    Projects.idealingua.id,
    Seq(
      Artifact(
        Projects.idealingua.model,
        Seq.empty,
        Projects.fundamentals.basics,
      ),
      Artifact(
        Projects.idealingua.core,
        Seq(fastparse),
        Projects.fundamentals.basics ++ Seq(Projects.idealingua.model, Projects.fundamentals.reflection).map(_ in Scope.Compile.all),
      ),
      Artifact(
        Projects.idealingua.runtimeRpcScala,
        scala_reflect ++ (cats_all ++ zio_all).map(_ in Scope.Compile.all),
        Projects.fundamentals.basics ++ Seq(Projects.fundamentals.bio, Projects.fundamentals.fundamentalsJsonCirce).map(_ in Scope.Compile.all),
      ),
      Artifact(
        Projects.idealingua.runtimeRpcHttp4s,
        (http4s_all ++ Seq(asynchttpclient)).map(_ in Scope.Compile.all),
        Seq(Projects.idealingua.runtimeRpcScala, Projects.logstage.core, Projects.logstage.adapterSlf4j).map(_ in Scope.Compile.all) ++
        Seq(Projects.idealingua.testDefs).map(_ in Scope.Test.jvm),
        platforms = Targets.jvm,
      ),
      Artifact(
        Projects.idealingua.transpilers,
        Seq(scala_xml, scalameta),
        Projects.fundamentals.basics ++
        Seq(Projects.fundamentals.fundamentalsJsonCirce, Projects.idealingua.core, Projects.idealingua.runtimeRpcScala).map(_ in Scope.Compile.all) ++
        Seq(Projects.idealingua.testDefs, Projects.idealingua.runtimeRpcTypescript, Projects.idealingua.runtimeRpcGo, Projects.idealingua.runtimeRpcCSharp)
          .map(_ in Scope.Test.jvm),
        settings = forkTests,
      ),
      Artifact(
        Projects.idealingua.testDefs,
        Seq.empty,
        Seq(Projects.idealingua.runtimeRpcScala).map(_ in Scope.Compile.all),
        platforms = Targets.jvm,
      ),
      Artifact(
        Projects.idealingua.runtimeRpcTypescript,
        Seq.empty,
        Seq.empty,
        platforms = Targets.jvm,
      ),
      Artifact(
        Projects.idealingua.runtimeRpcGo,
        Seq.empty,
        Seq.empty,
        platforms = Targets.jvm,
      ),
      Artifact(
        Projects.idealingua.runtimeRpcCSharp,
        Seq.empty,
        Seq.empty,
        platforms = Targets.jvm,
      ),
      Artifact(
        Projects.idealingua.compiler,
        Seq(typesafe_config),
        Seq(
          Projects.idealingua.transpilers,
          Projects.idealingua.runtimeRpcScala,
          Projects.idealingua.runtimeRpcTypescript,
          Projects.idealingua.runtimeRpcGo,
          Projects.idealingua.runtimeRpcCSharp,
          Projects.idealingua.testDefs,
        ).map(_ in Scope.Compile.all),
        platforms = Targets.jvm,
        plugins = Plugins(Seq(assemblyPluginJvm)),
        settings = Seq(
          "mainClass" in SettingScope.Raw("assembly") := """Some("izumi.idealingua.compiler.CommandlineIDLCompiler")""".raw,
          "assemblyMergeStrategy" in SettingScope.Raw("assembly") :=
            """{
              |      // FIXME: workaround for https://github.com/zio/interop-cats/issues/16
              |      case path if path.contains("zio/BuildInfo$.class") =>
              |        MergeStrategy.last
              |      case p =>
              |        (assembly / assemblyMergeStrategy).value(p)
              |}""".stripMargin.raw,
          "artifact" in SettingScope.Raw("Compile / assembly") :=
            """{
              |      val art = (Compile / assembly / artifact).value
              |      art.withClassifier(Some("assembly"))
              |}""".stripMargin.raw,
          SettingDef.RawSettingDef("addArtifact(Compile / assembly / artifact, assembly)"),
        ),
      ),
    ),
    pathPrefix = Projects.idealingua.basePath,
    groups = Groups.logstage,
    defaultPlatforms = Targets.cross,
  )

  val all = Seq(fundamentals, distage, logstage)

  final lazy val docs = Aggregate(
    Projects.docs.id,
    Seq(
      Artifact(
        Projects.docs.microsite,
        (cats_all ++ zio_all ++ http4s_all).map(_ in Scope.Compile.all),
        all.flatMap(_.artifacts).map(_.name in Scope.Compile.all).distinct,
        plugins = Plugins(
          Seq(
            Plugin("ScalaUnidocPlugin"),
            Plugin("ParadoxSitePlugin"),
            Plugin("SitePlugin"),
            Plugin("GhpagesPlugin"),
            Plugin("ParadoxMaterialThemePlugin"),
            Plugin("PreprocessPlugin"),
            Plugin("MdocPlugin"),
          ),
          Seq(Plugin("ScoverageSbtPlugin")),
        ),
        settings = Projects.root.docSettings ++ Seq(
          "coverageEnabled" := false,
          "skip" in SettingScope.Raw("publish") := true,
          """SettingKey[String]("doc-keys-prefix")""" :=
            """{if (isSnapshot.value) {
            "latest/snapshot"
          } else {
            "latest/release"
          }}""".raw,
          "previewFixedPort" := "Some(9999)".raw,
          "com.github.sbt.git.SbtGit.GitKeys.gitRemoteRepo" := "git@github.com:7mind/izumi-microsite.git",
          "classLoaderLayeringStrategy" in SettingScope.Raw("Compile") := "ClassLoaderLayeringStrategy.Flat".raw,
          "mdocIn" := """baseDirectory.value / "src/main/tut"""".raw,
          "sourceDirectory" in SettingScope.Raw("(Compile / paradox)") := "mdocOut.value".raw,
          "mdocExtraArguments" ++= Seq(" --no-link-hygiene"),
          "mappings" in SettingScope.Raw("SitePlugin.autoImport.makeSite") :=
            """{
            (SitePlugin.autoImport.makeSite / mappings)
              .dependsOn(mdoc.toTask(" "))
              .value
          }""".raw,
          "version" in SettingScope.Raw("(Compile / paradox)") := "version.value".raw,
          SettingDef.RawSettingDef("ParadoxMaterialThemePlugin.paradoxMaterialThemeSettings(Compile)"),
          SettingDef.RawSettingDef("addMappingsToSiteDir(ScalaUnidoc / packageDoc / mappings, ScalaUnidoc / siteSubdirName)"),
          SettingDef.RawSettingDef("ScalaUnidoc / unidoc / unidocProjectFilter := inAggregates(`izumi-jvm`, transitive=true)"),
          SettingDef.RawSettingDef("""Compile / paradoxMaterialTheme ~= {
            _.withCopyright("7mind.io")
              .withRepository(uri("https://github.com/7mind/izumi"))
            //        .withColor("222", "434343")
          }"""),
          "siteSubdirName" in SettingScope.Raw("ScalaUnidoc") := """s"${SettingKey[String]("doc-keys-prefix").value}/api"""".raw,
          "siteSubdirName" in SettingScope.Raw("Paradox") := """s"${SettingKey[String]("doc-keys-prefix").value}/doc"""".raw,
          SettingDef.RawSettingDef("""paradoxProperties ++= Map(
            "scaladoc.izumi.base_url" -> s"/${SettingKey[String]("doc-keys-prefix").value}/api/com/github/pshirshov/",
            "scaladoc.base_url" -> s"/${SettingKey[String]("doc-keys-prefix").value}/api/",
            "izumi.version" -> version.value,
          )"""),
          SettingDef.RawSettingDef("""ghpagesCleanSite / excludeFilter :=
            new FileFilter {
              def accept(f: File): Boolean = {
                (f.toPath.startsWith(ghpagesRepository.value.toPath.resolve("latest")) && !f.toPath.startsWith(ghpagesRepository.value.toPath.resolve(SettingKey[String]("doc-keys-prefix").value))) ||
                  (ghpagesRepository.value / "CNAME").getCanonicalPath == f.getCanonicalPath ||
                  (ghpagesRepository.value / ".nojekyll").getCanonicalPath == f.getCanonicalPath ||
                  (ghpagesRepository.value / "index.html").getCanonicalPath == f.getCanonicalPath ||
                  (ghpagesRepository.value / "README.md").getCanonicalPath == f.getCanonicalPath ||
                  f.toPath.startsWith((ghpagesRepository.value / "media").toPath) ||
                  f.toPath.startsWith((ghpagesRepository.value / "v0.5.50-SNAPSHOT").toPath)
              }
            }"""),
        ),
      )
    ),
    pathPrefix = Projects.docs.basePath,
    groups = Groups.docs,
    defaultPlatforms = Targets.jvmDocs,
    dontIncludeInSuperAgg = true,
    enableProjectSharedAggSettings = false,
    settings = Projects.root.docSettings,
  )

  final lazy val sbtplugins = Aggregate(
    Projects.sbtplugins.id,
    Seq(
      Artifact(
        Projects.sbtplugins.izumi_deps,
        Seq.empty,
        Seq.empty,
        settings = Projects.sbtplugins.settings ++ Seq(
          SettingDef.RawSettingDef("""withBuildInfo("izumi.sbt.deps", "Izumi")""")
        ),
      )
    ),
    pathPrefix = Projects.sbtplugins.basePath,
    groups = Groups.docs,
    defaultPlatforms = Targets.jvm,
    dontIncludeInSuperAgg = true,
    enableProjectSharedAggSettings = false,
  )

  val izumi: Project = Project(
    name = Projects.root.id,
    aggregates = Seq(
      fundamentals,
      distage,
      logstage,
      idealingua,
      docs,
      sbtplugins,
    ),
    topLevelSettings = Projects.root.settings,
    sharedSettings = Projects.root.sharedSettings,
    sharedAggSettings = Projects.root.sharedAggSettings,
    rootSettings = Projects.root.sharedRootSettings,
    imports = Seq.empty,
    globalLibs = Seq(
      ScopedLibrary(projector, FullDependencyScope(Scope.Compile, Platform.All), compilerPlugin = true),
      collection_compat in Scope.Compile.all,
      scalatest,
    ),
    globalPlugins = Projects.plugins,
    rootPlugins = Projects.root.plugins,
    pluginConflictRules = Map(assemblyPluginJvm.name -> true),
    appendPlugins = Defaults
      .SbtGenPlugins.map(
        _.copy(version = Version.VConst("0.0.97"))
      ) ++ Seq(
      SbtPlugin("com.eed3si9n", "sbt-assembly", PV.sbt_assembly),
      SbtPlugin("com.jsuereth", "sbt-pgp", PV.sbt_pgp),
      SbtPlugin("org.scoverage", "sbt-scoverage", PV.sbt_scoverage),
      SbtPlugin("com.eed3si9n", "sbt-unidoc", PV.sbt_unidoc),
      SbtPlugin("com.github.sbt", "sbt-ghpages", PV.sbt_ghpages),
      SbtPlugin("io.github.jonas", "sbt-paradox-material-theme", PV.sbt_paradox_material_theme),
      SbtPlugin("org.scalameta", "sbt-mdoc", PV.sbt_mdoc),
    ),
  )
}
