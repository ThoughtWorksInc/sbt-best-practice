sbtPlugin in ThisBuild := true

organization in ThisBuild := "com.thoughtworks.sbt-best-practice"

lazy val `remote-sbt-file` = project dependsOn issue2514

lazy val `detect-license` = project dependsOn git

lazy val travis = project dependsOn git

lazy val sonatype = project

lazy val issue2514 = project

lazy val git = project

lazy val `disable-deploy` = project dependsOn git

lazy val `cross-release` = project

lazy val `scalac-options` = project

lazy val `publish-unidoc` = project dependsOn travis dependsOn `scalac-options`

new sbt.internals.DslEnablePlugins(Nil) {
  override val toFunction = { thisProject: Project =>
    thisProject.dependsOn(`scalac-options`,
                          `remote-sbt-file`,
                          `detect-license`,
                          travis,
                          sonatype,
                          issue2514,
                          git,
                          `disable-deploy`,
                          `cross-release`,
                          `publish-unidoc`)
  }
}

libraryDependencies += "com.lihaoyi" %% "sourcecode" % "0.1.1"

addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % "0.2.3")

lazy val unidoc = project
  .enablePlugins(TravisUnidocTitle)
  .settings(
    UnidocKeys.unidocProjectFilter in ScalaUnidoc in UnidocKeys.unidoc := inAnyProject
  )
