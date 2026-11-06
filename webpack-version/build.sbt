// sbt-scalajs-bundler has no release for sbt 2.x, so on sbt 2.x this module
// has no sources (they are in src/main/scala-2.12) and does nothing.
libraryDependencies ++= {
  if (scalaBinaryVersion.value == "2.12")
    Seq(
      Defaults.sbtPluginExtra(
        "ch.epfl.scala" % "sbt-scalajs-bundler" % "0.21.1",
        (pluginCrossBuild / sbtBinaryVersion).value,
        (update / scalaBinaryVersion).value
      ) % Optional
    )
  else
    Seq.empty
}
