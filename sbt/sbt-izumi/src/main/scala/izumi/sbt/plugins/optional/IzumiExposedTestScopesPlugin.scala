package izumi.sbt.plugins.optional

import java.nio.file.Path

import izumi.sbt.compat.PluginCompat
import sbt.internal.inc.Analysis
import sbt.internal.util.ConsoleLogger
import sbt.librarymanagement.Configuration
import sbt.{Attributed, Def, IO, _}
import sbtcompat.PluginCompat._
import xsbti.FileConverter
import xsbti.compile.CompileAnalysis

import scala.util.control.NonFatal

// TODO: does not support SJS yet - it needs to run after ScalaJSPlugin.autoImport.scalaJSIR which is not so trivial
object IzumiExposedTestScopesPlugin extends AutoPlugin {
  // override def trigger = allRequirements

  import Keys._

  protected val logger: ConsoleLogger = ConsoleLogger()

  val testSettings: Seq[Def.Setting[_]] = exposedSettings(Test, "test-classes")

  /**
    * sbt 1.x only: sbt 2.x removed the `IntegrationTest` configuration in favour of
    * declaring integration tests as a separate subproject, so there is nothing to scope these to.
    */
  def itSettings: Seq[Def.Setting[_]] = {
    PluginCompat.integrationTestConfig match {
      case Some(config) =>
        exposedSettings(config, "it-classes")
      case None =>
        throw new UnsupportedOperationException(
          "IzumiExposedTestScopesPlugin.itSettings is unavailable on sbt 2.x: the IntegrationTest configuration was removed, " +
          "declare integration tests as a separate subproject instead"
        )
    }
  }

  override def projectSettings: Seq[sbt.Setting[_]] = testSettings

  private def exposedSettings(config: Configuration, classesDirectoryName: String): Seq[Def.Setting[_]] = {
    Seq(
      // sbt 2.x caches every task; neither CompileAnalysis nor Classpath has a JsonFormat,
      // and both of these rewrite files on disk on every run
      config / compile := Def.uncached {
        extractExposableTestScopeParts(
          streams.value,
          (config / classDirectory).value,
          (config / compile).value,
        )
      },
      config / dependencyClasspath := Def.uncached {
        implicit val converter: FileConverter = fileConverter.value
        modifyTestScopeDependency(
          streams.value,
          (config / dependencyClasspath).value,
          classesDirectoryName,
          projectID.value.name,
        )
      },
    )
  }

  private def modifyTestScopeDependency(
    streams: TaskStreams,
    classpath: Classpath,
    directoryNameToModify: String,
    project: String,
  )(implicit converter: FileConverter
  ): Classpath = {
    val logger = streams.log
    classpath.flatMap {
      entry =>
        val data = toFile(entry.data)
        if (data.getName.equals(directoryNameToModify)) {
          val modified = modifyPath(data).toFile
          logger.debug(s"Classpath entry modified in $project: $data => $modified")
          Seq(Attributed.blank(toFileRef(modified)))
        } else {
          logger.debug(s"Classpath entry NOT modified in $project: $entry")
          Seq(entry)
        }
    }
  }

  private def extractExposableTestScopeParts(
    streams: TaskStreams,
    classDirectory: File,
    compileAnalysis: CompileAnalysis,
  ): CompileAnalysis = {
    val logger = streams.log
    Option(compileAnalysis)
      .collect {
        case a0: Analysis => a0
      }.foreach {
        analysis =>
          val targetExposed: Path = modifyPath(classDirectory)
          IO.delete(targetExposed.toFile)
          targetExposed.toFile.mkdirs()

          import scala.collection.JavaConverters._
          analysis
            .readSourceInfos().getAllSourceInfos.asScala.filter {
              case (sourceFile0, _) =>
                val sourceFile = file(sourceFile0.name())
                val isExposed =
                  try {
                    // TODO: better criterion involving tree parsing, dependencies
                    IO.read(sourceFile).contains("@ExposedTestScope")
                  } catch {
                    case NonFatal(e) =>
                      logger.warn(s"Exception while processing ${sourceFile.getCanonicalPath}: $e")
                      false
                  }
                isExposed
              case _ => false
            }.foreach {
              case (sourceFile, _) =>
                val products = analysis.relations.products(sourceFile)
                products.foreach {
                  p =>
                    val targetProduct = targetExposed.resolve(classDirectory.toPath.relativize(file(p.name()).toPath))
                    IO.copyFile(file(p.name()), targetProduct.toFile)
                }
            }
      }

    compileAnalysis
  }

  private def modifyPath(classDirectory: sbt.File): Path = {
    val parentPath = classDirectory.getParentFile.toPath
    val targetExposed = parentPath.resolve(s"${classDirectory.getName}-exposed")
    targetExposed
  }
}
