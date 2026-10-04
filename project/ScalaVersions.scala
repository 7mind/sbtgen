object ScalaVersions {
  val scala_212 = "2.12.21"
  val scala_213 = "2.13.18"
  val scala_3 = "3.3.8"

  // sbt 2.x plugins must be compiled with the Scala version of the sbt 2.x metabuild:
  // its API is published as TASTy, which an older compiler cannot read.
  val scala_3_sbt2 = "3.9.0"

  val scalaJsVersion = "1.21.0"
  val scalaNativeVersion = "0.5.10"
}
