libraryDependencies += "com.jsuereth" %% "scala-arm" % "2.0"

addSbtPlugin("com.github.sbt" % "sbt-unidoc" % "0.5.0")

libraryDependencies += Defaults.sbtPluginExtra(
  "org.scala-js" % "sbt-scalajs" % "1.18.2",
  (sbtBinaryVersion in pluginCrossBuild).value,
  (scalaBinaryVersion in update).value
) % Optional

libraryDependencies += "org.yaml" % "snakeyaml" % "2.0"

libraryDependencies += "com.thoughtworks.dsl" %% "keywords-continue" % "1.5.5"

libraryDependencies += "com.thoughtworks.dsl" %% "keywords-each" % "1.5.5"

libraryDependencies += "com.thoughtworks.dsl" %% "keywords-return" % "1.5.5"

addCompilerPlugin(
  "com.thoughtworks.dsl" %% "compilerplugins-bangnotation" % "1.5.5"
)

addCompilerPlugin(
  "com.thoughtworks.dsl" %% "compilerplugins-reseteverywhere" % "1.5.5"
)
