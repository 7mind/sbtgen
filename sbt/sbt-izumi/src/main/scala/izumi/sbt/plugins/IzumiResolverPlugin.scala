package izumi.sbt.plugins

import izumi.sbt.compat.PluginCompat
import sbt.Keys._
import sbt._

object IzumiResolverPlugin extends AutoPlugin {

  import IzumiPropertiesPlugin.autoImport._

  override lazy val globalSettings = Seq(
    update / aggregate := sys.props.getBoolean("build.update.aggregate", default = true),
  ) ++ PluginCompat.ivyUpdateOptionsSettings
}
