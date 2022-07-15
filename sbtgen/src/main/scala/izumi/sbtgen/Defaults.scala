package izumi.sbtgen

import izumi.sbtgen.model.Const.CRaw
import izumi.sbtgen.model._

object Defaults {
  /**
    * For [[Project.rootSettings]]
    */
  final val RootOptions: Seq[SettingDef.UnscopedSettingDef] = Seq(
    "onChangedBuildSource" in SettingScope.Raw("Global") := "ReloadOnSourceChanges".raw,
    "publishMavenStyle" in SettingScope.Build := true,
    "scalacOptions" in SettingScope.Build ++= Seq[Const](
      "-encoding",
      "UTF-8",
      "-feature",
      "-unchecked",
      "-deprecation",
      "-language:higherKinds",
    ),
    "javacOptions" in SettingScope.Build ++= Seq(
      "-encoding",
      "UTF-8",
      "-source",
      "1.8",
      "-target",
      "1.8",
      "-deprecation",
      "-parameters",
      "-Xlint:all",
      "-XDignore.symbol.file",
    ),
  )

  final val SbtMetaRootOptionsScala2: Seq[Const] = Seq(
    """s"-Xmacro-settings:sbt-version=${sbtVersion.value}"""".raw,
    """s"-Xmacro-settings:git-repo-clean=${com.github.sbt.SbtGit.GitKeys.gitUncommittedChanges.value}"""".raw,
    """s"-Xmacro-settings:git-branch=${com.github.sbt.SbtGit.GitKeys.gitCurrentBranch.value}"""".raw,
    """s"-Xmacro-settings:git-described-version=${com.github.sbt.SbtGit.GitKeys.gitDescribedVersion.value.getOrElse("")}"""".raw,
    """s"-Xmacro-settings:git-head-commit=${com.github.sbt.SbtGit.GitKeys.gitHeadCommit.value.getOrElse("")}"""".raw,
  )

  // -Xmacro-settings not implemented yet https://github.com/lampepfl/dotty/issues/12038
  final val SbtMetaRootOptionsScala3: Seq[Const] = Seq()

  final val SbtMetaRootOptions: Seq[SettingDef.UnscopedSettingDef] = Seq(
    "scalacOptions" in SettingScope.Build ++= SbtMetaRootOptionsScala2
  )

  final val SbtMetaSharedOptionsScala2: Seq[Const] = Seq(
    """s"-Xmacro-settings:product-name=${name.value}"""".raw,
    """s"-Xmacro-settings:product-version=${version.value}"""".raw,
    """s"-Xmacro-settings:product-group=${organization.value}"""".raw,
    """s"-Xmacro-settings:scala-version=${scalaVersion.value}"""".raw,
    """s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"""".raw,
  )

  // -Xmacro-settings not implemented yet https://github.com/lampepfl/dotty/issues/12038
  final val SbtMetaSharedOptionsScala3: Seq[Const] = Seq()

  /**
    * For [[Project.sharedSettings]]
    */
  final val SbtMetaSharedOptions: Seq[SettingDef.UnscopedSettingDef] = Seq(
    "scalacOptions" ++= SbtMetaSharedOptionsScala2
  )

  final val CrossScalaPlusSources: Seq[SettingDef.UnscopedSettingDef] = {
    Seq(
      "unmanagedSourceDirectories" in SettingScope.Compile ++= addScalaVersionPlusSourceDirs("Compile / unmanagedSourceDirectories"),
      "unmanagedSourceDirectories" in SettingScope.Test ++= addScalaVersionPlusSourceDirs("Test / unmanagedSourceDirectories"),
    )
  }

  final val CrossScalaRangeSources: Seq[SettingDef.UnscopedSettingDef] = {
    Seq(
      "unmanagedSourceDirectories" in SettingScope.Compile ++= addScalaVersionRangeSourceDirs("Compile / unmanagedSourceDirectories"),
      "unmanagedSourceDirectories" in SettingScope.Test ++= addScalaVersionRangeSourceDirs("Test / unmanagedSourceDirectories"),
    )
  }

  def addScalaVersionPlusSourceDirs(key: String): CRaw = {
    s"""{
       |  val version = scalaVersion.value
       |  val crossVersions = crossScalaVersions.value
       |  import Ordering.Implicits._
       |  val ltEqVersions = crossVersions.map(CrossVersion.partialVersion).filter(_ <= CrossVersion.partialVersion(version)).flatten
       |  ($key).value.flatMap {
       |    case dir if dir.getPath.endsWith("scala") => ltEqVersions.map { case (m, n) => file(dir.getPath + s"-$$m.$$n+") }
       |    case _ => Seq.empty
       |  }
       |}""".stripMargin.raw
  }

  def addScalaVersionRangeSourceDirs(key: String): CRaw = {
    s"""{
       |  val crossVersions = crossScalaVersions.value
       |  import Ordering.Implicits._
       |  val ltEqVersions = crossVersions.map(CrossVersion.partialVersion).sorted.flatten
       |  def joinV = (_: Product).productIterator.mkString(".")
       |  val allRangeVersions = (2 to math.max(2, ltEqVersions.size - 1))
       |    .flatMap(i => ltEqVersions.sliding(i).filter(_.size == i))
       |    .map(l => (l.head, l.last))
       |  CrossVersion.partialVersion(scalaVersion.value).toList.flatMap {
       |    version =>
       |      val rangeVersions = allRangeVersions
       |        .filter { case (l, r) => l <= version && version <= r }
       |        .map { case (l, r) => s"-$${joinV(l)}-$${joinV(r)}" }
       |      ($key).value.flatMap {
       |        case dir if dir.getPath.endsWith("scala") => rangeVersions.map { vStr => file(dir.getPath + vStr) }
       |        case _ => Seq.empty
       |      }
       |  }
       |}""".stripMargin.raw
  }

  final val Scala2Options = Seq[Const](
    "-target:jvm-1.8",
    "-explaintypes", // Explain type errors in more detail.
  )

  final val Scala212Options = Scala2Options ++ Seq[Const](
    "-Xsource:3", // Compile with maximum dotty compatibility
    "-P:kind-projector:underscore-placeholders", // Use underscore type-lambda syntax by default
    "-Ypartial-unification", // 2.12 only

    CRaw("""if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning""""), // enable fatal warnings on CI
    "-Wconf:cat=optimizer:warning", // make optimizer (inliner) warnings non-fatal
    "-Wconf:cat=other-match-analysis:error", // make non-exhaustive matches fatal

    "-Ybackend-parallelism",
    CRaw("math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString"),
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

    "-opt-warnings:_", // 2.12 only
    "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined.
    "-Ywarn-unused:_", // Enable or disable specific `unused' warnings: `_' for all, `-Ywarn-unused:help' to list choices.
    "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver.
    "-Ywarn-dead-code", // Warn when dead code is identified.
    "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
    "-Ywarn-infer-any", // Warn when a type argument is inferred to be `Any`.
    "-Ywarn-nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Ywarn-nullary-unit", // Warn when nullary methods return Unit.
    "-Ywarn-numeric-widen", // Warn when numerics are widened.
    // "-Ywarn-self-implicit", // Spurious warnings for any top-level implicit, including scala.language._
    "-Ywarn-unused-import", // Warn when imports are unused.
    "-Ywarn-value-discard", // Warn when non-Unit expression results are unused.

    // https://github.com/scala/scala/pull/6412
    // https://twitter.com/olafurpg/status/1191299377064824832
    // > The caching logic for compiler plugins is enabled by default in Bloop and that one does make a difference,
    //   around 20/30%, see https://github.com/scala/scala-dev/issues/458
    "-Ycache-plugin-class-loader:always",
    "-Ycache-macro-class-loader:last-modified",
  )

  final val Scala213Options = Scala2Options ++ Seq[Const](
    "-Xsource:3", // Compile with maximum dotty compatibility
    "-P:kind-projector:underscore-placeholders", // Use underscore type-lambda syntax by default

    CRaw("""if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning""""), // enable fatal warnings on CI
    "-Wconf:cat=optimizer:warning", // make optimizer (inliner) warnings non-fatal
    "-Wconf:cat=other-match-analysis:error", // make non-exhaustive matches fatal

    "-Vimplicits", // Enables the tek/splain features to make the compiler print implicit resolution chains when no implicit value can be found
    "-Vtype-diffs", // Enables the tek/splain features to turn type error messages (found: X, required: Y) into colored diffs between the two types

    "-Ybackend-parallelism",
    CRaw("math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString"),
    "-Wdead-code",
    "-Wextra-implicit",
    "-Wnumeric-widen",
    "-Woctal-literal",
    "-Wvalue-discard",
    "-Wunused:_",
    "-Wmacros:after", // Count variables as used when used in macros (e.g izumi.reflect.TagK evidences)

    // https://github.com/scala/scala/pull/6412
    // https://twitter.com/olafurpg/status/1191299377064824832
    // > The caching logic for compiler plugins is enabled by default in Bloop and that one does make a difference,
    //   around 20/30%, see https://github.com/scala/scala-dev/issues/458
    "-Ycache-plugin-class-loader:always",
    "-Ycache-macro-class-loader:last-modified",
  )

  final val Scala3Options = Seq[Const](
    "-Ykind-projector:underscores", // Use underscore type-lambda syntax by default
    "-no-indent",
    "-explain",
    "-deprecation",
    "-feature",
    "-Wconf:any:warning",
  )

  final val SbtGenPlugins = Seq(
    SbtPlugin("io.7mind.izumi.sbt", "sbt-izumi", Version.SbtGen)
  )

}
