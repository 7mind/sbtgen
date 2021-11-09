package izumi.sbtgen.sbtmeta

import java.time.LocalDateTime

object SbtgenMeta {
  inline def buildTimestamp(): LocalDateTime = ${ ProjectAttributeMacro.buildTimestampMacro }
  inline def projectRoot(): Option[String] = ${ ProjectAttributeMacro.findProjectRootMacro }
  inline def extractSbtProjectGroupId(): Option[String] = ${ ProjectAttributeMacro.extractProjectGroupIdMacro }
  inline def extractSbtProjectVersion(): Option[String] = ${ ProjectAttributeMacro.extractProjectVersionMacro }
  inline def extractSbtVersion(): Option[String] = ${ ProjectAttributeMacro.extractSbtVersionMacro }
  inline def extractScalatestVersion(): Option[String] = ${ ProjectAttributeMacro.extractScalatestVersionMacro }
  inline def extractScalaVersion(): Option[String] = ${ ProjectAttributeMacro.extractScalaVersionMacro }
  inline def extractScalaVersions(): Option[String] = ${ ProjectAttributeMacro.extractScalaVersionsMacro }
  inline def extract(inline name: String): Option[String] = ${ ProjectAttributeMacro.extractAttrMacro('{name}) }
  inline def extractMandatory(inline name: String): Option[String] = ${ ProjectAttributeMacro.extractAttrMandatoryMacro('{name}) }
}
