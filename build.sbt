sbtPlugin in ThisBuild := true

organization in ThisBuild := "com.thoughtworks.sbt-best-practice"

lazy val `detect-license` = project dependsOn git

lazy val travis = project dependsOn git

lazy val git = project

lazy val `scalac-options` = project

lazy val `publish-unidoc` = project

lazy val `detect-scala-organization` = project

lazy val `workaround-for-sbt-sonatype-issue-79` = project

dependsOn(`scalac-options`,
          `detect-license`,
          travis,
          git,
          `publish-unidoc`,
          `detect-scala-organization`,
          `workaround-for-sbt-sonatype-issue-79`)

libraryDependencies += "com.lihaoyi" %% "sourcecode" % "0.1.5"

addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % "3.0.0")

enablePlugins(ScalaUnidocPlugin)

sonatypeDefaultResolver := {
  val sonatypeRepo = "https://oss.sonatype.org/"
  val profileM     = sonatypeStagingRepositoryProfile.?.value

  val staged = profileM.map { stagingRepoProfile =>
    "releases" at sonatypeRepo +
      "service/local/staging/deployByRepositoryId/" +
      stagingRepoProfile.repositoryId
  }
  staged.getOrElse(if (isSnapshot.value) {
    Opts.resolver.sonatypeSnapshots
  } else {
    Opts.resolver.sonatypeStaging
  })
}
