package izumi.sbt.plugins

import com.github.sbt.git.GitPlugin
import com.github.sbt.git.SbtGit.GitKeys.*
import sbt.Keys.*
import sbt.*
import sbt.internal.util.ConsoleLogger

object IzumiGitStampPlugin extends AutoPlugin {
  protected val logger: ConsoleLogger = ConsoleLogger()

  override def requires: Plugins = super.requires && GitPlugin

  object Keys {
    lazy val izGitRevision = settingKey[String]("Git revision")
    lazy val izGitBranch = settingKey[String]("Git branch")
    lazy val izGitIsClean = settingKey[Boolean]("Git working dir status")
  }

  import Keys.*

  override def globalSettings: Seq[Def.Setting[_]] = {
    Seq(
      izGitRevision := (ThisBuild / gitHeadCommit).value.getOrElse(""),
      izGitBranch := (ThisBuild / gitCurrentBranch).value,
      izGitIsClean := !(ThisBuild / gitUncommittedChanges).value,
      packageOptions += Def.task {
        val gitValues = Map(
          IzumiManifest.GitBranch -> izGitBranch.value,
          IzumiManifest.GitRepoIsClean -> izGitIsClean.value.toString,
          IzumiManifest.GitHeadRev -> izGitRevision.value,
        )

        gitValues.foreach {
          case (k, v) =>
            logger.debug(s"Manifest value: $k = $v")
        }

        Package.ManifestAttributes(gitValues.toSeq*)
      }.value,
    )
  }
}
