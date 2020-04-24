package izumi.sbtgen.model

case class GlobalSettings(
                           groupId: String,
                           sbtVersion: Option[String] = Some("1.3.10"),
                           scalaJsVersion: Version = Version.VConst("0.6.32"),
                           scalaNativeVersion: Version = Version.VConst("0.4.0-M2"),
                           crossProjectVersion: Version = Version.VConst("1.0.0"),
                           bundlerVersion: Option[Version] = Some(Version.VConst("0.14.0")),
                           sbtDottyVersion: Version = Version.VConst("0.4.1"),
                         )
