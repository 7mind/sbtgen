import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

/** True while building the sbt 1.x variant of a plugin, false while building the sbt 2.x one. */
val isSbt1 = Def.setting(scalaBinaryVersion.value == "2.12")

/**
  * Attaches the sbt/Scala binary versions an sbt plugin dependency is published under.
  * On sbt 1.x this yields the `_2.12_1.0` coordinates, on sbt 2.x the `_sbt2_3` ones.
  */
def sbtPlugins(modules: Def.Initialize[Seq[ModuleID]], sbt1Only: Boolean = false): Def.Initialize[Seq[ModuleID]] = Def.setting {
  if (sbt1Only && !isSbt1.value) {
    Seq.empty
  } else {
    val sbtBinary = (pluginCrossBuild / sbtBinaryVersion).value
    val scalaBinary = (update / scalaBinaryVersion).value
    modules.value.map(Defaults.sbtPluginExtra(_, sbtBinary, scalaBinary))
  }
}

ThisBuild / turbo := true

ThisBuild / organization := "io.7mind.izumi.sbt"

ThisBuild / homepage := Some(url("https://izumi.7mind.io"))
ThisBuild / licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php"))
ThisBuild / developers := List(
  Developer(id = "7mind", name = "Septimal Mind", url = url("https://github.com/7mind"), email = "team@7mind.io"),
)
ThisBuild / scmInfo := Some(ScmInfo(url("https://github.com/7mind/sbtgen"), "scm:git:https://github.com/7mind/sbtgen.git"))

ThisBuild / credentials ++= Seq(
  Path.userHome / ".sbt" / "secrets" / "credentials.sonatype-new.properties",
  Path.userHome / ".sbt" / "secrets" / "credentials.sonatype-nexus.properties",
  file(".") / ".secrets" / "credentials.sonatype-nexus.properties"
)
  .filter(_.exists())
  .map(Credentials.apply)

// https://github.com/sbt/sbt/issues/8131
ThisBuild / publishTo := {
  if (isSnapshot.value) {
    Some(
      "central-snapshots" at "https://central.sonatype.com/repository/maven-snapshots/"
    )
  } else {
    localStaging.value
  }
}

val scalaOpts = scalacOptions ++= ((isSnapshot.value, scalaVersion.value) match {
  case (_, ScalaVersions.scala_212) => Seq(
    "-Xsource:2.13",
    "-Ybackend-parallelism",
    "8",
    "-explaintypes",

    "-Yno-adapted-args",
    "-Ypartial-unification",

    "-Xlint:adapted-args",
    "-Xlint:by-name-right-associative",
    "-Xlint:constant",
    "-Xlint:delayedinit-select",
    "-Xlint:doc-detached",
    "-Xlint:inaccessible",
    "-Xlint:infer-any",
    "-Xlint:missing-interpolator",
    "-Xlint:nullary-override",
    "-Xlint:nullary-unit",
    "-Xlint:option-implicit",
    "-Xlint:package-object-classes",
    "-Xlint:poly-implicit-overload",
    "-Xlint:private-shadow",
    "-Xlint:stars-align",
    "-Xlint:type-parameter-shadow",
    "-Xlint:unsound-match",

    "-opt-warnings:_",

    "-Ywarn-extra-implicit",
    "-Ywarn-unused:_",
    "-Ywarn-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-inaccessible",
    "-Ywarn-infer-any",
    "-Ywarn-nullary-override",
    "-Ywarn-nullary-unit",
    "-Ywarn-numeric-widen",
    "-Ywarn-unused-import",
    "-Ywarn-value-discard",
  )
  case (_, ScalaVersions.scala_213) => Seq(
    "-Xsource:2.13",
    "-Ybackend-parallelism",
    "8",
    "-explaintypes",

    "-Xlint:_,-missing-interpolator",

    "-Wdead-code",
    "-Wextra-implicit",
    "-Wnumeric-widen",
    "-Woctal-literal",
    "-Wvalue-discard",
    "-Wunused:_",
  )
  case (_, ScalaVersions.scala_3 | ScalaVersions.scala_3_sbt2) => Seq(
    "-no-indent",
    "-explain",
  )
  case (_, _) => Seq.empty
})

lazy val sbtmeta = (project in file("sbtmeta"))
  .settings(
    crossScalaVersions := Seq(ScalaVersions.scala_3, ScalaVersions.scala_213, ScalaVersions.scala_212),
    scalaVersion := crossScalaVersions.value.head,
    libraryDependencies ++= {
      if (scalaVersion.value.startsWith("2"))
        Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided)
      else Seq.empty
    },
    scalaOpts,
  )

lazy val sbtgen = (project in file("sbtgen"))
  .dependsOn(sbtmeta)
  .settings(
    crossScalaVersions := Seq(ScalaVersions.scala_3, ScalaVersions.scala_213, ScalaVersions.scala_212),
    scalaVersion := crossScalaVersions.value.head,
    libraryDependencies += "com.github.scopt" %% "scopt" % "4.1.0",
    libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.14.0",
    (ThisBuild / libraryDependencies) += "org.scalatest" %% "scalatest" % "3.2.20" % Test,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:sbt-version=${sbtVersion.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}",
      s"-Xmacro-settings:scala-js-version=${ScalaVersions.scalaJsVersion}",
      s"-Xmacro-settings:scala-native-version=${ScalaVersions.scalaNativeVersion}",
      s"-Xmacro-settings:crossproject-version=${Deps.crossProjectVersion}",
      s"-Xmacro-settings:bundler-version=${Deps.bundlerVersion}",
      s"-Xmacro-settings:sbt-js-dependencies-version=${Deps.sbtJsDependenciesVersion}",
    ),
    scalaOpts,
  )


lazy val `sbt-izumi` = (project in file("sbt/sbt-izumi"))
  .settings(
    // sbt 1.x plugins are Scala 2.12, sbt 2.x plugins are Scala 3; `pluginCrossBuild / sbtVersion`
    // maps each Scala version onto the sbt version the plugin is compiled against.
    crossScalaVersions := Seq(ScalaVersions.scala_212, ScalaVersions.scala_3_sbt2),
    scalaVersion := crossScalaVersions.value.head,
    sbtPlugin := true,
    sbtPluginPublishLegacyMavenStyle := false,
    pluginCrossBuild / sbtVersion := {
      if (isSbt1.value) sbtVersion.value else Deps.sbt2Version
    },
    libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always,
    // coursier_3 is built against Scala 3.9, whose TASTy the sbt 2.x metabuild compiler cannot read,
    // so the Scala 3 build consumes the Scala 2.13 artifact instead. Its Scala 2.13 flavours of the
    // standard modules are dropped in favour of the Scala 3 ones sbt itself already brings in.
    libraryDependencies += {
      val coursier = "io.get-coursier" %% "coursier" % Deps.coursierVersion
      if (isSbt1.value) {
        coursier
      } else {
        coursier
          .cross(CrossVersion.for3Use2_13)
          .exclude("org.scala-lang.modules", "scala-xml_2.13")
          .exclude("org.scala-lang.modules", "scala-collection-compat_2.13")
      }
    },
    libraryDependencies ++= sbtPlugins(
      Def.setting(Seq(
        // https://github.com/scoverage/sbt-scoverage
        "org.scoverage" % "sbt-scoverage" % "2.4.4",

        // http://www.scala-sbt.org/sbt-pgp/
        "com.github.sbt" % "sbt-pgp" % "2.3.1",

        // https://github.com/sbt/sbt-git
        "com.github.sbt" % "sbt-git" % "2.1.0",

        // https://github.com/sbt/sbt-release
        "com.github.sbt" % "sbt-release" % "1.4.0",

        // https://github.com/sbt/sbt2-compat, shims the sbt 1.x/2.x API differences
        "com.github.sbt" % "sbt2-compat" % Deps.sbt2CompatVersion,
      ))
    ).value,
    // No sbt 2.x releases exist for these. sbt-dependency-tree is in-sourced into sbt 2.x core,
    // the other two are unmaintained, so the sbt 2.x plugin simply does not re-export them.
    libraryDependencies ++= sbtPlugins(
      Def.setting(Seq(
        // https://github.com/sbt/sbt-dependency-graph
        "org.scala-sbt" % "sbt-dependency-tree" % sbtVersion.value,

        // https://github.com/sbt/sbt-duplicates-finder
        "com.github.sbt" % "sbt-duplicates-finder" % "1.1.0",

        // https://github.com/orrsella/sbt-stats
        "com.orrsella" % "sbt-stats" % "1.0.7",
      )),
      sbt1Only = true,
    ).value,
    libraryDependencies ++= sbtPlugins(
      Def.setting(Seq(
        "org.scala-js" % "sbt-scalajs" % ScalaVersions.scalaJsVersion % Test,
        "org.scala-native" % "sbt-scala-native" % ScalaVersions.scalaNativeVersion % Test,
        "org.portable-scala" % "sbt-scalajs-crossproject" % Deps.crossProjectVersion % Test,
        "ch.epfl.scala" % "sbt-scalajs-bundler" % Deps.bundlerVersion % Test,
        "org.scala-js" % "sbt-jsdependencies" % Deps.sbtJsDependenciesVersion % Test,
      )),
      sbt1Only = true,
    ).value,
    scalaOpts,
  )

lazy val `sbt-tests` = (project in file("sbt/sbt-tests"))
  .dependsOn(`sbt-izumi`)
  .enablePlugins(ScriptedPlugin)
  .settings(
    crossScalaVersions := Seq(ScalaVersions.scala_212, ScalaVersions.scala_3_sbt2),
    scalaVersion := crossScalaVersions.value.head,
    sbtPlugin := true,
    sbtPluginPublishLegacyMavenStyle := false,
    pluginCrossBuild / sbtVersion := {
      if (isSbt1.value) sbtVersion.value else Deps.sbt2Version
    },
    // the scripted fixtures differ between sbt majors, so each gets its own tree
    sbtTestDirectory := {
      val name = if (isSbt1.value) "sbt-test" else "sbt2-test"
      sourceDirectory.value / name
    },
    // sbt 2.x pulls compiler-interface 2.x, which conflicts with the one scala3-compiler wants;
    // the scripted fixtures are the actual tests here, so this project needs no sbt on its classpath
    libraryDependencies ++= {
      if (isSbt1.value) Seq("org.scala-sbt" % "sbt" % sbtVersion.value) else Seq.empty
    },
    publish / skip :=true,
    scriptedLaunchOpts := {
      Seq(
        scriptedLaunchOpts.value,
        Seq(
          "-Xmx1024M",
          "-Dplugin.version=" + version.value,
        ),
        Option(System.getProperty("sbt.ivy.home"))
          .toSeq
          .flatMap(value => Seq(s"-Dsbt.ivy.home=$value", s"-Divy.home=$value")),
      ).flatten
    },
    scriptedBufferLog := false,
    scalaOpts,
    // Ignore scala-xml version conflict between scoverage where `coursier` requires scala-xml v2
    // and scoverage requires scala-xml v1 on Scala 2.12,
    // introduced when updating scoverage from 1.9.3 to 2.0.5
    libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
  )

lazy val `izumi-sbtgen` = (project in file("."))
  .aggregate(
    sbtgen,
    sbtmeta,
    `sbt-izumi`,
  )
  .settings(
    name := "izumi-sbtgen",
    scalaVersion := ScalaVersions.scala_212,
    crossScalaVersions := Nil,
    publish / skip := true,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies, // : ReleaseStep
      inquireVersions, // : ReleaseStep
      runClean, // : ReleaseStep
      runTest, // : ReleaseStep
      runClean, // : ReleaseStep
      ReleaseStep(
        action = { st: State =>
          val extracted = Project.extract(st)
          val ref = extracted.get(`sbt-tests` / thisProjectRef)
          extracted.runInputTask(ref / (Global / scripted), "", st)._1
        }
      ),
      setReleaseVersion, // : ReleaseStep
      commitReleaseVersion, // : ReleaseStep, performs the initial git checks
      tagRelease, // : ReleaseStep
      //publishArtifacts,                       // : ReleaseStep, checks whether `publishTo` is properly set up
      setNextVersion, // : ReleaseStep
      commitNextVersion, // : ReleaseStep
      pushChanges // : ReleaseStep, also checks that an upstream branch is properly configured
    ),
  )
