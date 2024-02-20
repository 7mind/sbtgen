package izumi.sbtgen.sbtmeta

import scala.language.experimental.macros

object SbtgenMeta {
  def projectRoot(): Option[String] = macro ProjectAttributeMacro.findProjectRootMacro

  def extractSbtProjectVersion(): Option[String] = macro ProjectAttributeMacro.extractProjectVersionMacro

  def extractScalaVersions(): Option[String] = macro ProjectAttributeMacro.extractScalaVersionsMacro

  def extractMandatory(name: String): Option[String] = macro ProjectAttributeMacro.extractAttrMacro
}
