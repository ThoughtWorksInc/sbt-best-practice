libraryDependencies += "com.jsuereth" %% "scala-arm" % "2.0"

addSbtPlugin("com.eed3si9n" % "sbt-unidoc" % "0.4.3")

libraryDependencies += Defaults.sbtPluginExtra(
  "org.scala-js" % "sbt-scalajs" % "1.0.0",
  (sbtBinaryVersion in pluginCrossBuild).value,
  (scalaBinaryVersion in update).value
) % Optional

libraryDependencies += "org.yaml" % "snakeyaml" % "1.29"

libraryDependencies += "com.thoughtworks.dsl" %% "keywords-continue" % "1.5.2"

libraryDependencies += "com.thoughtworks.dsl" %% "keywords-each" % "1.5.2"

libraryDependencies += "com.thoughtworks.dsl" %% "keywords-return" % "1.5.2"

addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-bangnotation" % "1.5.2")

addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-reseteverywhere" % "1.5.2")
