package izumi.sbt.plugins

import sbt.{Def, _}

object SbtgenVerificationPlugin extends AutoPlugin {
  override def globalSettings: Seq[Def.Setting[_]] = {

    val scripts = file("project") ** "*.sc"
    val scFiles = scripts.get

    val sbtgen = (Seq(new File("sbtgen.sc")) ++ scFiles)
      .filter(_.exists())
      .map(f => (f, f.lastModified()))

    val buildsbt = new File("build.sbt").lastModified()
    val bad = sbtgen.filter(_._2 > buildsbt)

    if (bad.nonEmpty) {
      throw new RuntimeException(s"sbtgen definitions are newer than build.sbt, run `sbtgen.sc`: ${bad.map(_._1)}")
    }

    Seq.empty
  }
}
