import sbt.Defaults.sbtPluginExtra

// https://www.scala-sbt.org/0.13/docs/Testing-sbt-plugins.html
libraryDependencies += sbtPluginExtra(
  "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value,
  sbtVersion.value,
  sbtBinaryVersion.value
)

// https://github.com/sbt/sbt-release
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.13")

// http://www.scala-sbt.org/sbt-pgp/
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.0.1")

// https://github.com/xerial/sbt-sonatype
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.2")
