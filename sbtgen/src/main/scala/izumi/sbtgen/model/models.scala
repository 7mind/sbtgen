package izumi.sbtgen.model

sealed trait Version
object Version {

  case class VConst(value: String) extends Version

  case class VExpr(value: String) extends Version

  case object SbtGen extends Version

}

sealed trait Scope {
  def jvm: FullDependencyScope = FullDependencyScope(this, Platform.Jvm)
  def js: FullDependencyScope = FullDependencyScope(this, Platform.Js)
  def native: FullDependencyScope = FullDependencyScope(this, Platform.Native)
  def all: FullDependencyScope = FullDependencyScope(this, Platform.All)
}

object Scope {

  case object Runtime extends Scope

  case object Optional extends Scope

  case object Provided extends Scope

  case object Compile extends Scope

  case object Test extends Scope

}

case class ScalaVersion(value: String)

sealed trait Platform

object Platform {
  sealed trait BasePlatform extends Platform

  case object Jvm extends BasePlatform
  case object Js extends BasePlatform
  case object Native extends BasePlatform

  case object All extends Platform
}

case class PlatformEnv(
                        platform: Platform.BasePlatform,
                        language: Seq[ScalaVersion],
                        settings: Seq[SettingDef] = Seq.empty,
                        plugins: Plugins = Plugins(Seq.empty, Seq.empty),
                      )

case class ArtifactId(value: String)

sealed trait LibraryType

object LibraryType {
  case object Invariant extends LibraryType
  case object AutoJvm extends LibraryType
  case object Auto extends LibraryType
}

case class SbtPlugin(group: String, artifact: String, version: Version)
object SbtPlugin {
  def apply(group: String, artifact: String, version: String): SbtPlugin = new SbtPlugin(group, artifact, Version.VConst(version))
}

sealed trait LibSetting
object LibSetting {
  case class Exclusion(group: String, artifact: String)
  case class Exclusions(exclusions: Seq[Exclusion]) extends LibSetting
  case class Raw(value: String) extends LibSetting
}


case class Library(group: String, artifact: String, version: Version, kind: LibraryType, more: Option[LibSetting]) {
  def more(settings: LibSetting): Library = this.copy(more = Some(settings))
}


object Library {
  def apply(
             group: String,
             artifact: String,
             version: Version,
             kind: LibraryType
           ): Library = {
    new Library(
      group,
      artifact,
      version,
      kind,
      None
    )
  }

  def apply(
             group: String,
             artifact: String,
             version: String,
             kind: LibraryType = LibraryType.Auto
           ): Library = {
    new Library(
      group,
      artifact,
      Version.VConst(version),
      kind,
      None
    )
  }
}

case class FullDependencyScope(scope: Scope, platform: Platform)

case class ScopedLibrary(dependency: Library, scope: FullDependencyScope, compilerPlugin: Boolean = false)

case class ScopedDependency(name: ArtifactId, scope: FullDependencyScope, mergeTestScopes: Boolean = false)

case class Group(name: String)

case class Artifact(
                     name: ArtifactId,
                     libs: Seq[ScopedLibrary],
                     depends: Seq[ScopedDependency],
                     pathPrefix: Seq[String] = Seq.empty,
                     platforms: Seq[PlatformEnv] = Seq.empty,
                     groups: Set[Group] = Set.empty,
                     subGroupId: Option[String] = None,
                     settings: Seq[SettingDef] = Seq.empty,
                     plugins: Plugins = Plugins(Seq.empty, Seq.empty),
                   )

case class Aggregate(
                      name: ArtifactId,
                      artifacts: Seq[Artifact],
                      pathPrefix: Seq[String] = Seq.empty,
                      groups: Set[Group] = Set.empty,
                      defaultPlatforms: Seq[PlatformEnv] = Seq.empty,
                      settings: Seq[SettingDef] = Seq.empty,
                      enableSharedSettings: Boolean = true,
                      dontIncludeInSuperAgg: Boolean = false,
                      sharedDeps: Seq[ScopedDependency] = Seq.empty,
                      sharedLibs: Seq[ScopedLibrary] = Seq.empty,
                      sharedPlugins: Plugins = Plugins(Seq.empty, Seq.empty),
                      sharedSettings: Seq[SettingDef] = Seq.empty
                    ) {
  def merge: Aggregate = {
    val newArtifacts = artifacts.map {
      a =>
        val newPlatforms = if (a.platforms.isEmpty) {
          defaultPlatforms
        } else {
          a.platforms
        }
        val newPrefix = if (a.pathPrefix.isEmpty) {
          pathPrefix
        } else {
          a.pathPrefix
        }
        a.copy(
          platforms = newPlatforms,
          pathPrefix = newPrefix,
          depends = a.depends ++ this.sharedDeps,
          libs = a.libs ++ this.sharedLibs,
          plugins = Plugins(a.plugins.enabled ++ this.sharedPlugins.enabled, a.plugins.disabled ++ this.sharedPlugins.disabled),
          settings = a.settings ++ this.sharedSettings
        )
    }
    this.copy(artifacts = newArtifacts)
  }
}



case class Import(value: String)

case class Plugin(name: String, platform: Platform = Platform.All)

case class Plugins(enabled: Seq[Plugin], disabled: Seq[Plugin] = Seq.empty) {
  def ++(o: Plugins): Plugins = {
    Plugins(enabled ++ o.enabled, disabled ++ o.disabled)
  }
}

case class Project(
                    name: ArtifactId,
                    aggregates: Seq[Aggregate],
                    settings: Seq[SettingDef] = Seq.empty,
                    sharedSettings: Seq[SettingDef] = Seq.empty,
                    sharedAggSettings: Seq[SettingDef] = Seq.empty,
                    sharedRootSettings: Seq[SettingDef] = Seq.empty,
                    imports: Seq[Import] = Seq.empty,
                    globalLibs: Seq[ScopedLibrary] = Seq.empty,
                    rootPlugins: Plugins = Plugins(Seq.empty, Seq.empty),
                    globalPlugins: Plugins = Plugins(Seq.empty, Seq.empty),
                    pluginConflictRules: Map[String, Boolean] = Map.empty,
                    appendPlugins: Seq[SbtPlugin] = Seq.empty,
                  )

