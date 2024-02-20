package izumi.sbtgen.sbtmeta

object SbtgenMeta {
  inline def projectRoot(): Option[String] = ${ BuildAttributesImpl.sbtProjectRoot() }

  inline def extractSbtProjectVersion(): Option[String] = MacroParameters.artifactVersion()

  inline def extractScalaVersions(): Option[String] = MacroParameters.scalaCrossVersions()

  inline def extractMandatory(inline name: String): Option[String] = MacroParameters.macroSetting(name)
}
