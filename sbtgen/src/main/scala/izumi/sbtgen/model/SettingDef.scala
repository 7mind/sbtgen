package izumi.sbtgen.model

import scala.language.implicitConversions

sealed trait SettingScope
object SettingScope {
  case object Project extends SettingScope
  case object Build extends SettingScope
  case object Compile extends SettingScope
  case object Test extends SettingScope
  case class Raw(value: String) extends SettingScope
}

case class FullSettingScope(scope: SettingScope, platform: Platform)


sealed trait SettingDef {
  def scope: FullSettingScope
}

object SettingDef {

  sealed trait KVSettingDef extends SettingDef {
    def name: String

    def op: SettingOp
  }

  case class RawSettingDef(value: String, scope: FullSettingScope = FullSettingScope(SettingScope.Compile, Platform.All)) extends SettingDef

  case class UnscopedSettingDef(name: String, op: SettingOp, value: Const, scope: FullSettingScope) extends KVSettingDef

  case class ScopedSettingDef(name: String, op: SettingOp, defs: Seq[(SettingKey, Const)], scope: FullSettingScope) extends KVSettingDef

}

sealed trait SettingOp

object SettingOp {

  case object Append extends SettingOp

  case object Assign extends SettingOp

  case object Extend extends SettingOp

}



case class SettingKey(language: Option[ScalaVersion], release: Option[Boolean])
object SettingKey {
  def Default: SettingKey = SettingKey(None, None)
}


sealed trait Const

object Const {

  import scala.collection.compat._

  private[this] type ImportInspectionWorkaround = BuildFrom[Nothing, Nothing, Nothing]

  trait Conv[T] {
    def to(v: T): Const
  }

  implicit def make[T: Conv](a: T): Const = implicitly[Conv[T]].to(a)

  implicit object ConstToConst extends Conv[Const] {
    override def to(v: Const): Const = v
  }

  implicit object IntToConst extends Conv[Int] {
    override def to(v: Int): Const = CInt(v)
  }

  implicit object StrToConst extends Conv[String] {
    override def to(v: String): Const = CString(v)
  }

  implicit object BoolToConst extends Conv[Boolean] {
    override def to(v: Boolean): Const = CBoolean(v)
  }

  implicit def t2ToConst[T1: Conv, T2: Conv]: Conv[Tuple2[T1, T2]] = (a: Tuple2[T1, T2]) => CTuple(Seq(
    implicitly[Conv[T1]].to(a._1),
    implicitly[Conv[T2]].to(a._2),
  ))

  implicit def seqToConst[T: Conv]: Conv[Seq[T]] = (a: Seq[T]) => CSeq(a.map(implicitly[Conv[T]].to))

  implicit def mapToConst[T: Conv]: Conv[Map[Scalar, T]] = (a: Map[Scalar, T]) => CMap(a.view.mapValues(implicitly[Conv[T]].to).toMap)

  sealed trait Scalar extends Const

  case class CInt(value: Int) extends Scalar

  case class CString(value: String) extends Scalar

  case class CBoolean(value: Boolean) extends Scalar

  case class CRaw(value: String) extends Scalar

  case class CTuple(value: Seq[Const]) extends Const

  case class CSeq(value: Seq[Const]) extends Const

  case class CMap(value: Map[Scalar, Const]) extends Const

  case object EmptySeq extends Const

  case object EmptyMap extends Const

}
