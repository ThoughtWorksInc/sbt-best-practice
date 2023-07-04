package com.thoughtworks.sbtBestPractice.githubActions

import sbt._
import com.thoughtworks.dsl.keywords.Yield

/** Configure sbt settings of environment variables from Github Actions
  *
  * @note
  *   This plugin should only be enabled when sbt is running on Github Actions
  *
  * @author
  *   杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object GithubActionsEnvironmentVariables extends AutoPlugin {

  val githubRef = settingKey[String](
    "The branch or tag ref that triggered the workflow. For example, refs/heads/feature-branch-1. If neither a branch or tag is available for the event type, the variable will not exist."
  )
  val githubRepository = settingKey[String](
    "The owner and repository name. For example, octocat/Hello-World."
  )
  val githubSha = settingKey[String](
    "The commit SHA that triggered the workflow. For example, ffac537e6cbbf934b08745a378932722df287a53."
  )
  override def globalSettings: Stream[Setting[_]] = {
    sys.env.get("GITHUB_REF") match {
      case Some(value) =>
        !Yield(githubRef := value)
      case None =>
    }
    sys.env.get("GITHUB_REPOSITORY") match {
      case Some(value) =>
        !Yield(githubRepository := value)
      case None =>
    }
    sys.env.get("GITHUB_SHA") match {
      case Some(value) =>
        !Yield(githubRef := value)
      case None =>
    }
    Stream.empty
  }

  override def trigger = {
    if (sys.env.contains("GITHUB_REPOSITORY")) {
      allRequirements
    } else {
      noTrigger
    }
  }

}
