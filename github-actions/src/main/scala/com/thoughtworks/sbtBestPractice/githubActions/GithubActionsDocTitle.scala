package com.thoughtworks.sbtBestPractice.githubActions

import sbt.plugins.JvmPlugin
import sbt._
import Keys._

/** @author
  *   杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object GithubActionsDocTitle extends AutoPlugin {
  override def requires: Plugins =
    GithubActionsEnvironmentVariables && JvmPlugin

  override def trigger: PluginTrigger = allRequirements

  override def projectSettings = Seq(
    Compile / doc / scalacOptions := {
      val originalScalacOptions = (Compile / doc / scalacOptions).value
      GithubActionsEnvironmentVariables.githubRepository.?.value match {
        case Some(slug) =>
          originalScalacOptions.indexOf("-doc-title") match {
            case -1 =>
              originalScalacOptions ++ Seq("-doc-title", slug)
            case i =>
              originalScalacOptions.updated(i + 1, slug)
          }
        case None =>
          originalScalacOptions
      }
    }
  )

}
