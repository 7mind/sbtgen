package izumi.sbtgen.model

import scala.language.implicitConversions

case class Version(value: String)

sealed trait Scope {
  def jvm: FullDependencyScope = FullDependencyScope(this, Platform.Jvm)
  def js: FullDependencyScope = FullDependencyScope(this, Platform.Js)
  def native: FullDependencyScope = FullDependencyScope(this, Platform.Native)
  def all: FullDependencyScope = FullDependencyScope(this, Platform.All)
}

object Scope {

  case object Runtime extends Scope

  case object Optional extends Scope

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
                      )

case class ArtifactId(value: String)

sealed trait LibraryType

object LibraryType {

  case object Invariant extends LibraryType

  case object Auto extends LibraryType

}

case class Library(group: String, artifact: String, version: Version, kind: LibraryType)

case class FullDependencyScope(scope: Scope, platform: Platform)

case class ScopedLibrary(dependency: Library, scope: FullDependencyScope)

case class ScopedDependency(name: ArtifactId, scope: FullDependencyScope)

case class Group(name: String)

case class Artifact(
                     name: ArtifactId,
                     basePath: String,
                     libs: Seq[ScopedLibrary],
                     depends: Seq[ScopedDependency],
                     platforms: Seq[PlatformEnv],
                     groups: Set[Group] = Set.empty,
                     subGroupId: Option[String] = None,
                     settings: Seq[SettingDef] = Seq.empty,
                   )

case class Aggregate(
                      name: ArtifactId,
                      path: String,
                      artifacts: Seq[Artifact],
                    )



case class Project(
                    name: ArtifactId,
                    aggregates: Seq[Aggregate],
                    settings: Seq[SettingDef] = Seq.empty,
                  )

