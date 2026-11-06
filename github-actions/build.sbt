libraryDependencies += "org.yaml" % "snakeyaml" % "1.33"

addSbtPlugin("com.github.sbt" % "sbt2-compat" % "0.2.0")

libraryDependencies += Defaults.sbtPluginExtra(
  "org.scala-js" % "sbt-scalajs" % (scalaBinaryVersion.value match {
    case "2.12" => "1.0.1"
    case _      => "1.22.0" // the first release for sbt 2.x
  }),
  (pluginCrossBuild / sbtBinaryVersion).value,
  (update / scalaBinaryVersion).value
) % Optional
