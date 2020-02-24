package izumi.sbt.plugins

import sbt.internal.util.ConsoleLogger
import sbt.{Def, _}

object SbtgenVerificationPlugin extends AutoPlugin {
  protected val logger: ConsoleLogger = ConsoleLogger()

  override def globalSettings: Seq[Def.Setting[_]] = {

    val scripts = file("project") ** "*.sc"
    val scFiles = scripts.get

    val sbtgen = (Seq(new File("sbtgen.sc")) ++ scFiles)
      .filter(_.exists())
      .map(f => (f, f.lastModified()))

    val buildsbt = new File("build.sbt").lastModified()
    val bad = sbtgen.filter(_._2 > buildsbt)

    if (bad.nonEmpty) {
      val message = s"sbtgen definitions are newer than build.sbt, run `sbtgen.sc`: ${bad.map(_._1)}"
      if (Option(System.getProperty("sbtgen.modificationError")).forall(_.toBoolean))
        throw new RuntimeException(message)
      else
        logger.warn(message)
    }

    Seq.empty
  }
}
