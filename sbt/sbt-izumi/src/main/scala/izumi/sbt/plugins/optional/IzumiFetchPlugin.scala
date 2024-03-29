package izumi.sbt.plugins.optional

import java.io.File

import coursier._
import coursier.core.Authentication
import coursier.maven.MavenRepository
import izumi.sbt.plugins.optional.IzumiPublishingPlugin.Keys.publishTargets
import izumi.sbt.plugins.optional.IzumiPublishingPlugin.MavenTarget
import sbt.Keys.{target, _}
import sbt.internal.util.ConsoleLogger
import sbt.io.{CopyOptions, IO}
import sbt.librarymanagement.ModuleID
import sbt.librarymanagement.ivy.{DirectCredentials, FileCredentials}
import sbt.{AutoPlugin, Def, librarymanagement => lm, settingKey, taskKey}

object IzumiFetchPlugin extends AutoPlugin {

  object Keys {
    lazy val fetchArtifacts = settingKey[Seq[ModuleID]]("Jars to fetch from outside")
    lazy val artifactsTargetDir = settingKey[File]("Jars to fetch from outside")
    lazy val resolveArtifacts = taskKey[Seq[File]]("Performs transitive resolution with Coursier")
    lazy val copyArtifacts = taskKey[Set[File]]("Copy artifacts into target directory")
    lazy val fetchResolvers = taskKey[Seq[coursier.MavenRepository]]("")
    lazy val artifactFetcher = settingKey[CoursierFetch]("")
  }

  import Keys._

  protected val logger: ConsoleLogger = ConsoleLogger()

  import CoursierCompat._

  override def globalSettings: Seq[Def.Setting[_]] = {
    Seq(
      artifactFetcher := new CoursierFetch()
    )
  }
  override def projectSettings: Seq[Def.Setting[_]] = {
    Seq(
      fetchArtifacts := Seq.empty,
      artifactsTargetDir := {
        import sbt.io.syntax._
        target.value / "artifacts"
      },
      fetchResolvers := {
        val defaultResolvers = Seq(
          sbt.librarymanagement.Resolver.DefaultMavenRepository
        )

        (resolvers.value ++ defaultResolvers).collect {
          case m: lm.MavenRepository =>
            m.toCoursier(sbt.Keys.credentials.value)
        }
      },
      resolveArtifacts := Def.task {
        val ownRepos = publishTargets.value.map(_.toCoursier)
        val repos = ownRepos ++ fetchResolvers.value
        val scala = scalaBinaryVersion.value
        val deps = Keys.fetchArtifacts.value.map(_.toCoursier(scala))
        logger.info(s"Fetching external artifacts: ${deps.mkString("\n- ", "\n- ", "")}")
        val resolved = artifactFetcher.value.resolve(repos.distinct, deps)
        logger.info(s"Resolved artifacts: ${resolved.size}")
        resolved
      }.value,
      copyArtifacts := Def.task {
        val targetDir = artifactsTargetDir.value
        val resolved = resolveArtifacts.value
        IO.delete(targetDir)
        IO.createDirectory(targetDir)
        IO.copy(
          resolved.map(r => (r, targetDir.toPath.resolve(r.getName).toFile)),
          CopyOptions(overwrite = true, preserveLastModified = true, preserveExecutable = true),
        )
      }.value,
      lm.syntax.Compile / packageBin := Def.taskDyn {
        copyArtifacts.value

        val ctask = (lm.syntax.Compile / packageBin).value
        Def.task {
          ctask
        }
      }.value,
    )
  }
}

object CoursierCompat {

  implicit class SbtRepoExt(repository: lm.MavenRepository) {
    def toCoursier(creds: Seq[lm.ivy.Credentials]): MavenRepository = {
      val auth = creds
        .map(toDirect)
        .find {
          c =>
            repository.root.contains(c.host)
        }

      auth match {
        case Some(value) =>
          CoursierCompat.toCoursier(repository.root, value)
        case None =>
          MavenRepository(repository.root)
      }

    }
  }

  implicit class IzumiExt(target: MavenTarget) {
    def toCoursier: MavenRepository = {
      val creds: DirectCredentials = toDirect(target.credentials)
      CoursierCompat.toCoursier(target.repo.root, creds)
    }
  }

  def toCoursier(root: String, creds: DirectCredentials): MavenRepository = {
    val auth = Authentication(creds.userName, creds.passwd)
    MavenRepository(root, authentication = Some(auth))
  }

  def toDirect(credentials: lm.ivy.Credentials): DirectCredentials = {
    credentials match {
      case f: FileCredentials =>
        lm.ivy.Credentials.loadCredentials(f.path).right.get
      case d: DirectCredentials =>
        d
    }
  }

  implicit class ModuleIdExt(module: lm.ModuleID) {
    def toCoursier(scalaVersion: String): Dependency = {
      val name = module.crossVersion match {
        case b: lm.Binary =>
          val suffix = Option(b.suffix).filter(_.nonEmpty).getOrElse(scalaVersion)
          Seq(b.prefix, module.name, suffix).filter(_.nonEmpty).mkString("_")

        case _: lm.Disabled =>
          module.name

        case _ =>
          throw new IllegalArgumentException(s"Unexpected crossversion in $module")
      }

      Dependency(
        module = Module(Organization(module.organization), ModuleName(name), module.extraAttributes),
        version = module.revision,
      ).withAttributes(
        attributes = Attributes(`type` = core.Type("jar"))
      )
    }
  }

}

class CoursierFetch {
  protected val logger: ConsoleLogger = ConsoleLogger()

  def resolve(repositories: Seq[MavenRepository], modules: Seq[Dependency]): Seq[File] = {
    Fetch()
      .addDependencies(modules: _*)
      .addRepositories(repositories: _*)
      .run()

  }

}
