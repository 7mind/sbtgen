package izumi.sbtgen

import izumi.sbtgen.model._

object TestDottyProject {

  val aggregates = Seq(
    Aggregate(
      name = ArtifactId("test-agg"),
      artifacts = Seq(Artifact(
        name = ArtifactId("test"),
        libs = Nil,
        depends = Nil,
        platforms = Seq(
          PlatformEnv(
            platform = Platform.Jvm,
            language = Seq(ScalaVersion("0.23.0-RC1")),
            settings = Defaults.CrossScalaSources
          ),
          PlatformEnv(
            platform = Platform.Js,
            language = Seq(ScalaVersion("0.23.0-RC1")),
            settings = Defaults.CrossScalaSources
          )
        )
      )),
    )
  )

  val project: Project = Project(
    name = ArtifactId("test-dotty"),
    aggregates = aggregates,
  )
}
