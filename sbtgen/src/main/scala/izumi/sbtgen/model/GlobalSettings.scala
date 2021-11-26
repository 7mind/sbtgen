package izumi.sbtgen.model

import izumi.sbtgen.sbtmeta.SbtgenMeta

final case class GlobalSettings(
  groupId: String,
  sbtVersion: Option[String] = SbtgenMeta.extractMandatory("sbt-version"),
  scalaJsVersion: Version = Version.VConst(SbtgenMeta.extractMandatory("scala-js-version").get),
  scalaNativeVersion: Version = Version.VConst(SbtgenMeta.extractMandatory("scala-native-version").get),
  crossProjectVersion: Version = Version.VConst(SbtgenMeta.extractMandatory("crossproject-version").get),
  bundlerVersion: Option[Version] = Some(Version.VConst(SbtgenMeta.extractMandatory("bundler-version").get)),
  sbtJsDependenciesVersion: Option[Version] = Some(Version.VConst(SbtgenMeta.extractMandatory("sbt-js-dependencies-version").get))
)
