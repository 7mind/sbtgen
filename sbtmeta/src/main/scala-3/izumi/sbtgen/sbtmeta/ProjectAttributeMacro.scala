package izumi.sbtgen.sbtmeta

import java.nio.file.{Path, Paths}
import java.time.LocalDateTime
import scala.annotation.tailrec
import scala.quoted.{Expr, Quotes, quotes}
import scala.util.chaining.scalaUtilChainingOps

object ProjectAttributeMacro {

  def buildTimestampMacro(using Quotes): Expr[LocalDateTime] = {
    val time = LocalDateTime.now()
    '{ LocalDateTime.of(${Expr(time.getYear)}, ${Expr(time.getMonthValue)}, ${Expr(time.getDayOfMonth)}, ${Expr(time.getHour)}, ${Expr(time.getMinute)}, ${Expr(time.getSecond)}, ${Expr(time.getNano)}) }
  }

  def extractAttrMacro(name: Expr[String])(using Quotes): Expr[Option[String]] = {
    val nameStr = name.valueOrAbort
    extractAttr(nameStr)
  }

  def extractAttrMandatoryMacro(name: Expr[String])(using Quotes): Expr[Option[String]] = {
    val nameStr = name.valueOrAbort
    extractAttr(nameStr, force = true)
  }

  def extractProjectGroupIdMacro(using Quotes): Expr[Option[String]] = {
    extractAttr("product-group")
  }

  def extractSbtVersionMacro(using Quotes): Expr[Option[String]] = {
    extractAttr("sbt-version")
  }

  def extractScalatestVersionMacro(using Quotes): Expr[Option[String]] = {
    extractAttr("scalatest-version")
  }

  def extractScalaVersionsMacro(using Quotes): Expr[Option[String]] = {
    extractAttr("scala-versions")
  }

  def extractScalaVersionMacro(using Quotes): Expr[Option[String]] = {
    extractAttr("scala-version")
  }

  def extractProjectVersionMacro(using Quotes): Expr[Option[String]] = {
    extractAttr("product-version")
  }

  private def extractAttr(name: String, force: Boolean = false)(using Quotes): Expr[Option[String]] = {
    var scala3MacroSettingsExist: Boolean = true
    val macroSettings: List[String] = {
      quotes.reflect.report.warning(
        """Required implementation of -Xmacro-settings doesn't exist in Scala 3 yet.
          |
          |Please fix Dotty issue #12038 to proceed.
          |
          |See: https://github.com/lampepfl/dotty/issues/12038
          |   - https://github.com/lampepfl/dotty/pull/12039""".stripMargin
      )
      scala3MacroSettingsExist = false
      Nil
    }
    val prefix = s"$name="
    val value = macroSettings.find(_.startsWith(prefix)).filterNot(_.isEmpty).map(_.stripPrefix(prefix))
    if (value.isEmpty) {
      s"Undefined macro parameter $name, add `-Xmacro-settings:$prefix<value>` into `scalac` options"
        `pipe` (if (force && scala3MacroSettingsExist) quotes.reflect.report.errorAndAbort else quotes.reflect.report.warning)
    }
    Expr[Option[String]](value)
  }

  def findProjectRootMacro(using Quotes): Expr[Option[String]] = {
    val srcPath = quotes.reflect.Position.ofMacroExpansion.sourceFile.getJPath.getOrElse(
      quotes.reflect.report.errorAndAbort("Couldn't find file path of the current file")
    )
    val result = projectRoot(srcPath).map(_.toFile.getCanonicalPath)
    Expr[Option[String]](result)
  }

  @tailrec
  private def projectRoot(cp: Path): Option[Path] = {
    if (cp.resolve("build.sbt").toFile.exists()) {
      Some(cp)
    } else {
      val parent = cp.getParent

      if (parent == null || parent == cp.getRoot) {
        None
      } else {
        projectRoot(parent)
      }
    }
  }

}
