package izumi.sbtgen.model

case class GenConfig(
                      jvm: Boolean,
                      js: Boolean,
                      native: Boolean,
                      debug: Boolean,
                      mergeTestScopes: Boolean,
                      settings: GlobalSettings,
                      output: String,
                      onlyGroups: Set[String],
                      publishTests: Boolean,
                      compactify: Boolean,
                    ) {
  def jvmOnly: Boolean = jvm && !js && !native
}
