package izumi.sbtgen.model

import izumi.sbtgen.model.SettingDef.{ScopedSettingDef, UnscopedSettingDef}

trait ModelExt {

  implicit class SettingKeyExt(s: SettingKey) {
    def :=(const: Const): (SettingKey, Const) = {
      (s, const)
    }
  }

  trait WithSettingsDsl {
    protected val s: String
    protected val scope: FullSettingScope
    def :=(const: Const): UnscopedSettingDef = {
      UnscopedSettingDef(s, SettingOp.Assign, const, scope)
    }

    def +=(const: Const): UnscopedSettingDef = {
      UnscopedSettingDef(s, SettingOp.Append, const, scope)
    }

    def ++=(const: Const.EmptyMap.type): UnscopedSettingDef = {
      UnscopedSettingDef(s, SettingOp.Extend, const, scope)
    }

    def ++=(const: Const.EmptySeq.type): UnscopedSettingDef = {
      UnscopedSettingDef(s, SettingOp.Extend, const, scope)
    }

    def ++=[T: Const.Conv](const: Seq[T]): UnscopedSettingDef = {
      import Const._
      UnscopedSettingDef(s, SettingOp.Extend, const, scope)
    }

    def ++=[T: Const.Conv](const: Map[Const.Scalar, T]): UnscopedSettingDef = {
      import Const._
      UnscopedSettingDef(s, SettingOp.Extend, const, scope)
    }

    def :=(const: Seq[(SettingKey, Const)]): ScopedSettingDef = {
      ScopedSettingDef(s, SettingOp.Assign, const, scope)
    }

    def +=(const: Seq[(SettingKey, Const)]): ScopedSettingDef = {
      ScopedSettingDef(s, SettingOp.Append, const, scope)
    }

    def ++=(const: Seq[(SettingKey, Const)]): ScopedSettingDef = {
      ScopedSettingDef(s, SettingOp.Extend, const, scope)
    }

  }

  class ScopedSettingBuilder(protected val s: String, protected val scope: FullSettingScope) extends WithSettingsDsl

  implicit class StringExt(protected val s: String) extends WithSettingsDsl {
    override protected val scope: FullSettingScope = FullSettingScope(SettingScope.Project, Platform.All)

    def in(scope: SettingScope): ScopedSettingBuilder = new ScopedSettingBuilder(s, FullSettingScope(scope, Platform.All))
    def in(scope: SettingScope, platform: Platform): ScopedSettingBuilder = new ScopedSettingBuilder(s, FullSettingScope(scope, platform))

    def raw: Const = Const.CRaw(s)
  }



  implicit class ArtifactIdExt(id: ArtifactId) {
    def in(scope: FullDependencyScope): ScopedDependency = {
      ScopedDependency(id, scope)
    }
  }

  implicit class DependencyExt(dependency: Library) {
    def in(scope: FullDependencyScope): ScopedLibrary = {
      ScopedLibrary(dependency, scope)
    }
  }

}
