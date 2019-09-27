package izumi.sbtgen

import izumi.sbtgen.model.GlobalSettings
import izumi.sbtgen.sbtmeta.SbtgenMeta
import org.scalatest.WordSpec

class SbtGenTest extends WordSpec {
  "sbtgen" should {
    "produce the same output in JVM" in {
      val settings = GlobalSettings(
        groupId = "io.7mind"
      )

      val dir = "test-out-jvm/"
      val out = Seq("-d", "-o", dir)
      Entrypoint.main(Izumi.izumi, settings, out)

      import scala.sys.process._
      assert("diff -r test-out/ test/jvm/".!!.isEmpty)
    }

    "produce the same output in JS" in {
      val settings = GlobalSettings(
        groupId = "io.7mind"
      )

      val dir = "test-out-js/"
      val out = Seq("--js", "-d", "-o", dir)
      Entrypoint.main(Izumi.izumi, settings, out)

      import scala.sys.process._
      assert("diff -r test-out-js/ test/js/".!!.isEmpty)
    }

    "extract build meta" in {
      assert(SbtgenMeta.extractScalaVersions().nonEmpty)
    }
  }
}
