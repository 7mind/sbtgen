package izumi.sbtgen

import izumi.sbtgen.model.Const.CRaw
import izumi.sbtgen.model._

object Defaults {
  final val SharedOptions = Seq(
    "publishMavenStyle" in SettingScope.Build := true,
    "scalacOptions" in SettingScope.Build ++= Seq[Const](
      "-encoding", "UTF-8",
      "-target:jvm-1.8",
      "-feature",
      "-unchecked",
      "-deprecation",
      "-language:higherKinds",
      "-explaintypes", // Explain type errors in more detail.
    ),
    "javacOptions" in SettingScope.Build ++= Seq(
      "-encoding", "UTF-8",
      "-source", "1.8",
      "-target", "1.8",
      "-deprecation",
      "-parameters",
      "-Xlint:all",
      "-XDignore.symbol.file"
    ),
    "scalacOptions" in SettingScope.Build ++= Seq(
      """s"-Xmacro-settings:product-version=${version.value}"""".raw,
      """s"-Xmacro-settings:product-group=${organization.value}"""".raw,
      """s"-Xmacro-settings:sbt-version=${sbtVersion.value}"""".raw,
    )
  )

  final val Scala212Options = Seq[Const](
    "-Xsource:2.13",

    "-Ybackend-parallelism", CRaw("math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString"),

    "-Ypartial-unification", // 2.12 only
    "-Yno-adapted-args", // 2.12 only

    "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
    "-Xlint:by-name-right-associative", // By-name parameter of right associative operator.
    "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
    "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
    "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
    "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
    "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
    "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
    "-Xlint:nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
    "-Xlint:option-implicit", // Option.apply used implicit view.
    "-Xlint:package-object-classes", // Class or object defined in package object.
    "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
    "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
    "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
    "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
    "-Xlint:unsound-match", // Pattern match may not be typesafe.

    "-opt-warnings:_", //2.12 only
    "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined.
    "-Ywarn-unused:_", // Enable or disable specific `unused' warnings: `_' for all, `-Ywarn-unused:help' to list choices.
    "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver.
    "-Ywarn-dead-code", // Warn when dead code is identified.
    "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
    "-Ywarn-infer-any", // Warn when a type argument is inferred to be `Any`.
    "-Ywarn-nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Ywarn-nullary-unit", // Warn when nullary methods return Unit.
    "-Ywarn-numeric-widen", // Warn when numerics are widened.
    //"-Ywarn-self-implicit", // Spurious warnings for any top-level implicit, including scala.language._
    "-Ywarn-unused-import", // Warn when imports are unused.
    "-Ywarn-value-discard", // Warn when non-Unit expression results are unused.

    // https://github.com/scala/scala/pull/6412
    // https://twitter.com/olafurpg/status/1191299377064824832
    // > The caching logic for compiler plugins is enabled by default in Bloop and that one does make a difference,
    //   around 20/30%, see https://github.com/scala/scala-dev/issues/458
    "-Ycache-plugin-class-loader:always",
    "-Ycache-macro-class-loader:last-modified",
  )

  final val Scala213Options = Seq[Const](
    //        "-Xsource:3.0", // is available
    //        "-Xsource:2.14", // Delay -Xsource:2.14 due to spurious warnings https://github.com/scala/bug/issues/11639
    //        "-Xsource:2.13", // Don't use -Xsource: since it's not recommended... https://github.com/scala/bug/issues/11661
    "-Xlint:_,-eta-sam",

    "-Ybackend-parallelism", CRaw("math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString"),

    "-Wdead-code",
    "-Wextra-implicit",
    "-Wnumeric-widen",
    "-Woctal-literal",
    //"-Wself-implicit", // Spurious warnings for any top-level implicit, including scala.language._
    "-Wunused:_",
    "-Wvalue-discard",

    // https://github.com/scala/scala/pull/6412
    // https://twitter.com/olafurpg/status/1191299377064824832
    // > The caching logic for compiler plugins is enabled by default in Bloop and that one does make a difference,
    //   around 20/30%, see https://github.com/scala/scala-dev/issues/458
    "-Ycache-plugin-class-loader:always",
    "-Ycache-macro-class-loader:last-modified",
  )

  final val SbtGenPlugins = Seq(
    SbtPlugin("io.7mind.izumi.sbt", "sbt-izumi", Version.SbtGen),
  )

  final val SbtMeta = Seq(
    "scalacOptions" ++= Seq(
      """s"-Xmacro-settings:scala-version=${scalaVersion.value}"""".raw,
      """s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"""".raw,
    )
  )

  final val CrossScalaSources = {
    val addVersionSources =
      """_.value.flatMap {
        |  dir =>
        |   val partialVersion = CrossVersion.partialVersion(scalaVersion.value)
        |   def scalaDir(s: String) = file(dir.getPath + s)
        |   Seq(dir) ++ (partialVersion match {
        |     case Some((2, n)) => Seq(scalaDir("_2"), scalaDir("_2." + n.toString))
        |     case Some((x, n)) => Seq(scalaDir("_3"), scalaDir("_" + x.toString + "." + n.toString))
        |   })
        |}""".stripMargin.raw
    Seq(
      "unmanagedSourceDirectories" in SettingScope.Compile %= addVersionSources,
      "unmanagedSourceDirectories" in SettingScope.Test %= addVersionSources,
    )
  }
}
