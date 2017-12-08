sbtPlugin in ThisBuild := true

scalaVersion in ThisBuild := "2.12.4"

sbtBinaryVersion in update in ThisBuild := "1.0"

sbtVersion in pluginCrossBuild in ThisBuild := "1.0.0-RC3"

organization in ThisBuild := "com.thoughtworks.sbt-best-practice"

lazy val `detect-license` = project dependsOn git

lazy val travis = project dependsOn git

lazy val sonatype = project

lazy val git = project

lazy val `disable-deploy` = project dependsOn git

lazy val `cross-release` = project

lazy val `scalac-options` = project

lazy val `publish-unidoc` = project dependsOn travis dependsOn `scalac-options`

new sbt.internals.DslEnablePlugins(Nil) {
  override val toFunction = { thisProject: Project =>
    thisProject.dependsOn(`scalac-options`,
                          `detect-license`,
                          travis,
                          sonatype,
                          git,
                          `disable-deploy`,
                          `cross-release`,
                          `publish-unidoc`)
  }
}

libraryDependencies += "com.lihaoyi" %% "sourcecode" % "0.1.4"

addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % "2.0.0")

lazy val unidoc = project
  .enablePlugins(TravisUnidocTitle)
  .settings(
    UnidocKeys.unidocProjectFilter in ScalaUnidoc in UnidocKeys.unidoc := inAnyProject
  )
