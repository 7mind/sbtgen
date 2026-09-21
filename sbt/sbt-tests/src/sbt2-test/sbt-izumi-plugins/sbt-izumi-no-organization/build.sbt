// Deliberately does NOT define `ThisBuild / organization`: sbt 2.x fails the whole
// build with "Reference to undefined setting" if a global setting reads it directly.
enablePlugins(IzumiEnvironmentPlugin)

name := "sbt-izumi-no-organization"
