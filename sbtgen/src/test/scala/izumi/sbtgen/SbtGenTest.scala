package izumi.sbtgen

import izumi.sbtgen.model.GlobalSettings
import izumi.sbtgen.sbtmeta.SbtgenMeta
import org.scalatest.wordspec.AnyWordSpec

import java.io.File
import scala.sys.process._

class SbtGenTest extends AnyWordSpec {
  def genProjects(dir: String, args: Seq[String], settings: GlobalSettings = GlobalSettings(groupId = "io.7mind")): Unit = {
    val out = args ++ Seq("-o", _: String)
    Entrypoint.main(Izumi.izumi, settings, out(dir))
    Entrypoint.main(TestDottyProject.project, settings, out(s"$dir/dotty"))
  }

  "sbtgen" should {
    "produce the same output in JVM-only" in {
      val dir = "target/test-out-jvm/"
      genProjects(dir, Seq.empty)

      assert(s"diff -r $dir/ test/jvm/".!!.isEmpty)
    }

    "produce the same output in JS" in {
      val dir = "target/test-out-js/"
      genProjects(dir, Seq("--js"))

      assert(s"diff -r $dir/ test/js/".!!.isEmpty)
    }

    "extract build meta" in {
      assert(SbtgenMeta.extractScalaVersions().nonEmpty)
    }
  }

  "sbtgen/sbt" should {
    "produce working output in JVM-only" in {
      val dir = "target/test-out-jvm-build/"
      genProjects(dir, Seq.empty)

      assert(Process("sbt clean", new File(dir)).! == 0)
      assert(Process("sbt clean", new File(s"$dir/dotty")).! == 0)
    }

    "produce working output in JS" in {
      val dir = "target/test-out-js-build/"
      genProjects(dir, Seq("--js"))

      assert(Process("sbt clean", new File(dir)).! == 0)
      assert(Process("sbt clean", new File(s"$dir/dotty")).! == 0)
    }
  }
}
