// https://www.scala-sbt.org/0.13/docs/Testing-sbt-plugins.html
libraryDependencies += { "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value }


// https://github.com/sbt/sbt-release
addSbtPlugin("com.github.sbt" % "sbt-release" % "1.1.0")

// http://www.scala-sbt.org/sbt-pgp/
addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.2.1")

// https://github.com/xerial/sbt-sonatype
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.10.0")
