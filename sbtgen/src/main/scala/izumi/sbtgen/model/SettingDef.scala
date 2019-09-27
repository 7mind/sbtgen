package izumi.sbtgen.model

import scala.language.implicitConversions

sealed trait SettingScope
object SettingScope {
  case object Project extends SettingScope
  case object Build extends SettingScope
  case object Compile extends SettingScope
  case object Test extends SettingScope
  final case class Raw(value: String) extends SettingScope
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

  trait Render[+A] {
    def render(s: SettingDef): A
  }
}

sealed trait SettingOp
object SettingOp {
  case object Append extends SettingOp
  case object Assign extends SettingOp
  case object Extend extends SettingOp
}

final case class SettingKey(language: Option[ScalaVersion], release: Option[Boolean])
object SettingKey {
  def Default: SettingKey = SettingKey(None, None)
}

sealed trait Const

object Const {

  sealed trait Scalar extends Const
  final case class CInt(value: Int) extends Scalar
  final case class CString(value: String) extends Scalar
  final case class CBoolean(value: Boolean) extends Scalar
  final case class CRaw(value: String) extends Scalar

  final case class CTuple(value: Seq[Const]) extends Const
  final case class CSeq(value: Seq[Const]) extends Const
  final case class CMap(value: Map[Scalar, Const]) extends Const
  case object EmptySeq extends Const
  case object EmptyMap extends Const

  trait Conv[T] {
    def to(v: T): Const
  }
  object Conv {
    def apply[T: Conv]: Conv[T] = implicitly
  }

  implicit def make[T: Conv](a: T): Const = Conv[T].to(a)

  implicit def ConstToConst[C <: Const]: Conv[C] = c => c
  implicit val IntToConst: Conv[Int] = CInt(_)
  implicit val StrToConst: Conv[String] = CString(_)
  implicit val BoolToConst: Conv[Boolean] = CBoolean(_)

  implicit def t2ToConst[T1: Conv, T2: Conv]: Conv[(T1, T2)] = {
    case (a, b) => CTuple(Seq(
      Conv[T1].to(a),
      Conv[T2].to(b),
    ))
  }
  implicit def seqToConst[T: Conv]: Conv[Seq[T]] = (a: Seq[T]) => CSeq(a.map(Conv[T].to))
  implicit def mapToConst[T: Conv]: Conv[Map[Scalar, T]] = (a: Map[Scalar, T]) => CMap(a.view.mapValues(Conv[T].to).toMap)
}
