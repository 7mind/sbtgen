

disablePlugins(AssemblyPlugin)

lazy val `fundamentals-collections` = project.in(file("fundamentals/fundamentals-collections"))
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `fundamentals-platform` = project.in(file("fundamentals/fundamentals-platform"))
  .dependsOn(
    `fundamentals-collections` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `fundamentals-functional` = project.in(file("fundamentals/fundamentals-functional"))
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `fundamentals-bio` = project.in(file("fundamentals/fundamentals-bio"))
  .dependsOn(
    `fundamentals-functional` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
      "org.typelevel" %% "cats-core" % "2.0.0-RC1" % Optional,
      "org.typelevel" %% "cats-effect" % "2.0.0-RC1" % Optional,
      "dev.zio" %% "zio" % "1.0.0-RC11-1" % Optional
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `fundamentals-typesafe-config` = project.in(file("fundamentals/fundamentals-typesafe-config"))
  .dependsOn(
    `fundamentals-platform` % "test->compile;compile->compile",
    `fundamentals-functional` % "test->compile;compile->compile",
    `fundamentals-collections` % "test->compile;compile->compile",
    `fundamentals-reflection` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
      "com.typesafe" % "config" % "1.3.4"
    ),
    libraryDependencies ++= { if (scalaVersion.value.startsWith("2.")) Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided
    ) else Seq.empty },
    libraryDependencies ++= {
      val version = scalaVersion.value
      if (version.startsWith("0.") || version.startsWith("3.")) {
        Seq(
          "ch.epfl.lamp" %% "tasty-reflect" % scalaVersion.value % Provided
        )
      } else Seq.empty
    }
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `fundamentals-reflection` = project.in(file("fundamentals/fundamentals-reflection"))
  .dependsOn(
    `fundamentals-platform` % "test->compile;compile->compile",
    `fundamentals-functional` % "test->compile;compile->compile",
    `fundamentals-collections` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
      "io.suzaku" %% "boopickle" % "1.3.1"
    ),
    libraryDependencies ++= { if (scalaVersion.value.startsWith("2.")) Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided
    ) else Seq.empty },
    libraryDependencies ++= {
      val version = scalaVersion.value
      if (version.startsWith("0.") || version.startsWith("3.")) {
        Seq(
          "ch.epfl.lamp" %% "tasty-reflect" % scalaVersion.value % Provided
        )
      } else Seq.empty
    }
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `fundamentals-json-circe` = project.in(file("fundamentals/fundamentals-json-circe"))
  .dependsOn(
    `fundamentals-platform` % "test->compile;compile->compile",
    `fundamentals-functional` % "test->compile;compile->compile",
    `fundamentals-collections` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
      "io.circe" %% "circe-core" % "0.12.0-RC4",
      "io.circe" %% "circe-parser" % "0.12.0-RC4",
      "io.circe" %% "circe-literal" % "0.12.0-RC4",
      "io.circe" %% "circe-generic-extras" % "0.12.0-RC4",
      "io.circe" %% "circe-derivation" % "0.12.0-M5"
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `distage-model` = project.in(file("distage/distage-model"))
  .dependsOn(
    `fundamentals-platform` % "test->compile;compile->compile",
    `fundamentals-functional` % "test->compile;compile->compile",
    `fundamentals-collections` % "test->compile;compile->compile",
    `fundamentals-bio` % "test->compile;compile->compile",
    `fundamentals-reflection` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
      "org.typelevel" %% "cats-core" % "2.0.0-RC1" % Optional,
      "org.typelevel" %% "cats-effect" % "2.0.0-RC1" % Optional
    ),
    libraryDependencies ++= { if (scalaVersion.value.startsWith("2.")) Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided
    ) else Seq.empty },
    libraryDependencies ++= {
      val version = scalaVersion.value
      if (version.startsWith("0.") || version.startsWith("3.")) {
        Seq(
          "ch.epfl.lamp" %% "tasty-reflect" % scalaVersion.value % Provided
        )
      } else Seq.empty
    }
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `distage-proxy-cglib` = project.in(file("distage/distage-proxy-cglib"))
  .dependsOn(
    `fundamentals-platform` % "test->compile;compile->compile",
    `fundamentals-functional` % "test->compile;compile->compile",
    `fundamentals-collections` % "test->compile;compile->compile",
    `distage-model` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
      "cglib" % "cglib-nodep" % "3.3.0" exclude ("xxx", "yyy")
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
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
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
      "cglib" % "cglib-nodep" % "3.3.0" exclude ("xxx", "yyy")
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
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
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
      "com.typesafe" % "config" % "1.3.4"
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    Test / fork := true,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
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
      "org.scalatest" %% "scalatest" % "3.1.2" % Test
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `distage-plugins` = project.in(file("distage/distage-plugins"))
  .dependsOn(
    `distage-model` % "test->compile;compile->compile",
    `distage-core` % "test->compile,test",
    `distage-config` % "test->compile",
    `logstage-core` % "test->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
      "io.github.classgraph" % "classgraph" % "4.8.47"
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    Test / fork := true,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `distage-roles` = project.in(file("distage/distage-roles"))
  .dependsOn(
    `distage-roles-api` % "test->compile;compile->compile",
    `logstage-di` % "test->compile;compile->compile",
    `logstage-adapter-slf4j` % "test->compile;compile->compile",
    `logstage-rendering-circe` % "test->compile;compile->compile",
    `distage-core` % "test->test;compile->compile",
    `distage-plugins` % "test->test;compile->compile",
    `distage-config` % "test->test;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
      "org.typelevel" %% "cats-core" % "2.0.0-RC1" % Optional,
      "org.typelevel" %% "cats-effect" % "2.0.0-RC1" % Optional,
      "dev.zio" %% "zio" % "1.0.0-RC11-1" % Optional
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
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
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
      "com.chuusai" %% "shapeless" % "2.3.3"
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
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
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
      "org.scalatest" %% "scalatest" % "3.1.2",
      "org.typelevel" %% "cats-core" % "2.0.0-RC1" % Test,
      "org.typelevel" %% "cats-effect" % "2.0.0-RC1" % Test,
      "dev.zio" %% "zio" % "1.0.0-RC11-1" % Test
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    Test / classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.Flat
  )
  .disablePlugins(AssemblyPlugin)

lazy val `logstage-api` = project.in(file("logstage/logstage-api"))
  .dependsOn(
    `fundamentals-reflection` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
      "io.github.cquiroz" %% "scala-java-time" % "2.0.0-RC3"
    ),
    libraryDependencies ++= { if (scalaVersion.value.startsWith("2.")) Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided
    ) else Seq.empty },
    libraryDependencies ++= {
      val version = scalaVersion.value
      if (version.startsWith("0.") || version.startsWith("3.")) {
        Seq(
          "ch.epfl.lamp" %% "tasty-reflect" % scalaVersion.value % Provided
        )
      } else Seq.empty
    }
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `logstage-core` = project.in(file("logstage/logstage-core"))
  .dependsOn(
    `fundamentals-bio` % "test->compile;compile->compile",
    `logstage-api` % "test->test;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
      "org.typelevel" %% "cats-core" % "2.0.0-RC1" % Optional,
      "dev.zio" %% "zio" % "1.0.0-RC11-1" % Optional,
      "org.typelevel" %% "cats-core" % "2.0.0-RC1" % Test,
      "org.typelevel" %% "cats-effect" % "2.0.0-RC1" % Test,
      "dev.zio" %% "zio" % "1.0.0-RC11-1" % Test
    ),
    libraryDependencies ++= { if (scalaVersion.value.startsWith("2.")) Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided
    ) else Seq.empty },
    libraryDependencies ++= {
      val version = scalaVersion.value
      if (version.startsWith("0.") || version.startsWith("3.")) {
        Seq(
          "ch.epfl.lamp" %% "tasty-reflect" % scalaVersion.value % Provided
        )
      } else Seq.empty
    }
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `logstage-rendering-circe` = project.in(file("logstage/logstage-rendering-circe"))
  .dependsOn(
    `fundamentals-json-circe` % "test->compile;compile->compile",
    `logstage-core` % "test->test;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `logstage-di` = project.in(file("logstage/logstage-di"))
  .dependsOn(
    `logstage-config` % "test->compile;compile->compile",
    `distage-config` % "test->compile;compile->compile",
    `distage-model` % "test->compile;compile->compile",
    `distage-core` % "test->compile",
    `logstage-core` % "test->test;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `logstage-config` = project.in(file("logstage/logstage-config"))
  .dependsOn(
    `fundamentals-typesafe-config` % "test->compile;compile->compile",
    `logstage-core` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `logstage-adapter-slf4j` = project.in(file("logstage/logstage-adapter-slf4j"))
  .dependsOn(
    `logstage-core` % "test->test;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
      "org.slf4j" % "slf4j-api" % "1.7.28"
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    Compile / compileOrder := CompileOrder.Mixed,
    Test / compileOrder := CompileOrder.Mixed,
    Test / classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.Flat
  )
  .disablePlugins(AssemblyPlugin)

lazy val `logstage-sink-slf4j` = project.in(file("logstage/logstage-sink-slf4j"))
  .dependsOn(
    `logstage-api` % "test->compile;compile->compile",
    `logstage-core` % "test->compile,test"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
      "org.slf4j" % "slf4j-api" % "1.7.28",
      "org.slf4j" % "slf4j-simple" % "1.7.28" % Test
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `idealingua-v1-model` = project.in(file("idealingua-v1/idealingua-v1-model"))
  .dependsOn(
    `fundamentals-platform` % "test->compile;compile->compile",
    `fundamentals-functional` % "test->compile;compile->compile",
    `fundamentals-collections` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `idealingua-v1-core` = project.in(file("idealingua-v1/idealingua-v1-core"))
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
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
      "com.lihaoyi" %% "fastparse" % "2.1.3"
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `idealingua-v1-runtime-rpc-scala` = project.in(file("idealingua-v1/idealingua-v1-runtime-rpc-scala"))
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
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
      "org.typelevel" %% "cats-core" % "2.0.0-RC1",
      "org.typelevel" %% "cats-effect" % "2.0.0-RC1",
      "dev.zio" %% "zio" % "1.0.0-RC11-1",
      "dev.zio" %% "zio-interop-cats" % "2.0.0.0-RC2"
    ),
    libraryDependencies ++= { if (scalaVersion.value.startsWith("2.")) Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided
    ) else Seq.empty },
    libraryDependencies ++= {
      val version = scalaVersion.value
      if (version.startsWith("0.") || version.startsWith("3.")) {
        Seq(
          "ch.epfl.lamp" %% "tasty-reflect" % scalaVersion.value % Provided
        )
      } else Seq.empty
    }
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `idealingua-v1-runtime-rpc-http4s` = project.in(file("idealingua-v1/idealingua-v1-runtime-rpc-http4s"))
  .dependsOn(
    `idealingua-v1-runtime-rpc-scala` % "test->compile;compile->compile",
    `logstage-core` % "test->compile;compile->compile",
    `logstage-adapter-slf4j` % "test->compile;compile->compile",
    `idealingua-v1-test-defs` % "test->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
      "org.http4s" %% "http4s-dsl" % "0.21.0-M4",
      "org.http4s" %% "http4s-circe" % "0.21.0-M4",
      "org.http4s" %% "http4s-blaze-server" % "0.21.0-M4",
      "org.http4s" %% "http4s-blaze-client" % "0.21.0-M4",
      "org.asynchttpclient" % "async-http-client" % "2.10.1"
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `idealingua-v1-transpilers` = project.in(file("idealingua-v1/idealingua-v1-transpilers"))
  .dependsOn(
    `fundamentals-platform` % "test->compile;compile->compile",
    `fundamentals-functional` % "test->compile;compile->compile",
    `fundamentals-collections` % "test->compile;compile->compile",
    `fundamentals-json-circe` % "test->compile;compile->compile",
    `idealingua-v1-core` % "test->compile;compile->compile",
    `idealingua-v1-runtime-rpc-scala` % "test->compile;compile->compile",
    `idealingua-v1-test-defs` % "test->compile",
    `idealingua-v1-runtime-rpc-typescript` % "test->compile",
    `idealingua-v1-runtime-rpc-go` % "test->compile",
    `idealingua-v1-runtime-rpc-csharp` % "test->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
      "org.scala-lang.modules" %% "scala-xml" % "1.2.0",
      "org.scalameta" %% "scalameta" % "4.2.3"
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    Test / fork := true,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `idealingua-v1-test-defs` = project.in(file("idealingua-v1/idealingua-v1-test-defs"))
  .dependsOn(
    `idealingua-v1-runtime-rpc-scala` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `idealingua-v1-runtime-rpc-typescript` = project.in(file("idealingua-v1/idealingua-v1-runtime-rpc-typescript"))
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `idealingua-v1-runtime-rpc-go` = project.in(file("idealingua-v1/idealingua-v1-runtime-rpc-go"))
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `idealingua-v1-runtime-rpc-csharp` = project.in(file("idealingua-v1/idealingua-v1-runtime-rpc-csharp"))
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } }
  )
  .disablePlugins(AssemblyPlugin)

lazy val `idealingua-v1-compiler` = project.in(file("idealingua-v1/idealingua-v1-compiler"))
  .dependsOn(
    `idealingua-v1-transpilers` % "test->compile;compile->compile",
    `idealingua-v1-runtime-rpc-scala` % "test->compile;compile->compile",
    `idealingua-v1-runtime-rpc-typescript` % "test->compile;compile->compile",
    `idealingua-v1-runtime-rpc-go` % "test->compile;compile->compile",
    `idealingua-v1-runtime-rpc-csharp` % "test->compile;compile->compile",
    `idealingua-v1-test-defs` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
      "com.typesafe" % "config" % "1.3.4"
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    assembly / mainClass := Some("izumi.idealingua.compiler.CommandlineIDLCompiler"),
    assembly / assemblyMergeStrategy := {
          // FIXME: workaround for https://github.com/zio/interop-cats/issues/16
          case path if path.contains("zio/BuildInfo$.class") =>
            MergeStrategy.last
          case p =>
            (assembly / assemblyMergeStrategy).value(p)
    },
    Compile / assembly / artifact := {
          val art = (Compile / assembly / artifact).value
          art.withClassifier(Some("assembly"))
    },
    addArtifact(Compile / assembly / artifact, assembly)
  )
  .enablePlugins(AssemblyPlugin)

lazy val `microsite` = project.in(file("doc/microsite"))
  .dependsOn(
    `fundamentals-collections` % "test->compile;compile->compile",
    `fundamentals-platform` % "test->compile;compile->compile",
    `fundamentals-functional` % "test->compile;compile->compile",
    `fundamentals-bio` % "test->compile;compile->compile",
    `fundamentals-typesafe-config` % "test->compile;compile->compile",
    `fundamentals-reflection` % "test->compile;compile->compile",
    `fundamentals-json-circe` % "test->compile;compile->compile",
    `distage-model` % "test->compile;compile->compile",
    `distage-proxy-cglib` % "test->compile;compile->compile",
    `distage-core` % "test->compile;compile->compile",
    `distage-config` % "test->compile;compile->compile",
    `distage-roles-api` % "test->compile;compile->compile",
    `distage-plugins` % "test->compile;compile->compile",
    `distage-roles` % "test->compile;compile->compile",
    `distage-static` % "test->compile;compile->compile",
    `distage-testkit` % "test->compile;compile->compile",
    `logstage-api` % "test->compile;compile->compile",
    `logstage-core` % "test->compile;compile->compile",
    `logstage-rendering-circe` % "test->compile;compile->compile",
    `logstage-di` % "test->compile;compile->compile",
    `logstage-config` % "test->compile;compile->compile",
    `logstage-adapter-slf4j` % "test->compile;compile->compile",
    `logstage-sink-slf4j` % "test->compile;compile->compile"
  )
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test,
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
    crossScalaVersions := Seq(
      "2.12.8"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    crossScalaVersions := Seq(
      "2.12.8"
    ),
    scalaVersion := crossScalaVersions.value.head,
    coverageEnabled := false,
    publish / skip := true,
    DocKeys.prefix := {if (isSnapshot.value) {
                "latest/snapshot"
              } else {
                "latest/release"
              }},
    previewFixedPort := Some(9999),
    git.remoteRepo := "git@github.com:7mind/izumi-microsite.git",
    Compile / classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.Flat,
    mdocIn := baseDirectory.value / "src/main/tut",
    Paradox / sourceDirectory := mdocOut.value,
    mdocExtraArguments ++= Seq(
      " --no-link-hygiene"
    ),
    SitePlugin.autoImport.makeSite / mappings := {
                (SitePlugin.autoImport.makeSite / mappings)
                  .dependsOn(mdoc.toTask(" "))
                  .value
              },
    Paradox / version := version.value,
    ParadoxMaterialThemePlugin.paradoxMaterialThemeSettings(Paradox),
    addMappingsToSiteDir(ScalaUnidoc / packageDoc / mappings, ScalaUnidoc / siteSubdirName),
    ScalaUnidoc / unidoc / unidocProjectFilter := inAggregates(`izumi-jvm`, transitive=true),
    Paradox / paradoxMaterialTheme ~= {
                _.withCopyright("7mind.io")
                  .withRepository(uri("https://github.com/7mind/izumi"))
                //        .withColor("222", "434343")
              },
    ScalaUnidoc / siteSubdirName := s"${DocKeys.prefix.value}/api",
    Paradox / siteSubdirName := s"${DocKeys.prefix.value}/doc",
    paradoxProperties ++= Map(
                "scaladoc.izumi.base_url" -> s"/${DocKeys.prefix.value}/api/com/github/pshirshov/",
                "scaladoc.base_url" -> s"/${DocKeys.prefix.value}/api/",
                "izumi.version" -> version.value,
              ),
    ghpagesCleanSite / excludeFilter :=
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
                }
  )
  .enablePlugins(ScalaUnidocPlugin, ParadoxSitePlugin, SitePlugin, GhpagesPlugin, ParadoxMaterialThemePlugin, PreprocessPlugin, MdocPlugin)
  .disablePlugins(ScoverageSbtPlugin, AssemblyPlugin)

lazy val `sbt-izumi-deps` = project.in(file("sbt-plugins/sbt-izumi-deps"))
  .settings(
    libraryDependencies ++= Seq(
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2",
      "org.scalatest" %% "scalatest" % "3.1.2" % Test
    )
  )
  .settings(
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head,
    organization := "io.7mind",
    Compile / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/main/scala" ,
    Compile / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/main/resources" ,
    Test / unmanagedSourceDirectories += baseDirectory.value / ".jvm/src/test/scala" ,
    Test / unmanagedResourceDirectories += baseDirectory.value / ".jvm/src/test/resources" ,
    scalacOptions ++= Seq(
      s"-Xmacro-settings:product-name=${name.value}",
      s"-Xmacro-settings:product-version=${version.value}",
      s"-Xmacro-settings:product-group=${organization.value}",
      s"-Xmacro-settings:scala-version=${scalaVersion.value}",
      s"-Xmacro-settings:scala-versions=${crossScalaVersions.value.mkString(":")}"
    ),
    Test / testOptions += Tests.Argument("-oDF"),
    scalacOptions ++= { (isSnapshot.value, scalaVersion.value) match {
      case (_, "2.12.9") => Seq(
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        "-Ypartial-unification",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
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
        "-Xsource:3",
        "-P:kind-projector:underscore-placeholders",
        "-Wconf:msg=package.object.inheritance:silent",
        if (insideCI.value) "-Wconf:any:error" else "-Wconf:any:warning",
        "-Wconf:cat=optimizer:warning",
        "-Wconf:cat=other-match-analysis:error",
        "-Vimplicits",
        "-Vtype-diffs",
        "-Ybackend-parallelism",
        math.min(16, math.max(1, sys.runtime.availableProcessors() - 1)).toString,
        "-Wdead-code",
        "-Wextra-implicit",
        "-Wnumeric-widen",
        "-Woctal-literal",
        "-Wvalue-discard",
        "-Wunused:_",
        "-Wmacros:after",
        "-Ycache-plugin-class-loader:always",
        "-Ycache-macro-class-loader:last-modified"
      )
      case (_, _) => Seq.empty
    } },
    sbtPlugin := true,
    withBuildInfo("izumi.sbt.deps", "Izumi")
  )
  .disablePlugins(AssemblyPlugin)

lazy val `fundamentals` = (project in file(".agg/fundamentals-fundamentals"))
  .settings(
    publish / skip := true,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `fundamentals-collections`,
    `fundamentals-platform`,
    `fundamentals-functional`,
    `fundamentals-bio`,
    `fundamentals-typesafe-config`,
    `fundamentals-reflection`,
    `fundamentals-json-circe`
  )

lazy val `fundamentals-jvm` = (project in file(".agg/fundamentals-fundamentals-jvm"))
  .settings(
    publish / skip := true,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `fundamentals-collections`,
    `fundamentals-platform`,
    `fundamentals-functional`,
    `fundamentals-bio`,
    `fundamentals-typesafe-config`,
    `fundamentals-reflection`,
    `fundamentals-json-circe`
  )

lazy val `distage` = (project in file(".agg/distage-distage"))
  .settings(
    publish / skip := true,
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
    publish / skip := true,
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
    publish / skip := true,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `logstage-api`,
    `logstage-core`,
    `logstage-rendering-circe`,
    `logstage-di`,
    `logstage-config`,
    `logstage-adapter-slf4j`,
    `logstage-sink-slf4j`
  )

lazy val `logstage-jvm` = (project in file(".agg/logstage-logstage-jvm"))
  .settings(
    publish / skip := true,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `logstage-api`,
    `logstage-core`,
    `logstage-rendering-circe`,
    `logstage-di`,
    `logstage-config`,
    `logstage-adapter-slf4j`,
    `logstage-sink-slf4j`
  )

lazy val `idealingua` = (project in file(".agg/idealingua-v1-idealingua"))
  .settings(
    publish / skip := true,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `idealingua-v1-model`,
    `idealingua-v1-core`,
    `idealingua-v1-runtime-rpc-scala`,
    `idealingua-v1-runtime-rpc-http4s`,
    `idealingua-v1-transpilers`,
    `idealingua-v1-test-defs`,
    `idealingua-v1-runtime-rpc-typescript`,
    `idealingua-v1-runtime-rpc-go`,
    `idealingua-v1-runtime-rpc-csharp`,
    `idealingua-v1-compiler`
  )

lazy val `idealingua-jvm` = (project in file(".agg/idealingua-v1-idealingua-jvm"))
  .settings(
    publish / skip := true,
    crossScalaVersions := Seq(
      "2.12.9",
      "2.13.0"
    ),
    scalaVersion := crossScalaVersions.value.head
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `idealingua-v1-model`,
    `idealingua-v1-core`,
    `idealingua-v1-runtime-rpc-scala`,
    `idealingua-v1-runtime-rpc-http4s`,
    `idealingua-v1-transpilers`,
    `idealingua-v1-test-defs`,
    `idealingua-v1-runtime-rpc-typescript`,
    `idealingua-v1-runtime-rpc-go`,
    `idealingua-v1-runtime-rpc-csharp`,
    `idealingua-v1-compiler`
  )

lazy val `doc` = (project in file(".agg/doc-doc"))
  .settings(
    publish / skip := true,
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
    publish / skip := true,
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
    publish / skip := true
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `sbt-izumi-deps`
  )

lazy val `sbt-plugins-jvm` = (project in file(".agg/sbt-plugins-sbt-plugins-jvm"))
  .settings(
    publish / skip := true
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `sbt-izumi-deps`
  )

lazy val `izumi-jvm` = (project in file(".agg/.agg-jvm"))
  .settings(
    publish / skip := true,
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

lazy val `izumi` = (project in file("."))
  .settings(
    publish / skip := true,
    ThisBuild / publishMavenStyle := true,
    ThisBuild / scalacOptions ++= Seq(
      "-encoding",
      "UTF-8",
      "-target:jvm-1.8",
      "-feature",
      "-unchecked",
      "-deprecation",
      "-language:higherKinds",
      "-explaintypes"
    ),
    ThisBuild / javacOptions ++= Seq(
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
    ThisBuild / scalacOptions ++= Seq(
      s"-Xmacro-settings:sbt-version=${sbtVersion.value}",
      s"-Xmacro-settings:git-repo-clean=${com.typesafe.sbt.SbtGit.GitKeys.gitUncommittedChanges.value}",
      s"-Xmacro-settings:git-branch=${com.typesafe.sbt.SbtGit.GitKeys.gitCurrentBranch.value}",
      s"-Xmacro-settings:git-described-version=${com.typesafe.sbt.SbtGit.GitKeys.gitDescribedVersion.value.getOrElse("")}",
      s"-Xmacro-settings:git-head-commit=${com.typesafe.sbt.SbtGit.GitKeys.gitHeadCommit.value.getOrElse("")}"
    ),
    crossScalaVersions := Nil,
    scalaVersion := "2.12.9",
    ThisBuild / organization := "io.7mind.izumi",
    ThisBuild / homepage := Some(url("https://izumi.7mind.io")),
    ThisBuild / licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php")),
    ThisBuild / developers := List(
              Developer(id = "7mind", name = "Septimal Mind", url = url("https://github.com/7mind"), email = "team@7mind.io"),
            ),
    ThisBuild / scmInfo := Some(ScmInfo(url("https://github.com/7mind/izumi"), "scm:git:https://github.com/7mind/izumi.git")),
    ThisBuild / scalacOptions += """-Xmacro-settings:scalatest-version=3.1.2"""
  )
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    `fundamentals`,
    `distage`,
    `logstage`,
    `idealingua`
  )
