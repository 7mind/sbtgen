package izumi.sbtgen.model

case class GlobalSettings(
                           groupId: String,
                           sbtVersion: Option[String] = Some("1.3.12"),
                           scalaJsVersion: Version = Version.VConst("1.1.0"),
                           scalaNativeVersion: Version = Version.VConst("0.4.0-M2"),
                           crossProjectVersion: Version = Version.VConst("1.0.0"),
                           bundlerVersion: Option[Version] = Some(Version.VConst("0.18.0")),
                           sbtJsDependenciesVersion: Option[Version] = Some(Version.VConst("1.0.2")),
                         )
