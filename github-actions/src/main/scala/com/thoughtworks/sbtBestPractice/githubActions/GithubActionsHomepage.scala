package com.thoughtworks.sbtBestPractice.githubActions

import sbt.Keys._
import sbt._
import com.thoughtworks.sbtBestPractice.git.Compatibility

/** Fill project information from Github Actions environment variables
  */
object GithubActionsHomepage extends AutoPlugin {

  override def requires = GithubActionsEnvironmentVariables

  override def trigger = allRequirements

  override def buildSettings = Seq(
    homepage := {
      homepage.value match {
        case None =>
          GithubActionsEnvironmentVariables.githubRepository.?.value match {
            case None =>
              None
            case Some(slug) =>
              Some(Compatibility.httpsUrl("github.com", raw"""/$slug"""))
          }
        case someHomepage @ Some(_) =>
          someHomepage
      }
    },
    scmInfo := {
      scmInfo.value match {
        case None =>
          GithubActionsEnvironmentVariables.githubRepository.?.value match {
            case None =>
              None
            case Some(slug) =>
              Some(
                ScmInfo(
                  Compatibility.httpsUrl("github.com", raw"""/$slug"""),
                  raw"""scm:git:https://github.com/$slug.git""",
                  Some(raw"""scm:git:ssh://git@github.com/$slug.git""")
                )
              )
          }
        case someScmInfo @ Some(_) =>
          someScmInfo
      }
    }
  )

}
