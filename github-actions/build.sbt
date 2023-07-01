libraryDependencies += "org.yaml" % "snakeyaml" % "1.26"

libraryDependencies += "com.thoughtworks.dsl" %% "keywords-yield" % "1.5.5"

libraryDependencies += "com.thoughtworks.extractor" %% "extractor" % "2.1.3"

addCompilerPlugin(
  "com.thoughtworks.dsl" %% "compilerplugins-bangnotation" % "1.5.2"
)

addCompilerPlugin(
  "com.thoughtworks.dsl" %% "compilerplugins-reseteverywhere" % "1.5.2"
)

libraryDependencies += Defaults.sbtPluginExtra(
  "org.scala-js" % "sbt-scalajs" % "1.0.1",
  (sbtBinaryVersion in pluginCrossBuild).value,
  (scalaBinaryVersion in update).value
) % Optional
