package izumi.sbt.compat

import sbt.Credentials
import sbt.librarymanagement.Configuration
import sbt.librarymanagement.ivy.{DirectCredentials, FileCredentials}

/**
  * sbt 1.x half of the shims for the APIs that moved or disappeared in sbt 2.x
  * and that `sbt2-compat` does not cover.
  *
  * @see [[https://www.scala-sbt.org/2.x/docs/en/changes/sbt-2.0-migration.html the sbt 2.x migration guide]]
  */
object PluginCompat {

  /** Removed in sbt 2.x, see sbt/sbt#8184. */
  def integrationTestConfig: Option[Configuration] = Some(sbt.IntegrationTest)

  def resolveCredentials(credentials: Credentials): DirectCredential = {
    val direct = credentials match {
      case f: FileCredentials =>
        Credentials.loadCredentials(f.path) match {
          case Right(value) => value
          case Left(error) => throw new IllegalArgumentException(s"Cannot load credentials from ${f.path}: $error")
        }
      case d: DirectCredentials =>
        d
    }
    DirectCredential(direct.host, direct.userName, direct.passwd)
  }
}
