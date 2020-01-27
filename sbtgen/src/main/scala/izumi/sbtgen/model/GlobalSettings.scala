package izumi.sbtgen.model

case class GlobalSettings(
                           groupId: String,
                           sbtVersion: String = "1.3.7",
                           scalaJsVersion: Version = Version.VConst("0.6.29"),
                           crossProjectVersion: Version = Version.VConst("0.6.1"),
                           scalaNativeVersion: Version = Version.VConst("0.3.7"),
                           bundlerVersion: Version = Version.VConst("0.14.0"),
                         )
