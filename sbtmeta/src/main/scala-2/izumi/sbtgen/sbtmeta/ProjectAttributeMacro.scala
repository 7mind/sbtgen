package izumi.sbtgen.sbtmeta

import java.nio.file.{Path, Paths}
import scala.annotation.tailrec
import scala.reflect.macros.blackbox

object ProjectAttributeMacro {

  def extractAttrMacro(c: blackbox.Context)(name: c.Expr[String]): c.Expr[Option[String]] = {
    val nameStr = TreeTools.stringLiteral(c)(c.universe)(name.tree)
    extractAttr(c, nameStr, force = true)
  }

  def extractScalaVersionsMacro(c: blackbox.Context)(): c.Expr[Option[String]] = {
    extractAttr(c, "scala-versions")
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
