package izumi.sbtgen.sbtmeta

import java.time.LocalDateTime

import scala.language.experimental.macros

object SbtgenMeta {
  def buildTimestamp(): LocalDateTime = macro ProjectAttributeMacro.buildTimestampMacro

  def projectRoot(): Option[String] = macro ProjectAttributeMacro.findProjectRootMacro

  def extractSbtProjectGroupId(): Option[String] = macro ProjectAttributeMacro.extractProjectGroupIdMacro

  def extractSbtProjectVersion(): Option[String] = macro ProjectAttributeMacro.extractProjectVersionMacro

  def extractSbtVersion(): Option[String] = macro ProjectAttributeMacro.extractSbtVersionMacro

  def extractScalatestVersion(): Option[String] = macro ProjectAttributeMacro.extractScalatestVersionMacro

  def extractScalaVersion(): Option[String] = macro ProjectAttributeMacro.extractScalaVersionMacro

  def extractScalaVersions(): Option[String] = macro ProjectAttributeMacro.extractScalaVersionsMacro

  def extract(name: String): Option[String] = macro ProjectAttributeMacro.extractAttrMacro

  def extractMandatory(name: String): Option[String] = macro ProjectAttributeMacro.extractAttrMacro
}
