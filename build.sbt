sbtPlugin in ThisBuild := true

organization in ThisBuild := "com.thoughtworks.sbt-best-practice"

lazy val `detect-license` = project dependsOn git

lazy val travis = project dependsOn git

lazy val sonatype = project dependsOn `scalac-options`

lazy val git = project

lazy val `disable-deploy` = project dependsOn git

lazy val `cross-release` = project

lazy val `scalac-options` = project

lazy val `publish-unidoc` = project dependsOn travis dependsOn `scalac-options`

lazy val `detect-scala-organization` = project

dependsOn(`scalac-options`,
          `detect-license`,
          travis,
          sonatype,
          git,
          `disable-deploy`,
          `cross-release`,
          `publish-unidoc`,
          `detect-scala-organization`)

libraryDependencies += "com.lihaoyi" %% "sourcecode" % "0.1.5"

addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % "3.0.0")

lazy val unidoc = project
  .enablePlugins(TravisUnidocTitle)
  .settings(
    unidocProjectFilter in ScalaUnidoc in BaseUnidocPlugin.autoImport.unidoc := inAnyProject
  )

lazy val secret = project.settings(publishArtifact := false).in {
  val secretDirectory = file(sourcecode.File()).getParentFile / "secret"
  for (token <- sys.env.get("GITHUB_PERSONAL_ACCESS_TOKEN")) {
    IO.delete(secretDirectory)
    org.eclipse.jgit.api.Git
      .cloneRepository()
      .setURI("https://github.com/ThoughtWorksInc/tw-data-china-continuous-delivery-password.git")
      .setDirectory(secretDirectory)
      .setCredentialsProvider(
        new org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider(token, "")
      )
      .call()
      .close()
  }
  secretDirectory
}
