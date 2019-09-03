package izumi.sbtgen.example

import izumi.sbtgen.Entrypoint
import izumi.sbtgen.model.GlobalSettings
import org.scalatest.WordSpec

class SbtGenTest extends WordSpec {
  "sbtgen" should {
    "produce some output" in {
      val settings = GlobalSettings(
        groupId = "io.7mind"
      )

      Entrypoint.main(TestProject.tgSdk, settings, Seq("-o", "../izumi-r2"))
      Entrypoint.main(TestProject.tgSdk, settings, Seq("--js", "-o", "../izumi-r2"))
    }
  }
}
