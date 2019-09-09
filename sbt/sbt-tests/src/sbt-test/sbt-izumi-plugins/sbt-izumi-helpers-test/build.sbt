import IzumiConvenienceTasksPlugin.Keys._

enablePlugins(IzumiEnvironmentPlugin)

// -- build settings, root artifact settings, etc
name := "sbt-izumi-helpers-test"

defaultStubPackage in ThisBuild:= Some("org.test.project")
organization in ThisBuild := "izumi.test.idl"
