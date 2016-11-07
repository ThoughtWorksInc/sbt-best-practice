package com.thoughtworks.sbtBestPractice.travis

import sbt.Keys._
import sbt._

/**
  * Fill project informations from Travis environment variables
  */
object DetectHomepageFromTravisEnvironmentVariables extends AutoPlugin {

  override def requires = Travis

  override def trigger = allRequirements

  override def buildSettings = Seq(
    homepage := {
      Travis.travisRepoSlug.?.value.map { slug =>
        new URL("https", "github.com", raw"""/$slug""")
      }
    },
    scmInfo := Travis.travisRepoSlug.?.value.map { slug =>
      ScmInfo(
        new URL("https", "github.com", raw"""/$slug"""),
        raw"""https://github.com/$slug.git""",
        Some(raw"""git@github.com:$slug.git""")
      )
    }
  )

}
