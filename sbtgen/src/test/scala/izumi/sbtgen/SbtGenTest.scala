package izumi.sbtgen

import izumi.sbtgen.model.GlobalSettings
import izumi.sbtgen.sbtmeta.SbtgenMeta
import org.scalatest.wordspec.AnyWordSpec

import java.io.File
import scala.sys.process._

class SbtGenTest extends AnyWordSpec {
  def genProjects(dir: String, args: Seq[String], settings: GlobalSettings = GlobalSettings(groupId = "io.7mind")): Unit = {
    val out = args ++ Seq("-d", "-o", _: String)
    Entrypoint.main(Izumi.izumi, settings, out(dir))
    Entrypoint.main(TestDottyProject.project, settings, out(s"$dir/dotty"))
  }

  val jvmDir = "target/test-out-jvm/"
  lazy val genJvmProjects = genProjects(jvmDir, Seq.empty)

  val jsDir = "target/test-out-js/"
  lazy val genJsProjects = genProjects(jsDir, Seq("--js"))

  "sbtgen" should {
    "produce working output in JVM-only" in {
      genJvmProjects
      assert(Process("sbt --batch clean", new File(jvmDir)).! == 0)
    }

    "produce working output in JS" in {
      genJsProjects
      assert(Process("sbt --batch clean", new File(jsDir)).! == 0)
    }

    "produce the same output in JVM-only" in {
      genJvmProjects
      assert(s"diff -r $jvmDir/ test/jvm/".!!.isEmpty)
    }

    "produce the same output in JS" in {
      genJsProjects
      assert(s"diff -r $jsDir/ test/js/".!!.isEmpty)
    }

    "extract build meta" in {
      assert(SbtgenMeta.extractScalaVersions().nonEmpty)
    }
  }
}
