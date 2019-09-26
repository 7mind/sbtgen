package izumi.sbtgen.impl

import izumi.sbtgen.model.{Aggregate, Artifact, ArtifactId}

trait WithProjectIndex {
  protected def index: Map[ArtifactId, Artifact]

  protected def makeIndex(projects: Seq[Aggregate]): Map[ArtifactId, Artifact] = {
    projects
      .flatMap(_.artifacts)
      .groupBy(_.name)
      .map {
        case (k, a) =>
          if (a.size > 1) {
            throw new RuntimeException(s"Duplicated names: $a")
          }
          k -> a.head
      }
  }
}
