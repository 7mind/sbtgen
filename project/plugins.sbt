// https://www.scala-sbt.org/0.13/docs/Testing-sbt-plugins.html
libraryDependencies += { "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value }

// https://github.com/sbt/sbt-release
addSbtPlugin("com.github.sbt" % "sbt-release" % "1.4.0")

// http://www.scala-sbt.org/sbt-pgp/
addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.3.2")
