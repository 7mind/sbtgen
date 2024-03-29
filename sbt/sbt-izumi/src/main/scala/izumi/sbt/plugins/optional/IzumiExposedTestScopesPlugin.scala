package izumi.sbt.plugins.optional

import java.nio.file.Path

import sbt.internal.inc.Analysis
import sbt.internal.util.ConsoleLogger
import sbt.{Attributed, Def, IO, _}
import xsbti.compile.CompileAnalysis

import scala.util.control.NonFatal

// TODO: does not support SJS yet - it needs to run after ScalaJSPlugin.autoImport.scalaJSIR which is not so trivial
object IzumiExposedTestScopesPlugin extends AutoPlugin {
  // override def trigger = allRequirements

  import Keys._

  protected val logger: ConsoleLogger = ConsoleLogger()

  val testSettings: Seq[Def.Setting[_]] = Seq(
    Test / compile := Def.task {
      extractExposableTestScopeParts(
        streams.value,
        (Test / classDirectory).value,
        (Test / compile).value,
      )
    }.value,
    Test / dependencyClasspath := Def.task {
      modifyTestScopeDependency(
        streams.value,
        (Test / dependencyClasspath).value,
        "test-classes",
        projectID.value.name,
      )
    }.value,
  )

  val itSettings: Seq[Def.Setting[_]] = Seq(
    IntegrationTest / compile := Def.task {
      extractExposableTestScopeParts(
        streams.value,
        (IntegrationTest / classDirectory).value,
        (IntegrationTest / compile).value,
      )
    }.value,
    IntegrationTest / dependencyClasspath := Def.task {
      modifyTestScopeDependency(
        streams.value,
        (IntegrationTest / dependencyClasspath).value,
        "it-classes",
        projectID.value.name,
      )
    }.value,
  )

  override def projectSettings: Seq[sbt.Setting[_]] = testSettings

  private def modifyTestScopeDependency(
    streams: TaskStreams,
    classpath: Classpath,
    directoryNameToModify: String,
    project: String,
  ): Seq[Attributed[File]] = {
    val logger = streams.log
    classpath.flatMap {
      case f if f.data.getName.equals(directoryNameToModify) =>
        val modified = modifyPath(f.data).toFile
        logger.debug(s"Classpath entry modified in $project: ${f.data} => $modified")
        Seq(Attributed.blank(modified))
      case f =>
        logger.debug(s"Classpath entry NOT modified in $project: $f")
        Seq(f)
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
