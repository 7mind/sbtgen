package izumi.sbt.compat

import izumi.sbt.plugins.IzumiPropertiesPlugin.autoImport._
import sbt.Keys._
import sbt._

object IvyUpdateOptions {

  def settings: Seq[Def.Setting[_]] = Seq(
    updateOptions := updateOptions
      .value
      .withInterProjectFirst(sys.props.getBoolean("build.update.inter-project-first", default = true))
      .withCachedResolution(sys.props.getBoolean("build.update.cached-resolution", default = true))
      .withGigahorse(sys.props.getBoolean("build.update.gigahorse", default = true))
      .withLatestSnapshots(sys.props.getBoolean("build.update.latest-snapshots", default = false)),
  )
}
