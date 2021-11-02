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
      homepage.value match {
        case None =>
          Travis.travisRepoSlug.?.value match {
            case None =>
              None
            case Some(slug) =>
              Some(new URL("https", "github.com", raw"""/$slug"""))
          }
        case someHomepage @ Some(_) =>
          someHomepage
      }
    },
    scmInfo := {
      scmInfo.value match {
        case None =>
          Travis.travisRepoSlug.?.value match {
            case None =>
              None
            case Some(slug) =>
              Some(
                ScmInfo(
                  new URL("https", "github.com", raw"""/$slug"""),
                  raw"""https://github.com/$slug.git""",
                  Some(raw"""git@github.com:$slug.git""")
                )
              )
          }
        case someScmInfo @ Some(_) =>
          someScmInfo
      }
    }
  )

}
