import sbt.internal.librarymanagement.mavenint.PomExtraDependencyAttributes.SbtVersionKey
import sbt.internal.librarymanagement.mavenint.PomExtraDependencyAttributes.ScalaVersionKey
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._
import xerial.sbt.Sonatype.autoImport.sonatypeProfileName

ThisBuild / turbo :=true
ThisBuild / classLoaderLayeringStrategy :=ClassLoaderLayeringStrategy.ScalaLibrary

ThisBuild / organization :="io.7mind.izumi.sbt"

ThisBuild / homepage :=Some(url("https://izumi.7mind.io"))
ThisBuild / licenses :=Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php"))
ThisBuild / developers :=List(
  Developer(id = "7mind", name = "Septimal Mind", url = url("https://github.com/7mind"), email = "team@7mind.io"),
)
ThisBuild / scmInfo :=Some(ScmInfo(url("https://github.com/7mind/sbtgen"), "scm:git:https://github.com/7mind/sbtgen.git"))
(ThisBuild / credentials) += Credentials(file(".secrets/credentials.sonatype-nexus.properties"))

ThisBuild / publishTo :=(if (!isSnapshot.value) {
  sonatypePublishToBundle.value
} else {
  Some(Opts.resolver.sonatypeSnapshots)
})

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
  case (_, _) => Seq.empty
})

lazy val sbtmeta = (project in file("sbtmeta"))
  .settings(
    crossScalaVersions := Seq(ScalaVersions.scala_213, ScalaVersions.scala_212),
    scalaVersion := crossScalaVersions.value.head,
    libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided,
    scalaOpts,
  )

lazy val sbtgen = (project in file("sbtgen"))
  .dependsOn(sbtmeta)
  .settings(
    crossScalaVersions := Seq(ScalaVersions.scala_213, ScalaVersions.scala_212),
    scalaVersion := crossScalaVersions.value.head,
    //    libraryDependencies += "com.github.scopt" %% "scopt" % "4.0.0-RC2",
    libraryDependencies += "com.github.scopt" %% "scopt" % "3.7.1",
    libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.4.3",
    (ThisBuild / libraryDependencies) += "org.scalatest" %% "scalatest" % "3.2.8" % Test,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:sbt-version=${sbtVersion.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}",
    ),
    scalaOpts,
  )

lazy val `sbt-izumi` = (project in file("sbt/sbt-izumi"))
  .settings(
    crossScalaVersions := Seq(ScalaVersions.scala_212),
    scalaVersion := crossScalaVersions.value.head,
    crossSbtVersions := Seq(sbtVersion.value),
    sbtPlugin := true,
    libraryDependencies ++= Seq(
      "io.get-coursier" %% "coursier" % "2.0.16",

      // https://github.com/scoverage/sbt-scoverage
      ("org.scoverage" % "sbt-scoverage" % "1.7.3").extra(SbtVersionKey -> (pluginCrossBuild / sbtBinaryVersion).value, ScalaVersionKey -> (update / scalaBinaryVersion).value).withCrossVersion(Disabled()),

      // http://www.scala-sbt.org/sbt-pgp/
      ("com.github.sbt" % "sbt-pgp" % "2.1.2").extra(SbtVersionKey -> (pluginCrossBuild / sbtBinaryVersion).value, ScalaVersionKey -> (update / scalaBinaryVersion).value).withCrossVersion(Disabled()),

      // https://github.com/sbt/sbt-git
      ("com.typesafe.sbt" % "sbt-git" % "1.0.0").extra(SbtVersionKey -> (pluginCrossBuild / sbtBinaryVersion).value, ScalaVersionKey -> (update / scalaBinaryVersion).value).withCrossVersion(Disabled()),

      // https://github.com/orrsella/sbt-stats
      ("com.orrsella" % "sbt-stats" % "1.0.7").extra(SbtVersionKey -> (pluginCrossBuild / sbtBinaryVersion).value, ScalaVersionKey -> (update / scalaBinaryVersion).value).withCrossVersion(Disabled()),

      // https://github.com/xerial/sbt-sonatype
      ("org.xerial.sbt" % "sbt-sonatype" % "3.9.7").extra(SbtVersionKey -> (pluginCrossBuild / sbtBinaryVersion).value, ScalaVersionKey -> (update / scalaBinaryVersion).value).withCrossVersion(Disabled()),

      // https://github.com/sbt/sbt-release
      ("com.github.sbt" % "sbt-release" % "1.0.15").extra(SbtVersionKey -> (pluginCrossBuild / sbtBinaryVersion).value, ScalaVersionKey -> (update / scalaBinaryVersion).value).withCrossVersion(Disabled()),

      // https://github.com/sbt/sbt-dependency-graph
      ("org.scala-sbt" % "sbt-dependency-tree" % sbtVersion.value).extra(SbtVersionKey -> (pluginCrossBuild / sbtBinaryVersion).value, ScalaVersionKey -> (update / scalaBinaryVersion).value).withCrossVersion(Disabled()),

      // https://github.com/sbt/sbt-duplicates-finder
      ("com.github.sbt" % "sbt-duplicates-finder" % "1.1.0").extra(SbtVersionKey -> (pluginCrossBuild / sbtBinaryVersion).value, ScalaVersionKey -> (update / scalaBinaryVersion).value).withCrossVersion(Disabled()),
    ),
    scalaOpts,
  )

lazy val `sbt-tests` = (project in file("sbt/sbt-tests"))
  .dependsOn(`sbt-izumi`)
  .enablePlugins(ScriptedPlugin)
  .settings(
    crossSbtVersions := Seq(sbtVersion.value),
    crossScalaVersions := Seq(ScalaVersions.scala_212),
    scalaVersion := crossScalaVersions.value.head,
    sbtPlugin := true,
    libraryDependencies ++= Seq(
      "org.scala-sbt" % "sbt" % sbtVersion.value
    ),
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
  )

lazy val `sbtgen-root` = (project in file("."))
  .aggregate(
    sbtgen,
    sbtmeta,
    `sbt-izumi`,
  )
  .settings(
    name := "izumi-sbtgen",
    scalaVersion := ScalaVersions.scala_212,
    crossScalaVersions := Nil,
    publish / skip :=true,
    sonatypeProfileName := "io.7mind",
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies, // : ReleaseStep
      inquireVersions, // : ReleaseStep
      runClean, // : ReleaseStep
      runTest, // : ReleaseStep
      setReleaseVersion, // : ReleaseStep
      commitReleaseVersion, // : ReleaseStep, performs the initial git checks
      tagRelease, // : ReleaseStep
      //publishArtifacts,                       // : ReleaseStep, checks whether `publishTo` is properly set up
      setNextVersion, // : ReleaseStep
      commitNextVersion, // : ReleaseStep
      pushChanges // : ReleaseStep, also checks that an upstream branch is properly configured
    ),
  )
