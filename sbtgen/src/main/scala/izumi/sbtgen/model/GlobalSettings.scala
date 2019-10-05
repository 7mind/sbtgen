package izumi.sbtgen.model

case class GlobalSettings(
                           groupId: String,
                           sbtVersion: String = "1.3.2",
                           scalaJsVersion: String = "0.6.29",
                           crossProjectVersion: String = "0.6.1",
                           scalaNativeVersion: String = "0.3.7",
                           bundlerVersion: String = "0.14.0",
                         )
