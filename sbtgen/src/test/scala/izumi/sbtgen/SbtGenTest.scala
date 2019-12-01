package izumi.sbtgen

import izumi.sbtgen.model.GlobalSettings
import izumi.sbtgen.sbtmeta.SbtgenMeta
import org.scalatest.wordspec.AnyWordSpec

class SbtGenTest extends AnyWordSpec {
  "sbtgen" should {
    "produce the same output in JVM-only" in {
      val settings = GlobalSettings(
        groupId = "io.7mind"
      )

      val dir = "target/test-out-jvm/"
      val out = Seq("-d", "-o", dir)
      Entrypoint.main(Izumi.izumi, settings, out)

      import scala.sys.process._
      assert(s"diff -r $dir/ test/jvm/".!!.isEmpty)
    }

    "produce the same output in JS" in {
      val settings = GlobalSettings(
        groupId = "io.7mind"
      )

      val dir = "target/test-out-js/"
      val out = Seq("--js", "-d", "-o", dir)
      Entrypoint.main(Izumi.izumi, settings, out)

      import scala.sys.process._
      assert(s"diff -r $dir/ test/js/".!!.isEmpty)
    }

    "extract build meta" in {
      assert(SbtgenMeta.extractScalaVersions().nonEmpty)
    }
  }
}
