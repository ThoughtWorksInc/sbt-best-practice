package com.thoughtworks.sbtBestPractice.travis

import sbt.Keys._
import sbt._

/**
  * Fill project informations from Travis environment variables
  */
object TravisInformation extends AutoPlugin {

  override def requires = Travis

  override def trigger = allRequirements

  override def buildSettings = Seq(
    homepage <<= {

    }
    homepage := {
      Travis.travisRepoSlug.?.value.map { slug => new URL("https", "github.com", raw"""/$slug""") }
    },
    scmInfo := Some(ScmInfo(
      new URL("https", "github.com", raw"""/${Travis.travisRepoSlug.value}"""),
      raw"""https://github.com/${Travis.travisRepoSlug.value}.git""",
      Some(raw"""git@github.com:${Travis.travisRepoSlug.value}.git""")
    ))
  )

}