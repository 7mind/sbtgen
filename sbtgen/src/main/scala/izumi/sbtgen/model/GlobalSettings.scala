package izumi.sbtgen.model

case class GlobalSettings(
                           groupId: String,
                           sbtVersion: Option[String] = Some("1.3.8"),
                           scalaJsVersion: Version = Version.VConst("1.0.0"),
                           scalaNativeVersion: Version = Version.VConst("0.4.0-M2"),
                           crossProjectVersion: Version = Version.VConst("1.0.0"),
                           bundlerVersion: Version = Version.VConst("0.17.0"),
                         )
