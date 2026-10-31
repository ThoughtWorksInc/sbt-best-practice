libraryDependencies += Defaults.sbtPluginExtra(
  "ch.epfl.scala" % "sbt-scalajs-bundler" % "0.21.1",
  (sbtBinaryVersion in pluginCrossBuild).value,
  (scalaBinaryVersion in update).value
) % Optional
