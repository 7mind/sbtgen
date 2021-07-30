package izumi.sbtgen.sbtmeta

import java.nio.file.{Path, Paths}
import java.time.LocalDateTime
import scala.annotation.tailrec
import scala.reflect.macros.blackbox

object ProjectAttributeMacro {

  def buildTimestampMacro(c: blackbox.Context)(): c.Expr[LocalDateTime] = {
    import c.universe._

    val time = LocalDateTime.now()
    c.Expr[LocalDateTime] {
      q"{_root_.java.time.LocalDateTime.of(${time.getYear}, ${time.getMonthValue}, ${time.getDayOfMonth}, ${time.getHour}, ${time.getMinute}, ${time.getSecond}, ${time.getNano})}"
    }
  }

  def extractAttrMacro(c: blackbox.Context)(name: c.Expr[String]): c.Expr[Option[String]] = {
    val nameStr = TreeTools.stringLiteral(c)(c.universe)(name.tree)
    extractAttr(c, nameStr, force = true)
  }

  def extractProjectGroupIdMacro(c: blackbox.Context)(): c.Expr[Option[String]] = {
    extractAttr(c, "product-group")
  }

  def extractSbtVersionMacro(c: blackbox.Context)(): c.Expr[Option[String]] = {
    extractAttr(c, "sbt-version")
  }

  def extractScalatestVersionMacro(c: blackbox.Context)(): c.Expr[Option[String]] = {
    extractAttr(c, "scalatest-version")
  }

  def extractScalaVersionsMacro(c: blackbox.Context)(): c.Expr[Option[String]] = {
    extractAttr(c, "scala-versions")
  }

  def extractScalaVersionMacro(c: blackbox.Context)(): c.Expr[Option[String]] = {
    extractAttr(c, "scala-version")
  }

  def extractProjectVersionMacro(c: blackbox.Context)(): c.Expr[Option[String]] = {
    extractAttr(c, "product-version")
  }

  private def extractAttr(c: blackbox.Context, name: String, force: Boolean = false): c.Expr[Option[String]] = {
    import c.universe._

    val prefix = s"$name="
    val value = c.settings.find(_.startsWith(prefix)).filterNot(_.isEmpty).map(_.stripPrefix(prefix))
    if (value.isEmpty) {
      val msg = if (force) c.error _ else c.warning _
      msg(c.enclosingPosition, s"Undefined macro parameter $name, add `-Xmacro-settings:$prefix<value>` into `scalac` options")
    }
    c.Expr[Option[String]](q"$value")
  }

  def findProjectRootMacro(c: blackbox.Context)(): c.Expr[Option[String]] = {
    import c.universe._
    val srcPath = Paths.get(c.enclosingPosition.source.path)
    val result = projectRoot(srcPath).map(_.toFile.getCanonicalPath)
    c.Expr[Option[String]](q"$result")
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
