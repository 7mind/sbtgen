# sbtgen


## Publishing

```bash
sbt +clean +sonatypeBundleClean +publishSigned +sonatypeBundleRelease
```

## Usage examples

* Izumi framework build:
  * https://github.com/7mind/izumi/blob/develop/project/Deps.sc
  * https://github.com/7mind/izumi/blob/develop/sbtgen.sc
  
* Test project:
  * sbtgen script: https://github.com/7mind/sbtgen/blob/develop/sbtgen/src/test/scala/izumi/sbtgen/TestProject.scala
  * generated project: https://github.com/7mind/sbtgen/blob/develop/test/js/build.sbt
