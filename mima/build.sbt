addSbtPlugin("com.typesafe" % "sbt-mima-plugin" % "1.2.1")

libraryDependencies += Defaults.sbtPluginExtra(
  "com.github.sbt" % "sbt-dynver" % (if (scalaBinaryVersion.value == "2.12") "5.2.0" else "5.1.1"),
  (pluginCrossBuild / sbtBinaryVersion).value,
  (update / scalaBinaryVersion).value
)
