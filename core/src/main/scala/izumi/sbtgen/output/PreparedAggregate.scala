package izumi.sbtgen.output

import izumi.sbtgen.model.{ArtifactId, Platform, Plugins}

case class PreparedAggregate(
                              id: ArtifactId,
                              pathPrefix: Seq[String],
                              aggregatedNames: Seq[String],
                              target: Platform,
                              plugins: Plugins,
                              isRoot: Boolean = false,
                              enableSharedSettings: Boolean = true,
                              dontIncludeInSuperAgg: Boolean = false,
                            )
