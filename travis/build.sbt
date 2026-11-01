addSbtPlugin("com.github.sbt" % "sbt-unidoc" % "0.6.1")

addSbtPlugin("com.github.sbt" % "sbt2-compat" % "0.2.0")

libraryDependencies += Defaults.sbtPluginExtra(
  "org.scala-js" % "sbt-scalajs" % "1.22.0",
  (pluginCrossBuild / sbtBinaryVersion).value,
  (update / scalaBinaryVersion).value
) % Optional

libraryDependencies += "org.yaml" % "snakeyaml" % "2.7"
