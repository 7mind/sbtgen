package izumi.sbtgen.model

case class GenConfig(
                      jvm: Boolean,
                      js: Boolean,
                      native: Boolean,
                      debug: Boolean,
                      settings: GlobalSettings,
                      output: String,
                      onlyGroups: Set[Group],
                    ) {
  def jvmOnly: Boolean = jvm && !js && !native
}
