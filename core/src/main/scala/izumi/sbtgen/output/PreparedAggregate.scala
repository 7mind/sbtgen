package izumi.sbtgen.output

import izumi.sbtgen.model.{ArtifactId, Platform}

case class PreparedAggregate(id: ArtifactId, path: String, aggregatedNames: Seq[String], target: Platform)
