package izumi.sbtgen

import izumi.sbtgen.model.{GenConfig, GlobalSettings, SbtTarget, Version}
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

  private val sbt2Settings = GlobalSettings(
    groupId = "io.7mind",
    sbtVersion = Some(SbtGenTest.sbt2Version),
    sbtTarget = SbtTarget.Sbt2,
    bundlerVersion = None,
    sbtJsDependenciesVersion = None,
  )

  def genSbt2Project(dir: String): Unit = {
    Entrypoint.main(TestSbt2Project.project, sbt2Settings, Seq("-o", dir))
  }

  private def runWith(settings: GlobalSettings, js: Boolean = false): Unit = {
    val config = GenConfig(
      jvm = true,
      js = js,
      native = false,
      debug = false,
      mergeTestScopes = true,
      settings = settings,
      output = "target/test-out-sbt2-rejected/",
      onlyGroups = Set.empty,
      publishTests = true,
      compactify = false,
    )
    Entrypoint.run(config, TestSbt2Project.project, new Renderer(config, TestSbt2Project.project))
  }

  "sbtgen" should {
    "produce the same output in JVM-only (use ./copy-test-directory.sh to fix this test)" in {
      val dir = "target/test-out-jvm/"
      info(s"generated: $dir")

      genProjects(dir, Seq.empty)

      assert(s"diff -r $dir/ test/jvm/".!!.isEmpty)
    }

    "produce the same output in JS (use ./copy-test-directory.sh to fix this test)" in {
      val dir = "target/test-out-js/"
      info(s"generated: $dir")
      genProjects(dir, Seq("--js"))

      assert(s"diff -r $dir/ test/js/".!!.isEmpty)
    }

    "produce the same output for sbt 2 (use ./copy-test-directory.sh to fix this test)" in {
      val dir = "target/test-out-sbt2/"
      info(s"generated: $dir")

      genSbt2Project(dir)

      assert(s"diff -r $dir/ test/sbt2/".!!.isEmpty)
    }

    "reject an sbt version contradicting the sbt target" in {
      val e = intercept[IllegalArgumentException] {
        runWith(sbt2Settings.copy(sbtVersion = Some("1.12.8")))
      }
      assert(e.getMessage.contains("contradicts"))
    }

    "reject scalajs-bundler and jsdependencies when targeting sbt 2" in {
      val e = intercept[IllegalArgumentException] {
        runWith(sbt2Settings.copy(bundlerVersion = Some(Version.VConst("0.21.1"))), js = true)
      }
      assert(e.getMessage.contains("bundlerVersion"))
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

    "produce working output for sbt 2" in {
      val dir = "target/test-out-sbt2-build/"
      genSbt2Project(dir)

      assert(Process("sbt clean", new File(dir)).! == 0)
    }
  }
}

object SbtGenTest {
  final val sbt2Version = "2.0.9"
}
