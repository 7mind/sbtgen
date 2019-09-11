package izumi.sbtgen

import izumi.sbtgen.model.GlobalSettings
import org.scalatest.WordSpec

class SbtGenTest extends WordSpec {
  "sbtgen" should {
    "produce some output" in {
      val settings = GlobalSettings(
        groupId = "io.7mind"
      )

      val out = Seq.empty
//      val out = Seq("-o", "../izumi-r2.wip")
      Entrypoint.main(TestProject.izumi, settings, out)
      Entrypoint.main(TestProject.izumi, settings, Seq("--js") ++ out)
    }
  }
}
