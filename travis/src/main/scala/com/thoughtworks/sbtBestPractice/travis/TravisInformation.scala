package com.thoughtworks.sbtBestPractice.travis

import sbt.Keys._
import sbt._

/**
  * Fill project informations from Travis environment variables
  */
object TravisInformation extends AutoPlugin {

  override def requires = TravisEnvironmentVariables

  override def trigger = allRequirements

  override def buildSettings = Seq(
    homepage := Some(new URL("https", "github.com", raw"""/${TravisEnvironmentVariables.travisRepoSlug.value}""")),
    scmInfo := Some(ScmInfo(
      new URL("https", "github.com", raw"""/${TravisEnvironmentVariables.travisRepoSlug.value}"""),
      raw"""https://github.com/${TravisEnvironmentVariables.travisRepoSlug.value}.git""",
      Some(raw"""git@github.com:${TravisEnvironmentVariables.travisRepoSlug.value}.git""")
    ))
  )

}