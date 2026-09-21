package izumi.sbt.compat

/** The parts of sbt's `DirectCredentials` this plugin needs, spelled the same on sbt 1.x and 2.x. */
final case class DirectCredential(host: String, userName: String, passwd: String)
