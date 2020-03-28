package izumi.sbtgen.model

import izumi.sbtgen.model.SettingDef.{ScopedSettingDef, UnscopedSettingDef}

trait ModelSyntax {

  implicit class SettingKeyExt(s: SettingKey) {
    def :=(const: Const): (SettingKey, Const) = (s, const)
  }

  trait WithSettingsDsl extends Any {
    protected def s: String
    protected def scope: FullSettingScope

    def :=(const: Const): UnscopedSettingDef = UnscopedSettingDef(s, SettingOp.Assign, const, scope)
    def %=(const: Const): UnscopedSettingDef = UnscopedSettingDef(s, SettingOp.Modify, const, scope)
    def +=(const: Const): UnscopedSettingDef = UnscopedSettingDef(s, SettingOp.Append, const, scope)
    def ++=(const: Const): UnscopedSettingDef = UnscopedSettingDef(s, SettingOp.Extend, const, scope)
    def -=(const: Const): UnscopedSettingDef = UnscopedSettingDef(s, SettingOp.Remove, const, scope)
    def --=(const: Const): UnscopedSettingDef = UnscopedSettingDef(s, SettingOp.Subtract, const, scope)

    def ++=[T: Const.Conv](const: Seq[T]): UnscopedSettingDef = UnscopedSettingDef(s, SettingOp.Extend, const, scope)
    def ++=[T: Const.Conv](const: Map[Const.Scalar, T]): UnscopedSettingDef = UnscopedSettingDef(s, SettingOp.Extend, const, scope)
    def --=[T: Const.Conv](const: Seq[T]): UnscopedSettingDef = UnscopedSettingDef(s, SettingOp.Subtract, const, scope)
    def --=[T: Const.Conv](const: Map[Const.Scalar, T]): UnscopedSettingDef = UnscopedSettingDef(s, SettingOp.Subtract, const, scope)

    def :=(const: Seq[(SettingKey, Const)]): ScopedSettingDef = ScopedSettingDef(s, SettingOp.Assign, const, scope)
    def +=(const: Seq[(SettingKey, Const)]): ScopedSettingDef = ScopedSettingDef(s, SettingOp.Append, const, scope)
    def ++=(const: Seq[(SettingKey, Const)]): ScopedSettingDef = ScopedSettingDef(s, SettingOp.Extend, const, scope)
  }

  class ScopedSettingBuilder(protected val s: String, protected val scope: FullSettingScope) extends WithSettingsDsl

  implicit class StringExt(protected val s: String) extends WithSettingsDsl {
    override protected val scope: FullSettingScope = FullSettingScope(SettingScope.Project, Platform.All)

    def in(scope: SettingScope): ScopedSettingBuilder = new ScopedSettingBuilder(s, FullSettingScope(scope, Platform.All))
    def in(platform: Platform): ScopedSettingBuilder = new ScopedSettingBuilder(s, FullSettingScope(SettingScope.Project, platform))
    def in(scope: SettingScope, platform: Platform): ScopedSettingBuilder = new ScopedSettingBuilder(s, FullSettingScope(scope, platform))

    def raw: Const.CRaw = Const.CRaw(s)
  }

  implicit class ArtifactIdExt(id: ArtifactId) {
    def in(scope: FullDependencyScope): ScopedDependency = ScopedDependency(id, scope)
    def tin(scope: FullDependencyScope): ScopedDependency = ScopedDependency(id, scope, mergeTestScopes = true)
  }

  implicit class DependencyExt(dependency: Library) {
    def in(scope: FullDependencyScope): ScopedLibrary = ScopedLibrary(dependency, scope)
  }

}
