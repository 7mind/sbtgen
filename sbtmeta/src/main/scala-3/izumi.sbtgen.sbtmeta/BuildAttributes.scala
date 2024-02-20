package izumi.sbtgen.sbtmeta

import java.time.LocalDateTime

object BuildAttributes {

  inline def sbtProjectRoot(): Option[String] = ${ BuildAttributesImpl.sbtProjectRoot() }

}
