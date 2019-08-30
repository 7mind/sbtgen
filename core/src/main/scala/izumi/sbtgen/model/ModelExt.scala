package izumi.sbtgen.model

import izumi.sbtgen.model.SettingDef.{ScopedSettingDef, UnscopedSettingDef}

trait ModelExt {

  implicit class SettingKeyExt(s: SettingKey) {
    def :=(const: Const): (SettingKey, Const) = {
      (s, const)
    }
  }

  implicit class StringExt(s: String) {
    def :=(const: Const): UnscopedSettingDef = {
      UnscopedSettingDef(s, SettingOp.Assign, const)
    }

    def +=(const: Const): UnscopedSettingDef = {
      UnscopedSettingDef(s, SettingOp.Append, const)
    }

    def ++=(const: Const.EmptyMap.type): UnscopedSettingDef = {
      UnscopedSettingDef(s, SettingOp.Extend, const)
    }

    def ++=(const: Const.EmptySeq.type): UnscopedSettingDef = {
      UnscopedSettingDef(s, SettingOp.Extend, const)
    }

    def ++=[T: Const.Conv](const: Seq[T]): UnscopedSettingDef = {
      import Const._
      UnscopedSettingDef(s, SettingOp.Extend, const)
    }

    def ++=[T: Const.Conv](const: Map[Const.Scalar, T]): UnscopedSettingDef = {
      import Const._
      UnscopedSettingDef(s, SettingOp.Extend, const)
    }

    def :=(const: Seq[(SettingKey, Const)]): ScopedSettingDef = {
      ScopedSettingDef(s, SettingOp.Assign, const)
    }

    def +=(const: Seq[(SettingKey, Const)]): ScopedSettingDef = {
      ScopedSettingDef(s, SettingOp.Append, const)
    }

    def ++=(const: Seq[(SettingKey, Const)]): ScopedSettingDef = {
      ScopedSettingDef(s, SettingOp.Extend, const)
    }


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
