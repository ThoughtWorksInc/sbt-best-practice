sbtPlugin in ThisBuild := true

organization in ThisBuild := "com.thoughtworks.sbt-best-practice"

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
  `version-scheme`
)

libraryDependencies += "com.lihaoyi" %% "sourcecode" % "0.3.0"

addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % "3.0.2")

enablePlugins(ScalaUnidocPlugin)
