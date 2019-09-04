package izumi.sbtgen.output

import izumi.sbtgen.model.{ArtifactId, Platform}

case class PreparedAggregate(
                              id: ArtifactId,
                              pathPrefix: Seq[String],
                              aggregatedNames: Seq[String],
                              target: Platform,
                              isRoot: Boolean = false,
                            )
