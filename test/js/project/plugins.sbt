// DO NOT EDIT THIS FILE
// IT IS AUTOGENERATED BY `sbtgen.sc` SCRIPT
// ALL CHANGES WILL BE LOST
// https://www.scala-js.org/
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.12.0")

// https://github.com/portable-scala/sbt-crossproject
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.2.0")

// https://scalacenter.github.io/scalajs-bundler/
addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.21.1")

// https://github.com/scala-js/jsdependencies
addSbtPlugin("org.scala-js" % "sbt-jsdependencies" % "1.0.2")

////////////////////////////////////////////////////////////////////////////////

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.9")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.1.1")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.0.0")

addSbtPlugin("com.eed3si9n" % "sbt-unidoc" % "0.4.3")

addSbtPlugin("com.github.sbt" % "sbt-ghpages" % "0.7.0")

addSbtPlugin("io.github.jonas" % "sbt-paradox-material-theme" % "0.6.0")

addSbtPlugin("org.scalameta" % "sbt-mdoc" % "2.3.2")

// Ignore scala-xml version conflict between scoverage where `coursier` requires scala-xml v2
// and scoverage requires scala-xml v1 on Scala 2.12,
// introduced when updating scoverage from 1.9.3 to 2.0.5
libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
