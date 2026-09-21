# sbtgen

SBT build generator for role-based projects, based on [Ammonite](https://ammonite.io) and [Coursier](https://get-coursier.io/).

Details: [Monorepo or Multirepo? Role-Based Repositories](https://blog.7mind.io/role-based-repositories.html).

See also: [role-based projects for .NET/C# and JS](https://github.com/CK-Build/CKli/)

## Quick Start

Create a file `sbtgen.sc` and paste the following code:

```scala
#!/bin/sh
coursier launch com.lihaoyi:ammonite_2.13.0:1.6.9 --fork -M ammonite.Main -- sbtgen.sc $*
exit
!#
import $ivy.`io.7mind.izumi.sbt::sbtgen:0.0.66`, izumi.sbtgen._, izumi.sbtgen.model._

val globalSettings = GlobalSettings(
  groupId = "my.org",
)

@main
def entrypoint(args: String*): Unit = {
  Entrypoint.main(MyProject.root, globalSettings, Seq("-o", ".") ++ args)
}

object Platforms {
  val jvm = PlatformEnv(
    platform = Platform.Jvm,
    language = Seq(ScalaVersion("2.13.3")),
    settings = Seq("scalacSettings" += "-Xlint:_"),
  )
}

object MyProject {
  val root = Project(
    name = ArtifactId("my-project"),
    aggregates = Seq(
      Aggregate(
        name = ArtifactId("my-lib"),
        artifacts = Seq(
          Artifact(
            name = ArtifactId("my-module-a"),
            libs = Seq.empty,
            depends = Seq.empty,
            platforms = Seq(Platforms.jvm),
            groups = Set(Group("groupA")),
          ),
          Artifact(
            name = ArtifactId("my-module-b"),
            libs = Seq.empty,
            depends = Seq.empty,
            platforms = Seq(Platforms.jvm),
            groups = Set(Group("groupB")),
          ),
        ),
      ),
    ),
  )
}
```

Install [Coursier](https://get-coursier.io) and launch the script:

```bash
chmod +x sbtgen.sc
./sbtgen.sc
```

Alternatively, you may launch it with [Ammonite](https://ammonite.io) if it's installed:

```bash
amm sbtgen.sc
```

This will generate `build.sbt` for `my-project` in current directory.

Use `./sbtgen.sc -u groupA` or `./sbtgen.sc -u groupB` to build only `my-module-a` or `my-module-b`

Use `./sbtgen.sc --help` for help:

```bash
$ ./sbtgen.sc --help
Error: Unknown option --help
sbtgen
Usage: sbtgen [options]

  --nojvm               disable jvm projects
  --js                  enable js projects
  --native              enable native projects
  --nta                 don't publish test artifacts
  -d, --debug           enable debug output
  -c, --compactify      deduplicate repetative settings
  -t, --isolate-tests   don't inherit test scopes
  -o, --output <value>  output directory
  -u, --use <value>     use only groups specified
Cannot parse commandline
```

## sbt 2.x

`sbt-izumi` is published for both sbt majors: sbt 1.x picks up `sbt-izumi_2.12_1.0`,
sbt 2.x picks up `sbt-izumi_sbt2_3`. `addSbtPlugin` resolves the right one, so nothing
changes in `project/plugins.sbt`.

To make `sbtgen` emit a build for sbt 2.x, set `sbtTarget` and a matching `sbtVersion`:

```scala
val globalSettings = GlobalSettings(
  groupId = "my.org",
  sbtVersion = Some("2.0.9"),
  sbtTarget = SbtTarget.Sbt2,
)
```

The two must agree, otherwise generation fails.

Differences in the generated output:

* Project definitions are emitted flat, rather than grouped into the anonymous-class
  holders used to stay under the JVM classfile size limit on sbt 1.x. Scala 3, which
  compiles `build.sbt` on sbt 2.x, infers `Object` instead of a structural refinement
  for `new { ... }`, so the holders' members would be unreachable.
* `rootPlugins` and `topLevelSettings` are attached to the root project instead of being
  emitted as bare statements, because sbt 2.x injects bare statements into *every*
  subproject.

Known limitations on sbt 2.x:

* Scala.js builds cannot use `sbt-scalajs-bundler` or `sbt-jsdependencies`, which have no
  sbt 2.x releases. Set `bundlerVersion = None` and `sbtJsDependenciesVersion = None`;
  generation fails otherwise rather than silently dropping them.
* Scala.js/Native also need plugin versions newer than the defaults this project pins, since
  the older ones were never published for sbt 2.x:

  ```scala
  scalaJsVersion = Version.VConst("1.22.0"),
  scalaNativeVersion = Version.VConst("0.5.12"),
  crossProjectVersion = Version.VConst("1.4.0"),
  ```
* `sbt-izumi` does not re-export `sbt-duplicates-finder` or `sbt-stats` there (no sbt 2.x
  releases). `sbt-dependency-tree` is in-sourced into sbt 2.x itself, so it is still available.
* `IzumiExposedTestScopesPlugin.itSettings` throws on sbt 2.x: the `IntegrationTest`
  configuration was removed, and integration tests are meant to be a separate subproject.

## IDE Support

Intellij has built-in support for Ammonite scripts, if it doesn't work go to `Preferences -> Languages and Frameworks -> Scala -> Worksheet` and change `Treat .sc files as:` to `Always Ammonite`

To enable syntax highlighting for the library, ensure that `sbtgen.sc` is opened as an Ammonite script – there should be a `Run script` button in the upper-left corner. Press it, after script finishes Intellij should prompt to include `$ivy` dependencies in the script into the project – this will enable full IntelliSense for the script.

## Example projects

Complete projects that use this tool:

* Izumi framework build:
  * https://github.com/7mind/izumi/blob/develop/project/Deps.sc

* `izumi-reflect` build:
  * https://github.com/zio/izumi-reflect/blob/master/project/Deps.sc
  
* D4S build:
  * https://github.com/PlayQ/d4s/blob/master/sbtgen.sc

* Test project:
  * sbtgen script: https://github.com/7mind/sbtgen/blob/develop/sbtgen/src/test/scala/izumi/sbtgen/TestProject.scala
  * generated project: https://github.com/7mind/sbtgen/blob/develop/test/js/build.sbt

## Contributors: how to publish

Use 

```bash
./copy-test-directory.sh
```

To deal with build diff failures if you changed the expected output of the DSL

To release new version via CI:

```bash
sbt +clean +test release
```

