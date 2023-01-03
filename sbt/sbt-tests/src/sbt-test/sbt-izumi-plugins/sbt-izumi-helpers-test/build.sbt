import IzumiConvenienceTasksPlugin.Keys._

enablePlugins(IzumiEnvironmentPlugin)

// -- build settings, root artifact settings, etc
name := "sbt-izumi-helpers-test"

ThisBuild / defaultStubPackage := Some("org.test.project")
ThisBuild / organization := "izumi.test.idl"
