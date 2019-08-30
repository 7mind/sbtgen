package izumi.sbtgen

import java.nio.file.Paths

object SbtGen {
  def main(args: Array[String]): Unit = {
    ammonite.Main(
      predefCode =
        """import izumi.sbtgen._
          |import izumi.sbtgen.model._
          |
          |""".stripMargin
    )
      .runScript(os.Path(Paths.get("sbtgen.sc").toAbsolutePath), args.map(a => (a, None)))
  }
}
