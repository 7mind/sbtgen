package izumi.sbtgen.model

/**
  * Major sbt version the generated build is meant to be loaded by.
  *
  * sbt 1.x and sbt 2.x compile `build.sbt` with different Scala versions
  * (2.12 and 3.x respectively) and differ in the semantics of bare statements,
  * so the emitted build has to be shaped for one of them explicitly.
  */
sealed trait SbtTarget {
  def majorVersion: String
}

object SbtTarget {
  case object Sbt1 extends SbtTarget {
    override def majorVersion: String = "1"
  }
  case object Sbt2 extends SbtTarget {
    override def majorVersion: String = "2"
  }
}
