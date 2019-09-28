package izumi.sbt.plugins.global

import izumi.sbt.plugins
import sbt.AutoPlugin

object IzumiImportsPlugin extends AutoPlugin {
  override def trigger = allRequirements

  //noinspection TypeAnnotation
  object autoImport {
    val IzumiEnvironmentPlugin = plugins.presets.IzumiEnvironmentPlugin
    val IzumiGitEnvironmentPlugin = plugins.presets.IzumiGitEnvironmentPlugin

    val IzumiGitStampPlugin = plugins.IzumiGitStampPlugin

    val IzumiBuildManifestPlugin = plugins.IzumiBuildManifestPlugin
    val IzumiConvenienceTasksPlugin = plugins.IzumiConvenienceTasksPlugin
    val IzumiPropertiesPlugin = plugins.IzumiPropertiesPlugin
    val IzumiResolverPlugin = plugins.IzumiResolverPlugin

    val IzumiExposedTestScopesPlugin = plugins.optional.IzumiExposedTestScopesPlugin
    val IzumiFetchPlugin = plugins.optional.IzumiFetchPlugin
    val IzumiPublishingPlugin = plugins.optional.IzumiPublishingPlugin
  }

}








