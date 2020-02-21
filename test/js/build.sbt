import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}



disablePlugins(AssemblyPlugin)

lazy val `fundamentals-collections` = crossProject(JVMPlatform, JSPlatform).crossType(CrossType.Pure).in(file("fundamentals/fundamentals-collections"))
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %%% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %%% "scalatest" % "3.1.1" % Test
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .jvmSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .jsSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    coverageEnabled := false,
    scalaJSModuleKind := ModuleKind.CommonJSModule
  )
lazy val `fundamentals-collectionsJVM` = `fundamentals-collections`.jvm
  .disablePlugins(AssemblyPlugin)
lazy val `fundamentals-collectionsJS` = `fundamentals-collections`.js
  .disablePlugins(AssemblyPlugin)

lazy val `fundamentals-platform` = crossProject(JVMPlatform, JSPlatform).crossType(CrossType.Pure).in(file("fundamentals/fundamentals-platform"))
  .dependsOn(
    `fundamentals-collections` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %%% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %%% "scalatest" % "3.1.1" % Test
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .jvmSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .jsSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    coverageEnabled := false,
    scalaJSModuleKind := ModuleKind.CommonJSModule,
    npmDependencies in Compile ++= Seq(
      (  "hash.js",  "1.1.7")
    )
  )
lazy val `fundamentals-platformJVM` = `fundamentals-platform`.jvm
  .disablePlugins(AssemblyPlugin)
lazy val `fundamentals-platformJS` = `fundamentals-platform`.js
  .enablePlugins(ScalaJSBundlerPlugin)
  .disablePlugins(AssemblyPlugin)

lazy val `fundamentals-functional` = crossProject(JVMPlatform, JSPlatform).crossType(CrossType.Pure).in(file("fundamentals/fundamentals-functional"))
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %%% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %%% "scalatest" % "3.1.1" % Test
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .jvmSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .jsSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    coverageEnabled := false,
    scalaJSModuleKind := ModuleKind.CommonJSModule
  )
lazy val `fundamentals-functionalJVM` = `fundamentals-functional`.jvm
  .disablePlugins(AssemblyPlugin)
lazy val `fundamentals-functionalJS` = `fundamentals-functional`.js
  .disablePlugins(AssemblyPlugin)

lazy val `fundamentals-bio` = crossProject(JVMPlatform, JSPlatform).crossType(CrossType.Pure).in(file("fundamentals/fundamentals-bio"))
  .dependsOn(
    `fundamentals-functional` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %%% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %%% "scalatest" % "3.1.1" % Test,
      "org.typelevel" %%% "cats-core" % "2.0.0-RC1" % Optional,
      "org.typelevel" %%% "cats-effect" % "2.0.0-RC1" % Optional,
      "dev.zio" %%% "zio" % "1.0.0-RC11-1" % Optional
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .jvmSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .jsSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    coverageEnabled := false,
    scalaJSModuleKind := ModuleKind.CommonJSModule
  )
lazy val `fundamentals-bioJVM` = `fundamentals-bio`.jvm
  .disablePlugins(AssemblyPlugin)
lazy val `fundamentals-bioJS` = `fundamentals-bio`.js
  .disablePlugins(AssemblyPlugin)

lazy val `fundamentals-typesafe-config` = project.in(file("fundamentals/fundamentals-typesafe-config"))
  .dependsOn(
    `fundamentals-platformJVM` % "test->compile;compile->compile",
    `fundamentals-functionalJVM` % "test->compile;compile->compile",
    `fundamentals-collectionsJVM` % "test->compile;compile->compile",
    `fundamentals-reflectionJVM` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test,
      "com.typesafe" % "config" % "1.3.4",
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val `fundamentals-reflection` = crossProject(JVMPlatform, JSPlatform).crossType(CrossType.Pure).in(file("fundamentals/fundamentals-reflection"))
  .dependsOn(
    `fundamentals-platform` % "test->compile;compile->compile",
    `fundamentals-functional` % "test->compile;compile->compile",
    `fundamentals-collections` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %%% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %%% "scalatest" % "3.1.1" % Test,
      "io.suzaku" %%% "boopickle" % "1.3.1",
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .jvmSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .jsSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    coverageEnabled := false,
    scalaJSModuleKind := ModuleKind.CommonJSModule
  )
lazy val `fundamentals-reflectionJVM` = `fundamentals-reflection`.jvm
  .disablePlugins(AssemblyPlugin)
lazy val `fundamentals-reflectionJS` = `fundamentals-reflection`.js
  .disablePlugins(AssemblyPlugin)

lazy val `fundamentals-json-circe` = crossProject(JVMPlatform, JSPlatform).crossType(CrossType.Pure).in(file("fundamentals/fundamentals-json-circe"))
  .dependsOn(
    `fundamentals-platform` % "test->compile;compile->compile",
    `fundamentals-functional` % "test->compile;compile->compile",
    `fundamentals-collections` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %%% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %%% "scalatest" % "3.1.1" % Test,
      "io.circe" %%% "circe-core" % "0.12.0-RC4",
      "io.circe" %%% "circe-parser" % "0.12.0-RC4",
      "io.circe" %%% "circe-literal" % "0.12.0-RC4",
      "io.circe" %%% "circe-generic-extras" % "0.12.0-RC4",
      "io.circe" %%% "circe-derivation" % "0.12.0-M5"
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .jvmSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .jsSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    coverageEnabled := false,
    scalaJSModuleKind := ModuleKind.CommonJSModule
  )
lazy val `fundamentals-json-circeJVM` = `fundamentals-json-circe`.jvm
  .disablePlugins(AssemblyPlugin)
lazy val `fundamentals-json-circeJS` = `fundamentals-json-circe`.js
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %% "jawn-parser" % "0.14.2"
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val `distage-model` = project.in(file("distage/distage-model"))
  .dependsOn(
    `fundamentals-platformJVM` % "test->compile;compile->compile",
    `fundamentals-functionalJVM` % "test->compile;compile->compile",
    `fundamentals-collectionsJVM` % "test->compile;compile->compile",
    `fundamentals-bioJVM` % "test->compile;compile->compile",
    `fundamentals-reflectionJVM` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test,
      "org.typelevel" %% "cats-core" % "2.0.0-RC1" % Optional,
      "org.typelevel" %% "cats-effect" % "2.0.0-RC1" % Optional,
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val `distage-proxy-cglib` = project.in(file("distage/distage-proxy-cglib"))
  .dependsOn(
    `fundamentals-platformJVM` % "test->compile;compile->compile",
    `fundamentals-functionalJVM` % "test->compile;compile->compile",
    `fundamentals-collectionsJVM` % "test->compile;compile->compile",
    `distage-model` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test,
      "cglib" % "cglib-nodep" % "3.3.0" exclude("xxx", "yyy")
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val `distage-core` = project.in(file("distage/distage-core"))
  .dependsOn(
    `distage-model` % "test->compile;compile->compile",
    `distage-proxy-cglib` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test,
      "cglib" % "cglib-nodep" % "3.3.0" exclude("xxx", "yyy")
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val `distage-config` = project.in(file("distage/distage-config"))
  .dependsOn(
    `distage-model` % "test->compile;compile->compile",
    `fundamentals-typesafe-config` % "test->compile;compile->compile",
    `distage-core` % "test->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test,
      "com.typesafe" % "config" % "1.3.4"
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    fork in Test := true
  )
  .disablePlugins(AssemblyPlugin)

lazy val `distage-roles-api` = project.in(file("distage/distage-roles-api"))
  .dependsOn(
    `distage-model` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val `distage-plugins` = project.in(file("distage/distage-plugins"))
  .dependsOn(
    `distage-model` % "test->compile;compile->compile",
    `distage-core` % "test->compile,test",
    `distage-config` % "test->compile",
    `logstage-coreJVM` % "test->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test,
      "io.github.classgraph" % "classgraph" % "4.8.47"
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    fork in Test := true
  )
  .disablePlugins(AssemblyPlugin)

lazy val `distage-roles` = project.in(file("distage/distage-roles"))
  .dependsOn(
    `distage-roles-api` % "test->compile;compile->compile",
    `logstage-di` % "test->compile;compile->compile",
    `logstage-adapter-slf4j` % "test->compile;compile->compile",
    `logstage-rendering-circeJVM` % "test->compile;compile->compile",
    `distage-core` % "test->test;compile->compile",
    `distage-plugins` % "test->test;compile->compile",
    `distage-config` % "test->test;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test,
      "org.typelevel" %% "cats-core" % "2.0.0-RC1" % Optional,
      "org.typelevel" %% "cats-effect" % "2.0.0-RC1" % Optional,
      "dev.zio" %% "zio" % "1.0.0-RC11-1" % Optional
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val `distage-static` = project.in(file("distage/distage-static"))
  .dependsOn(
    `distage-core` % "test->test;compile->compile",
    `distage-roles` % "test->compile,test"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test,
      "com.chuusai" %% "shapeless" % "2.3.3"
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val `distage-testkit` = project.in(file("distage/distage-testkit"))
  .dependsOn(
    `distage-config` % "test->compile;compile->compile",
    `distage-roles` % "test->compile;compile->compile",
    `logstage-di` % "test->compile;compile->compile",
    `distage-core` % "test->test;compile->compile",
    `distage-plugins` % "test->test;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test,
      "org.scalatest" %% "scalatest" % "3.1.1",
      "org.typelevel" %% "cats-core" % "2.0.0-RC1" % Test,
      "org.typelevel" %% "cats-effect" % "2.0.0-RC1" % Test,
      "dev.zio" %% "zio" % "1.0.0-RC11-1" % Test
    )
  )
  .settings(
    organization := "io.7mind",
    classLoaderLayeringStrategy in Test := ClassLoaderLayeringStrategy.Flat,
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val `logstage-api` = crossProject(JVMPlatform, JSPlatform).crossType(CrossType.Pure).in(file("logstage/logstage-api"))
  .dependsOn(
    `fundamentals-reflection` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %%% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %%% "scalatest" % "3.1.1" % Test,
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided,
      "io.github.cquiroz" %%% "scala-java-time" % "2.0.0-RC3"
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .jvmSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .jsSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    coverageEnabled := false,
    scalaJSModuleKind := ModuleKind.CommonJSModule
  )
lazy val `logstage-apiJVM` = `logstage-api`.jvm
  .disablePlugins(AssemblyPlugin)
lazy val `logstage-apiJS` = `logstage-api`.js
  .disablePlugins(AssemblyPlugin)

lazy val `logstage-core` = crossProject(JVMPlatform, JSPlatform).crossType(CrossType.Pure).in(file("logstage/logstage-core"))
  .dependsOn(
    `fundamentals-bio` % "test->compile;compile->compile",
    `logstage-api` % "test->test;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %%% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %%% "scalatest" % "3.1.1" % Test,
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided,
      "org.typelevel" %%% "cats-core" % "2.0.0-RC1" % Optional,
      "dev.zio" %%% "zio" % "1.0.0-RC11-1" % Optional,
      "org.typelevel" %%% "cats-core" % "2.0.0-RC1" % Test,
      "org.typelevel" %%% "cats-effect" % "2.0.0-RC1" % Test,
      "dev.zio" %%% "zio" % "1.0.0-RC11-1" % Test
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .jvmSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .jsSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    coverageEnabled := false,
    scalaJSModuleKind := ModuleKind.CommonJSModule
  )
lazy val `logstage-coreJVM` = `logstage-core`.jvm
  .disablePlugins(AssemblyPlugin)
lazy val `logstage-coreJS` = `logstage-core`.js
  .disablePlugins(AssemblyPlugin)

lazy val `logstage-rendering-circe` = crossProject(JVMPlatform, JSPlatform).crossType(CrossType.Pure).in(file("logstage/logstage-rendering-circe"))
  .dependsOn(
    `fundamentals-json-circe` % "test->compile;compile->compile",
    `logstage-core` % "test->test;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %%% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %%% "scalatest" % "3.1.1" % Test
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .jvmSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .jsSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    coverageEnabled := false,
    scalaJSModuleKind := ModuleKind.CommonJSModule
  )
lazy val `logstage-rendering-circeJVM` = `logstage-rendering-circe`.jvm
  .disablePlugins(AssemblyPlugin)
lazy val `logstage-rendering-circeJS` = `logstage-rendering-circe`.js
  .disablePlugins(AssemblyPlugin)

lazy val `logstage-di` = project.in(file("logstage/logstage-di"))
  .dependsOn(
    `logstage-config` % "test->compile;compile->compile",
    `distage-config` % "test->compile;compile->compile",
    `distage-model` % "test->compile;compile->compile",
    `distage-core` % "test->compile",
    `logstage-coreJVM` % "test->test;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val `logstage-config` = project.in(file("logstage/logstage-config"))
  .dependsOn(
    `fundamentals-typesafe-config` % "test->compile;compile->compile",
    `logstage-coreJVM` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val `logstage-adapter-slf4j` = project.in(file("logstage/logstage-adapter-slf4j"))
  .dependsOn(
    `logstage-coreJVM` % "test->test;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test,
      "org.slf4j" % "slf4j-api" % "1.7.28"
    )
  )
  .settings(
    organization := "io.7mind",
    compileOrder in Compile := CompileOrder.Mixed,
    compileOrder in Test := CompileOrder.Mixed,
    classLoaderLayeringStrategy in Test := ClassLoaderLayeringStrategy.Flat,
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val `logstage-sink-slf4j` = project.in(file("logstage/logstage-sink-slf4j"))
  .dependsOn(
    `logstage-apiJVM` % "test->compile;compile->compile",
    `logstage-coreJVM` % "test->compile,test"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test,
      "org.slf4j" % "slf4j-api" % "1.7.28",
      "org.slf4j" % "slf4j-simple" % "1.7.28" % Test
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val `idealingua-v1-model` = crossProject(JVMPlatform, JSPlatform).crossType(CrossType.Pure).in(file("idealingua-v1/idealingua-v1-model"))
  .dependsOn(
    `fundamentals-platform` % "test->compile;compile->compile",
    `fundamentals-functional` % "test->compile;compile->compile",
    `fundamentals-collections` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %%% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %%% "scalatest" % "3.1.1" % Test
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .jvmSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .jsSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    coverageEnabled := false,
    scalaJSModuleKind := ModuleKind.CommonJSModule
  )
lazy val `idealingua-v1-modelJVM` = `idealingua-v1-model`.jvm
  .disablePlugins(AssemblyPlugin)
lazy val `idealingua-v1-modelJS` = `idealingua-v1-model`.js
  .disablePlugins(AssemblyPlugin)

lazy val `idealingua-v1-core` = crossProject(JVMPlatform, JSPlatform).crossType(CrossType.Pure).in(file("idealingua-v1/idealingua-v1-core"))
  .dependsOn(
    `fundamentals-platform` % "test->compile;compile->compile",
    `fundamentals-functional` % "test->compile;compile->compile",
    `fundamentals-collections` % "test->compile;compile->compile",
    `idealingua-v1-model` % "test->compile;compile->compile",
    `fundamentals-reflection` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %%% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %%% "scalatest" % "3.1.1" % Test,
      "com.lihaoyi" %%% "fastparse" % "2.1.3"
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .jvmSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .jsSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    coverageEnabled := false,
    scalaJSModuleKind := ModuleKind.CommonJSModule
  )
lazy val `idealingua-v1-coreJVM` = `idealingua-v1-core`.jvm
  .disablePlugins(AssemblyPlugin)
lazy val `idealingua-v1-coreJS` = `idealingua-v1-core`.js
  .disablePlugins(AssemblyPlugin)

lazy val `idealingua-v1-runtime-rpc-scala` = crossProject(JVMPlatform, JSPlatform).crossType(CrossType.Pure).in(file("idealingua-v1/idealingua-v1-runtime-rpc-scala"))
  .dependsOn(
    `fundamentals-platform` % "test->compile;compile->compile",
    `fundamentals-functional` % "test->compile;compile->compile",
    `fundamentals-collections` % "test->compile;compile->compile",
    `fundamentals-bio` % "test->compile;compile->compile",
    `fundamentals-json-circe` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %%% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %%% "scalatest" % "3.1.1" % Test,
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided,
      "org.typelevel" %%% "cats-core" % "2.0.0-RC1",
      "org.typelevel" %%% "cats-effect" % "2.0.0-RC1",
      "dev.zio" %%% "zio" % "1.0.0-RC11-1",
      "dev.zio" %%% "zio-interop-cats" % "2.0.0.0-RC2"
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .jvmSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .jsSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    coverageEnabled := false,
    scalaJSModuleKind := ModuleKind.CommonJSModule
  )
lazy val `idealingua-v1-runtime-rpc-scalaJVM` = `idealingua-v1-runtime-rpc-scala`.jvm
  .disablePlugins(AssemblyPlugin)
lazy val `idealingua-v1-runtime-rpc-scalaJS` = `idealingua-v1-runtime-rpc-scala`.js
  .disablePlugins(AssemblyPlugin)

lazy val `idealingua-v1-runtime-rpc-http4s` = project.in(file("idealingua-v1/idealingua-v1-runtime-rpc-http4s"))
  .dependsOn(
    `idealingua-v1-runtime-rpc-scalaJVM` % "test->compile;compile->compile",
    `logstage-coreJVM` % "test->compile;compile->compile",
    `logstage-adapter-slf4j` % "test->compile;compile->compile",
    `idealingua-v1-test-defs` % "test->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test,
      "org.http4s" %% "http4s-dsl" % "0.21.0-M4",
      "org.http4s" %% "http4s-circe" % "0.21.0-M4",
      "org.http4s" %% "http4s-blaze-server" % "0.21.0-M4",
      "org.http4s" %% "http4s-blaze-client" % "0.21.0-M4",
      "org.asynchttpclient" % "async-http-client" % "2.10.1"
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val `idealingua-v1-transpilers` = crossProject(JVMPlatform, JSPlatform).crossType(CrossType.Pure).in(file("idealingua-v1/idealingua-v1-transpilers"))
  .dependsOn(
    `fundamentals-platform` % "test->compile;compile->compile",
    `fundamentals-functional` % "test->compile;compile->compile",
    `fundamentals-collections` % "test->compile;compile->compile",
    `fundamentals-json-circe` % "test->compile;compile->compile",
    `idealingua-v1-core` % "test->compile;compile->compile",
    `idealingua-v1-runtime-rpc-scala` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %%% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %%% "scalatest" % "3.1.1" % Test,
      "org.scala-lang.modules" %%% "scala-xml" % "1.2.0",
      "org.scalameta" %%% "scalameta" % "4.2.3"
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .jvmSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    fork in Test := true
  )
  .jsSettings(
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    coverageEnabled := false,
    scalaJSModuleKind := ModuleKind.CommonJSModule
  )
lazy val `idealingua-v1-transpilersJVM` = `idealingua-v1-transpilers`.jvm
  .dependsOn(
    `idealingua-v1-test-defs` % "test->compile",
    `idealingua-v1-runtime-rpc-typescript` % "test->compile",
    `idealingua-v1-runtime-rpc-go` % "test->compile",
    `idealingua-v1-runtime-rpc-csharp` % "test->compile"
  )
  .disablePlugins(AssemblyPlugin)
lazy val `idealingua-v1-transpilersJS` = `idealingua-v1-transpilers`.js
  .disablePlugins(AssemblyPlugin)

lazy val `idealingua-v1-test-defs` = project.in(file("idealingua-v1/idealingua-v1-test-defs"))
  .dependsOn(
    `idealingua-v1-runtime-rpc-scalaJVM` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val `idealingua-v1-runtime-rpc-typescript` = project.in(file("idealingua-v1/idealingua-v1-runtime-rpc-typescript"))
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val `idealingua-v1-runtime-rpc-go` = project.in(file("idealingua-v1/idealingua-v1-runtime-rpc-go"))
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val `idealingua-v1-runtime-rpc-csharp` = project.in(file("idealingua-v1/idealingua-v1-runtime-rpc-csharp"))
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test
    )
  )
  .settings(
    organization := "io.7mind",
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val `idealingua-v1-compiler` = project.in(file("idealingua-v1/idealingua-v1-compiler"))
  .dependsOn(
    `idealingua-v1-transpilersJVM` % "test->compile;compile->compile",
    `idealingua-v1-runtime-rpc-scalaJVM` % "test->compile;compile->compile",
    `idealingua-v1-runtime-rpc-typescript` % "test->compile;compile->compile",
    `idealingua-v1-runtime-rpc-go` % "test->compile;compile->compile",
    `idealingua-v1-runtime-rpc-csharp` % "test->compile;compile->compile",
    `idealingua-v1-test-defs` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test,
      "com.typesafe" % "config" % "1.3.4"
    )
  )
  .settings(
    organization := "io.7mind",
    mainClass in assembly := Some("izumi.idealingua.compiler.CommandlineIDLCompiler"),
    assemblyMergeStrategy in assembly := {
          // FIXME: workaround for https://github.com/zio/interop-cats/issues/16
          case path if path.contains("zio/BuildInfo$.class") =>
            MergeStrategy.last
          case p =>
            (assemblyMergeStrategy in assembly).value(p)
    },
    artifact in (Compile, assembly) := {
          val art = (artifact in(Compile, assembly)).value
          art.withClassifier(Some("assembly"))
    },
    addArtifact(artifact in(Compile, assembly), assembly),
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .enablePlugins(AssemblyPlugin)

lazy val `microsite` = project.in(file("doc/microsite"))
  .dependsOn(
    `fundamentals-collectionsJVM` % "test->compile;compile->compile",
    `fundamentals-platformJVM` % "test->compile;compile->compile",
    `fundamentals-functionalJVM` % "test->compile;compile->compile",
    `fundamentals-bioJVM` % "test->compile;compile->compile",
    `fundamentals-typesafe-config` % "test->compile;compile->compile",
    `fundamentals-reflectionJVM` % "test->compile;compile->compile",
    `fundamentals-json-circeJVM` % "test->compile;compile->compile",
    `distage-model` % "test->compile;compile->compile",
    `distage-proxy-cglib` % "test->compile;compile->compile",
    `distage-core` % "test->compile;compile->compile",
    `distage-config` % "test->compile;compile->compile",
    `distage-roles-api` % "test->compile;compile->compile",
    `distage-plugins` % "test->compile;compile->compile",
    `distage-roles` % "test->compile;compile->compile",
    `distage-static` % "test->compile;compile->compile",
    `distage-testkit` % "test->compile;compile->compile",
    `logstage-apiJVM` % "test->compile;compile->compile",
    `logstage-coreJVM` % "test->compile;compile->compile",
    `logstage-rendering-circeJVM` % "test->compile;compile->compile",
    `logstage-di` % "test->compile;compile->compile",
    `logstage-config` % "test->compile;compile->compile",
    `logstage-adapter-slf4j` % "test->compile;compile->compile",
    `logstage-sink-slf4j` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test,
      "org.typelevel" %% "cats-core" % "2.0.0-RC1",
      "org.typelevel" %% "cats-effect" % "2.0.0-RC1",
      "dev.zio" %% "zio" % "1.0.0-RC11-1",
      "dev.zio" %% "zio-interop-cats" % "2.0.0.0-RC2",
      "org.http4s" %% "http4s-dsl" % "0.21.0-M4",
      "org.http4s" %% "http4s-circe" % "0.21.0-M4",
      "org.http4s" %% "http4s-blaze-server" % "0.21.0-M4",
      "org.http4s" %% "http4s-blaze-client" % "0.21.0-M4"
    )
  )
  .settings(
    organization := "io.7mind",
    crossScalaVersions := Seq(
      "2.12.8"
    ),
    scalaVersion := crossScalaVersions.value.head,
    coverageEnabled := false,
    skip in publish := true,
    DocKeys.prefix := {if (isSnapshot.value) {
                "latest/snapshot"
              } else {
                "latest/release"
              }},
    previewFixedPort := Some(9999),
    git.remoteRepo := "git@github.com:7mind/izumi-microsite.git",
    classLoaderLayeringStrategy in Compile := ClassLoaderLayeringStrategy.Flat,
    mdocIn := baseDirectory.value / "src/main/tut",
    sourceDirectory in Paradox := mdocOut.value,
    mdocExtraArguments ++= Seq(
      " --no-link-hygiene"
    ),
    mappings in SitePlugin.autoImport.makeSite := {
                (mappings in SitePlugin.autoImport.makeSite)
                  .dependsOn(mdoc.toTask(" "))
                  .value
              },
    version in Paradox := version.value,
    ParadoxMaterialThemePlugin.paradoxMaterialThemeSettings(Paradox),
    addMappingsToSiteDir(mappings in(ScalaUnidoc, packageDoc), siteSubdirName in ScalaUnidoc),
    unidocProjectFilter in(ScalaUnidoc, unidoc) := inAggregates(`izumi-jvm`, transitive=true),
    paradoxMaterialTheme in Paradox ~= {
                _.withCopyright("7mind.io")
                  .withRepository(uri("https://github.com/7mind/izumi"))
                //        .withColor("222", "434343")
              },
    siteSubdirName in ScalaUnidoc := s"${DocKeys.prefix.value}/api",
    siteSubdirName in Paradox := s"${DocKeys.prefix.value}/doc",
    paradoxProperties ++= Map(
                "scaladoc.izumi.base_url" -> s"/${DocKeys.prefix.value}/api/com/github/pshirshov/",
                "scaladoc.base_url" -> s"/${DocKeys.prefix.value}/api/",
                "izumi.version" -> version.value,
              ),
    excludeFilter in ghpagesCleanSite :=
                new FileFilter {
                  def accept(f: File): Boolean = {
                    (f.toPath.startsWith(ghpagesRepository.value.toPath.resolve("latest")) && !f.toPath.startsWith(ghpagesRepository.value.toPath.resolve(DocKeys.prefix.value))) ||
                      (ghpagesRepository.value / "CNAME").getCanonicalPath == f.getCanonicalPath ||
                      (ghpagesRepository.value / ".nojekyll").getCanonicalPath == f.getCanonicalPath ||
                      (ghpagesRepository.value / "index.html").getCanonicalPath == f.getCanonicalPath ||
                      (ghpagesRepository.value / "README.md").getCanonicalPath == f.getCanonicalPath ||
                      f.toPath.startsWith((ghpagesRepository.value / "media").toPath) ||
                      f.toPath.startsWith((ghpagesRepository.value / "v0.5.50-SNAPSHOT").toPath)
                  }
                },
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.8"
    )
  )
  .enablePlugins(ScalaUnidocPlugin, ParadoxSitePlugin, SitePlugin, GhpagesPlugin, ParadoxMaterialThemePlugin, PreprocessPlugin, MdocPlugin)
  .disablePlugins(ScoverageSbtPlugin, AssemblyPlugin)

lazy val `sbt-izumi-deps` = project.in(file("sbt-plugins/sbt-izumi-deps"))
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.1" % Test
    )
  )
  .settings(
    organization := "io.7mind",
    sbtPlugin := true,
    withBuildInfo("izumi.sbt.deps", "Izumi"),
    testOptions in Test += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.12.8") => Seq(
        "-Xsource:2.13",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Ypartial-unification",
        "-Yno-adapted-args",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-opt-warnings:_",
        "-Ywarn-extra-implicit",
        "-Ywarn-unused:_",
        "-Ywarn-adapted-args",
        "-Ywarn-dead-code",
        "-Ywarn-inaccessible",
        "-Ywarn-infer-any",
        "-Ywarn-nullary-override",
        "-Ywarn-nullary-unit",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, "2.13.0") => Seq(
        "-Xlint:_,-eta-sam",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wunused:_",
        "-Wvalue-discard",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    )
  )
  .disablePlugins(AssemblyPlugin)

lazy val `fundamentals` = (project in file(".agg/fundamentals-fundamentals"))
  .settings(
    skip in publish := true,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `fundamentals-collectionsJVM`,
    `fundamentals-collectionsJS`,
    `fundamentals-platformJVM`,
    `fundamentals-platformJS`,
    `fundamentals-functionalJVM`,
    `fundamentals-functionalJS`,
    `fundamentals-bioJVM`,
    `fundamentals-bioJS`,
    `fundamentals-typesafe-config`,
    `fundamentals-reflectionJVM`,
    `fundamentals-reflectionJS`,
    `fundamentals-json-circeJVM`,
    `fundamentals-json-circeJS`
  )

lazy val `fundamentals-jvm` = (project in file(".agg/fundamentals-fundamentals-jvm"))
  .settings(
    skip in publish := true,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `fundamentals-collectionsJVM`,
    `fundamentals-platformJVM`,
    `fundamentals-functionalJVM`,
    `fundamentals-bioJVM`,
    `fundamentals-typesafe-config`,
    `fundamentals-reflectionJVM`,
    `fundamentals-json-circeJVM`
  )

lazy val `fundamentals-js` = (project in file(".agg/fundamentals-fundamentals-js"))
  .settings(
    skip in publish := true,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `fundamentals-collectionsJS`,
    `fundamentals-platformJS`,
    `fundamentals-functionalJS`,
    `fundamentals-bioJS`,
    `fundamentals-typesafe-config`,
    `fundamentals-reflectionJS`,
    `fundamentals-json-circeJS`
  )

lazy val `distage` = (project in file(".agg/distage-distage"))
  .settings(
    skip in publish := true,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `distage-model`,
    `distage-proxy-cglib`,
    `distage-core`,
    `distage-config`,
    `distage-roles-api`,
    `distage-plugins`,
    `distage-roles`,
    `distage-static`,
    `distage-testkit`
  )

lazy val `distage-jvm` = (project in file(".agg/distage-distage-jvm"))
  .settings(
    skip in publish := true,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `distage-model`,
    `distage-proxy-cglib`,
    `distage-core`,
    `distage-config`,
    `distage-roles-api`,
    `distage-plugins`,
    `distage-roles`,
    `distage-static`,
    `distage-testkit`
  )

lazy val `distage-js` = (project in file(".agg/distage-distage-js"))
  .settings(
    skip in publish := true,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `distage-model`,
    `distage-proxy-cglib`,
    `distage-core`,
    `distage-config`,
    `distage-roles-api`,
    `distage-plugins`,
    `distage-roles`,
    `distage-static`,
    `distage-testkit`
  )

lazy val `logstage` = (project in file(".agg/logstage-logstage"))
  .settings(
    skip in publish := true,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `logstage-apiJVM`,
    `logstage-apiJS`,
    `logstage-coreJVM`,
    `logstage-coreJS`,
    `logstage-rendering-circeJVM`,
    `logstage-rendering-circeJS`,
    `logstage-di`,
    `logstage-config`,
    `logstage-adapter-slf4j`,
    `logstage-sink-slf4j`
  )

lazy val `logstage-jvm` = (project in file(".agg/logstage-logstage-jvm"))
  .settings(
    skip in publish := true,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `logstage-apiJVM`,
    `logstage-coreJVM`,
    `logstage-rendering-circeJVM`,
    `logstage-di`,
    `logstage-config`,
    `logstage-adapter-slf4j`,
    `logstage-sink-slf4j`
  )

lazy val `logstage-js` = (project in file(".agg/logstage-logstage-js"))
  .settings(
    skip in publish := true,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `logstage-apiJS`,
    `logstage-coreJS`,
    `logstage-rendering-circeJS`,
    `logstage-di`,
    `logstage-config`,
    `logstage-adapter-slf4j`,
    `logstage-sink-slf4j`
  )

lazy val `idealingua` = (project in file(".agg/idealingua-v1-idealingua"))
  .settings(
    skip in publish := true,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `idealingua-v1-modelJVM`,
    `idealingua-v1-modelJS`,
    `idealingua-v1-coreJVM`,
    `idealingua-v1-coreJS`,
    `idealingua-v1-runtime-rpc-scalaJVM`,
    `idealingua-v1-runtime-rpc-scalaJS`,
    `idealingua-v1-runtime-rpc-http4s`,
    `idealingua-v1-transpilersJVM`,
    `idealingua-v1-transpilersJS`,
    `idealingua-v1-test-defs`,
    `idealingua-v1-runtime-rpc-typescript`,
    `idealingua-v1-runtime-rpc-go`,
    `idealingua-v1-runtime-rpc-csharp`,
    `idealingua-v1-compiler`
  )

lazy val `idealingua-jvm` = (project in file(".agg/idealingua-v1-idealingua-jvm"))
  .settings(
    skip in publish := true,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `idealingua-v1-modelJVM`,
    `idealingua-v1-coreJVM`,
    `idealingua-v1-runtime-rpc-scalaJVM`,
    `idealingua-v1-runtime-rpc-http4s`,
    `idealingua-v1-transpilersJVM`,
    `idealingua-v1-test-defs`,
    `idealingua-v1-runtime-rpc-typescript`,
    `idealingua-v1-runtime-rpc-go`,
    `idealingua-v1-runtime-rpc-csharp`,
    `idealingua-v1-compiler`
  )

lazy val `idealingua-js` = (project in file(".agg/idealingua-v1-idealingua-js"))
  .settings(
    skip in publish := true,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `idealingua-v1-modelJS`,
    `idealingua-v1-coreJS`,
    `idealingua-v1-runtime-rpc-scalaJS`,
    `idealingua-v1-runtime-rpc-http4s`,
    `idealingua-v1-transpilersJS`,
    `idealingua-v1-test-defs`,
    `idealingua-v1-runtime-rpc-typescript`,
    `idealingua-v1-runtime-rpc-go`,
    `idealingua-v1-runtime-rpc-csharp`,
    `idealingua-v1-compiler`
  )

lazy val `doc` = (project in file(".agg/doc-doc"))
  .settings(
    skip in publish := true,
    crossScalaVersions := Seq(
      "2.12.8"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `microsite`
  )

lazy val `doc-jvm` = (project in file(".agg/doc-doc-jvm"))
  .settings(
    skip in publish := true,
    crossScalaVersions := Seq(
      "2.12.8"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `microsite`
  )

lazy val `doc-js` = (project in file(".agg/doc-doc-js"))
  .settings(
    skip in publish := true,
    crossScalaVersions := Seq(
      "2.12.8"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `microsite`
  )

lazy val `sbt-plugins` = (project in file(".agg/sbt-plugins-sbt-plugins"))
  .settings(
    skip in publish := true
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `sbt-izumi-deps`
  )

lazy val `sbt-plugins-jvm` = (project in file(".agg/sbt-plugins-sbt-plugins-jvm"))
  .settings(
    skip in publish := true
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `sbt-izumi-deps`
  )

lazy val `sbt-plugins-js` = (project in file(".agg/sbt-plugins-sbt-plugins-js"))
  .settings(
    skip in publish := true
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `sbt-izumi-deps`
  )

lazy val `izumi-jvm` = (project in file(".agg/.agg-jvm"))
  .settings(
    skip in publish := true,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `fundamentals-jvm`,
    `distage-jvm`,
    `logstage-jvm`,
    `idealingua-jvm`
  )

lazy val `izumi-js` = (project in file(".agg/.agg-js"))
  .settings(
    skip in publish := true,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `fundamentals-js`,
    `distage-js`,
    `logstage-js`,
    `idealingua-js`
  )

lazy val `izumi` = (project in file("."))
  .settings(
    skip in publish := true,
    publishMavenStyle in ThisBuild := true,
    scalacOptions in ThisBuild ++= Seq(
      "-encoding",
      "UTF-8",
      "-target:jvm-1.8",
      "-feature",
      "-unchecked",
      "-deprecation",
      "-language:higherKinds",
      "-explaintypes"
    ),
    javacOptions in ThisBuild ++= Seq(
      "-encoding",
      "UTF-8",
      "-source",
      "1.8",
      "-target",
      "1.8",
      "-deprecation",
      "-parameters",
      "-Xlint:all",
      "-XDignore.symbol.file"
    ),
    scalacOptions in ThisBuild ++= Seq(
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:sbt-version=${sbtVersion.value}"
    ),
    crossScalaVersions := Nil,
    scalaVersion := "2.12.9",
    organization in ThisBuild := "io.7mind.izumi",
    homepage in ThisBuild := Some(url("https://izumi.7mind.io")),
    licenses in ThisBuild := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php")),
    developers in ThisBuild := List(
              Developer(id = "7mind", name = "Septimal Mind", url = url("https://github.com/7mind"), email = "team@7mind.io"),
            ),
    scmInfo in ThisBuild := Some(ScmInfo(url("https://github.com/7mind/izumi"), "scm:git:https://github.com/7mind/izumi.git")),
    scalacOptions in ThisBuild += s"-Xmacro-settings:product-version=${version.value}",
    scalacOptions in ThisBuild += s"-Xmacro-settings:product-group=${organization.value}",
    scalacOptions in ThisBuild += s"-Xmacro-settings:sbt-version=${sbtVersion.value}",
    scalacOptions in ThisBuild += s"-Xmacro-settings:scala-version=${scalaVersion.value}",
    scalacOptions in ThisBuild += """-Xmacro-settings:scalatest-version=3.0.8""",
    scalacOptions in ThisBuild += """-Xmacro-settings:scala-versions=2.12.9:2.13.0"""
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `fundamentals`,
    `distage`,
    `logstage`,
    `idealingua`
  )
