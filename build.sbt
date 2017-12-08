sbtPlugin in ThisBuild := true

organization in ThisBuild := "com.thoughtworks.sbt-best-practice"

lazy val `detect-license` = project dependsOn git

lazy val travis = project dependsOn git

lazy val sonatype = project

lazy val git = project

lazy val `disable-deploy` = project dependsOn git

lazy val `cross-release` = project

lazy val `scalac-options` = project

lazy val `publish-unidoc` = project dependsOn travis dependsOn `scalac-options`

publishTo in ThisBuild := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

dependsOn(`scalac-options`,
          `detect-license`,
          travis,
          sonatype,
          git,
          `disable-deploy`,
          `cross-release`,
          `publish-unidoc`)

libraryDependencies += "com.lihaoyi" %% "sourcecode" % "0.1.4"

libraryDependencies += "commons-io" % "commons-io" % "2.5"

addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % "2.0.0")

lazy val unidoc = project
  .enablePlugins(TravisUnidocTitle)
  .settings(
    unidocProjectFilter in ScalaUnidoc in BaseUnidocPlugin.autoImport.unidoc := inAnyProject
  )
