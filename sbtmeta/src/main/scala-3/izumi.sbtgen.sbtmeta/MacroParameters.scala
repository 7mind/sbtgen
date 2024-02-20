package izumi.sbtgen.sbtmeta

import scala.quoted.{Expr, Quotes, Type}

object MacroParameters {

  inline def scalaCrossVersions(): Option[String] = macroSetting("scala-versions")

  inline def artifactVersion(): Option[String] = macroSetting("product-version")

  inline def macroSetting(inline name: String): Option[String] = {
    ${ MacroParametersImpl.extractString('{ name }) }
  }

}
