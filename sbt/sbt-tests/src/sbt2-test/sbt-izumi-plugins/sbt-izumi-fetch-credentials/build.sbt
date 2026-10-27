enablePlugins(IzumiFetchPlugin)

name := "sbt-izumi-fetch-credentials"
ThisBuild / organization := "izumi.test.fetch"

credentials += Credentials(file("credentials.properties"))
resolvers += "private" at "https://repo.example.test/maven2"

lazy val checkFetchCredentials = taskKey[Unit]("Check that file credentials reach the fetch resolvers")

checkFetchCredentials := Def.uncached {
  val repository = izumi.sbt.plugins.optional.IzumiFetchPlugin.Keys.fetchResolvers.value
    .find(_.root.contains("repo.example.test"))
    .getOrElse(sys.error("private resolver is missing"))
  val authentication = repository.authentication.getOrElse(sys.error("private resolver has no authentication"))
  assert(authentication.user == "fetch-user", authentication)
  assert(authentication.passwordOpt.contains("fetch-pass"), authentication)
}
