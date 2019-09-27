package izumi.sbtgen.impl

import izumi.sbtgen.model.Platform.BasePlatform
import izumi.sbtgen.model.{Aggregate, Artifact, GenConfig, Platform, PlatformEnv}

trait WithArtifactExt {
  this: WithProjectIndex with WithBasicRenderers =>

  protected val config: GenConfig

  protected implicit class AggregateExt(agg: Aggregate) {
    def filteredArtifacts: Seq[Artifact] = {
      agg.artifacts.filter(a => config.onlyGroups.isEmpty || (agg.groups ++ a.groups).intersect(config.onlyGroups).nonEmpty)
    }
  }

  protected implicit class ArtifactExt(a: Artifact) {
    def supportsPlatform(p: Platform): Boolean = {
      val enabled = p match {
        case platform: BasePlatform =>
          platformEnabled(platform)
        case Platform.All =>
          true
      }
      enabled && a.platforms.exists(_.platform == p)
    }

    def isJvmOnly: Boolean = {
      config.jvmOnly || a.platforms.size == 1 && supportsPlatform(Platform.Jvm)
    }

    def nameOn(platform: Platform): String = {
      renderName(nameOn0(platform))
    }

    def nameOn0(platform: Platform): String = {
      platform match {
        case Platform.All =>
          a.name.value
        case x: BasePlatform if isJvmOnly =>
          if (x == Platform.Jvm) {
            a.name.value
          } else {
            throw new RuntimeException(s"JVM-only project do not support platform: $a / $platform")
          }
        case x: BasePlatform =>
          if (a.supportsPlatform(x)) {
            a.name.value + platformName(x).toUpperCase
          } else {
            throw new RuntimeException(s"Project do not support platform `$platform`: $a")
          }

      }
    }
  }

  protected def platformEnabled(penv: PlatformEnv): Boolean = platformEnabled(penv.platform)

  protected def platformEnabled(p: Platform.BasePlatform): Boolean = {
    p match {
      case Platform.Jvm =>
        config.jvm
      case Platform.Js =>
        config.js
      case Platform.Native =>
        config.native
    }
  }

  protected def platformName(p: Platform.BasePlatform): String = {
    p match {
      case Platform.Jvm =>
        "jvm"
      case Platform.Js =>
        "js"
      case Platform.Native =>
        "native"
    }
  }
}
