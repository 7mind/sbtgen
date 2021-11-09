package izumi.sbtgen.model

import izumi.sbtgen.model.LibSetting.Exclusion
import izumi.sbtgen.model.Platform.BasePlatform
import izumi.sbtgen.sbtmeta.SbtgenMeta

import scala.collection.compat._
import scala.collection.immutable.Queue
import scala.language.implicitConversions

sealed trait Version
object Version {
  case class VConst(value: String) extends Version
  case class VExpr(value: String) extends Version
  case object SbtGen extends Version {
    def value: String = SbtgenMeta.extractSbtProjectVersion().getOrElse("UNKNOWN-SBTGEN")
  }
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
  final case class Raw(s: String) extends Scope
}

final case class ScalaVersion(value: String) {
  def isDotty: Boolean = value.startsWith("0.") || value.startsWith("3.")
}

sealed trait Platform {
  final def supportsPlatform(p: BasePlatform): Boolean = this == p || this == Platform.All
}
object Platform {
  sealed trait BasePlatform extends Platform
  case object Jvm extends BasePlatform
  case object Js extends BasePlatform
  case object Native extends BasePlatform
  case object All extends Platform
}

case class PlatformEnv(
                        platform: BasePlatform,
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
  case class Exclusions(exclusions: Seq[Exclusion]) extends LibSetting
  case class Classifier(classifier: String) extends LibSetting
  case class Raw(value: String) extends LibSetting

  case class Exclusion(group: String, artifact: String)
}

case class Library(group: String, artifact: String, version: Version, kind: LibraryType, more: Seq[LibSetting]) {
  def exclude(exclusions: Exclusion*): Library = copy(more = more :+ LibSetting.Exclusions(exclusions))
  def exclude(group: String, artifact: String): Library = exclude(Exclusion(group, artifact))
  def classifier(s: String): Library = copy(more = more :+ LibSetting.Classifier(s))
  def more(settings: LibSetting): Library = copy(more = more :+ settings)
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
      Queue.empty,
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
      Queue.empty,
    )
  }
}

case class FullDependencyScope(
                                scope: Scope,
                                platform: Platform,
                                scalaVersionScope: ScalaVersionScope = ScalaVersionScope.AllVersions,
                              ) {
  def scalaVersion(scalaVersion: ScalaVersionScope): FullDependencyScope = copy(scalaVersionScope = scalaVersion)
}

case class ScopedLibrary(dependency: Library, scope: FullDependencyScope, compilerPlugin: Boolean = false)
object ScopedLibrary {
  implicit def fromLibrary(library: Library): ScopedLibrary = library in Scope.Compile.all
  implicit def fromLibrarySeq(libraries: Seq[Library]): Seq[ScopedLibrary] = libraries.map(fromLibrary)
}

case class ScopedDependency(name: ArtifactId, scope: FullDependencyScope, mergeTestScopes: Boolean = false)
object ScopedDependency {
  implicit def fromDep(dep: ArtifactId): ScopedDependency = dep in Scope.Compile.all
  implicit def fromDepSeq(deps: Seq[ArtifactId]): Seq[ScopedDependency] = deps.map(fromDep)
}

sealed trait ScalaVersionScope
object ScalaVersionScope {
  case object AllVersions extends ScalaVersionScope
  case object AllScala2 extends ScalaVersionScope
  case object AllScala3 extends ScalaVersionScope
  case class Versions(versions: Seq[ScalaVersion]) extends ScalaVersionScope
  object Versions {
    def apply(versions: ScalaVersion*)(implicit dummyImplicit: DummyImplicit): Versions = new Versions(versions)
  }

  implicit val ord: Ordering[ScalaVersionScope] = {
    import Ordering.Implicits._
    val _ = IterableOnce // prevent unused import compat warning
    Ordering.fromLessThan {
      case (AllVersions, _) => true
      case (AllScala2, AllVersions) => false
      case (AllScala2, _) => true
      case (AllScala3, AllVersions | AllScala2) => false
      case (AllScala3, _) => true
      case (Versions(s1), Versions(s2)) => s1.map(_.value).sorted < s2.map(_.value).sorted
      case (Versions(_), _) => false
    }
  }
}

case class Group(
                  name: String,
                  deps: Set[Group] = Set.empty,
                )

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
                      enableProjectSharedAggSettings: Boolean = true,
                      dontIncludeInSuperAgg: Boolean = false,
                      sharedDeps: Seq[ScopedDependency] = Seq.empty,
                      sharedLibs: Seq[ScopedLibrary] = Seq.empty,
                      sharedPlugins: Plugins = Plugins(Seq.empty, Seq.empty),
                      sharedSettings: Seq[SettingDef] = Seq.empty
                    ) {
  def merge: Aggregate = {
    val newArtifacts = artifacts.map {
      a =>
        val newPlatforms = if (a.platforms.isEmpty) defaultPlatforms else a.platforms
        val newPrefix = if (a.pathPrefix.isEmpty) pathPrefix else a.pathPrefix
        a.copy(
          platforms = newPlatforms,
          pathPrefix = newPrefix,
          depends = this.sharedDeps ++ a.depends,
          libs = this.sharedLibs ++ a.libs,
          plugins = Plugins(this.sharedPlugins.enabled ++ a.plugins.enabled, this.sharedPlugins.disabled ++ a.plugins.disabled),
          settings = this.sharedSettings ++ a.settings
        )
    }
    this.copy(artifacts = newArtifacts)
  }
}

final case class Import(value: String, platform: Platform = Platform.All)

case class Plugin(name: String, platform: Platform = Platform.All)

case class Plugins(
                    enabled: Seq[Plugin] = Seq.empty,
                    disabled: Seq[Plugin] = Seq.empty,
                  ) {
  def ++(o: Plugins): Plugins = {
    Plugins(enabled ++ o.enabled, disabled ++ o.disabled)
  }
}

case class Project(
                    name: ArtifactId,
                    aggregates: Seq[Aggregate],
                    topLevelSettings: Seq[SettingDef] = Seq.empty,
                    sharedSettings: Seq[SettingDef] = Seq.empty,
                    sharedAggSettings: Seq[SettingDef] = Seq.empty,
                    rootSettings: Seq[SettingDef] = Seq.empty,
                    imports: Seq[Import] = Seq.empty,
                    globalLibs: Seq[ScopedLibrary] = Seq.empty,
                    rootPlugins: Plugins = Plugins(Seq.empty, Seq.empty),
                    globalPlugins: Plugins = Plugins(Seq.empty, Seq.empty),
                    pluginConflictRules: Map[String, Boolean] = Map.empty,
                    appendPlugins: Seq[SbtPlugin] = Seq.empty,
                  )
