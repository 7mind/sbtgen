// IzumiFetchPlugin reads IzumiPublishingPlugin's `publishTargets`, so enabling it on its own
// must still pull that plugin in; otherwise the setting is undefined and the build cannot load.
enablePlugins(IzumiFetchPlugin)

name := "sbt-izumi-fetch-alone"
ThisBuild / organization := "izumi.test.fetch"
