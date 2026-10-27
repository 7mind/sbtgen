package izumi.sbt.compat

import sbt.{Credentials, Def}
import sbt.internal.librarymanagement.ivy.IvyCredentials
import sbt.librarymanagement.Configuration

/**
  * sbt 2.x half of the shims for the APIs that moved or disappeared in sbt 2.x
  * and that `sbt2-compat` does not cover.
  *
  * @see [[https://www.scala-sbt.org/2.x/docs/en/changes/sbt-2.0-migration.html the sbt 2.x migration guide]]
  */
object PluginCompat {

  /** Removed in sbt 2.x, see sbt/sbt#8184. */
  def integrationTestConfig: Option[Configuration] = None

  private final val IvyUpdateOptionsClass = "sbt.internal.librarymanagement.ivy.UpdateOptions"

  def ivyUpdateOptionsSettings: Seq[Def.Setting[?]] =
    if (isClassAvailable(IvyUpdateOptionsClass)) IvyUpdateOptions.settings else Seq.empty

  private def isClassAvailable(name: String): Boolean =
    try {
      Class.forName(name, false, getClass.getClassLoader)
      true
    } catch {
      case _: ClassNotFoundException => false
    }

  def resolveCredentials(credentials: Credentials): DirectCredential = {
    val direct = IvyCredentials.toDirect(credentials)
    DirectCredential(direct.host, direct.userName, direct.passwd)
  }
}
