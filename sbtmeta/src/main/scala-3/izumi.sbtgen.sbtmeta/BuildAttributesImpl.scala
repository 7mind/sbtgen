package izumi.sbtgen.sbtmeta

import java.nio.file.Path
import java.time.LocalDateTime

import scala.annotation.tailrec
import scala.quoted.{Expr, Quotes}

object BuildAttributesImpl {

  def sbtProjectRoot()(using quotes: Quotes): Expr[Option[String]] = {
    import quotes.reflect.*

    val result = SourceFile.current.getJPath
      .flatMap(findProjectRoot)
      .map(_.toFile.getCanonicalPath)

    Expr(result)
  }

  @tailrec
  private def findProjectRoot(cp: Path): Option[Path] = {
    if (cp.resolve("build.sbt").toFile.exists()) {
      Some(cp)
    } else {
      val parent = cp.getParent

      if (parent == null || parent == cp.getRoot) {
        None
      } else {
        findProjectRoot(parent)
      }
    }
  }

}
