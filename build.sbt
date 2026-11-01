sbtPlugin in ThisBuild := true

organization in ThisBuild := "com.thoughtworks.sbt-best-practice"

// Cross-build for both sbt 1.x (Scala 2.12) and sbt 2.x (Scala 3). The Scala
// versions are not configured here: sbt-best-practice derives scalaVersion and
// crossScalaVersions from the build matrix in .github/workflows/scala.yml.
// Sources that differ between the two are in src/main/scala-2.12 and
// src/main/scala-3 of each module.
ThisBuild / pluginCrossBuild / sbtVersion := {
  scalaBinaryVersion.value match {
    case "2.12" => sbtVersion.value // sbt 1.x, from project/build.properties
    case _      => "2.0.0" // sbt 2.x
  }
}

lazy val `detect-license` = project dependsOn git

lazy val travis = project dependsOn git

lazy val `github-actions` = project dependsOn git

lazy val git = project

lazy val `scalac-options` = project

lazy val `publish-unidoc` = project

lazy val `detect-scala-organization` = project

lazy val `subdirectory-organization` = project

lazy val mima = project

lazy val `skip-duplicate-java-publish` = project

lazy val `version-scheme` = project

lazy val `webpack-version` = project

dependsOn(
  `scalac-options`,
  `detect-license`,
  travis,
  `github-actions`,
  git,
  `publish-unidoc`,
  `detect-scala-organization`,
  `subdirectory-organization`,
  `skip-duplicate-java-publish`,
  `version-scheme`,
  `webpack-version`
)

libraryDependencies += "com.lihaoyi" %% "sourcecode" % "0.4.4"

addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % "3.1.0")

addSbtPlugin("com.github.sbt" % "sbt-dynver" % "5.1.1")

addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.3.2")

// partial-unification and sbt-example have no release for sbt 2.x, so the
// sbt 2.x build of this plugin does not bring them.
libraryDependencies ++= {
  if (scalaBinaryVersion.value == "2.12")
    Seq(
      "org.lyranthe.sbt" % "partial-unification" % "1.1.2",
      "com.thoughtworks.example" % "sbt-example" % "9.3.1"
    ).map(
      Defaults.sbtPluginExtra(
        _,
        (pluginCrossBuild / sbtBinaryVersion).value,
        (update / scalaBinaryVersion).value
      )
    )
  else
    Seq.empty
}

enablePlugins(ScalaUnidocPlugin)
