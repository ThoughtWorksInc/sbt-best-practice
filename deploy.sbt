enablePlugins(Travis)

enablePlugins(SonatypeRelease)

lazy val secret = project.settings(publishArtifact := false).in {
  val temporaryDirectory = IO.createTemporaryDirectory
  org.eclipse.jgit.api.Git
    .cloneRepository()
    .setURI("https://github.com/ThoughtWorksInc/tw-data-china-continuous-delivery-password.git")
    .setDirectory(temporaryDirectory)
    .setCredentialsProvider(
      new org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider(sys.env("GITHUB_PERSONAL_ACCESS_TOKEN"), "")
    )
    .call()
    .close()
  temporaryDirectory
}
