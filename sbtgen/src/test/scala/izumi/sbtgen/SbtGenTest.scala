package izumi.sbtgen

import izumi.sbtgen.model.GlobalSettings
import org.scalatest.WordSpec

class SbtGenTest extends WordSpec {
  "sbtgen" should {
    "produce some output" in {
      val settings = GlobalSettings(
        groupId = "io.7mind"
      )

      val out = Seq()
//      val out = Seq("-d")
//      val out = Seq("-d", "-o", "../izumi-r2.wip")
      Entrypoint.main(Izumi.izumi, settings, out)
      Entrypoint.main(Izumi.izumi, settings, Seq("--js") ++ out)
      Entrypoint.main(Izumi.izumi, settings, Seq("--native") ++ out)
      Entrypoint.main(Izumi.izumi, settings, Seq("--js", "--native") ++ out)
    }
  }
}
