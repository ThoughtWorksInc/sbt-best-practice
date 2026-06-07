package com.thoughtworks.sbtBestPractice.githubActions

import com.thoughtworks.sbtBestPractice.git.{Git => GitPlugin}
import org.eclipse.jgit.lib.Constants
import sbt.Keys._
import sbt._
import Ordering.Implicits._

/** @author
  *   杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object GithubActionsScaladocSourceUrl extends AutoPlugin {
  override def requires: Plugins =
    GithubActionsEnvironmentVariables && GitPlugin

  override def trigger: PluginTrigger = allRequirements

  override def projectSettings = Seq(
    Compile / doc / scalacOptions := {
      val originalScalacOptions = (Compile / doc / scalacOptions).value
      if (VersionNumber(scalaVersion.value).numbers < Seq(3L)) {
        GitPlugin.gitWorkTree.value match {
          case Some(rootDirectory) =>
            originalScalacOptions.indexOf("-sourcepath") match {
              case -1 =>
                originalScalacOptions ++ Seq(
                  "-sourcepath",
                  rootDirectory.toString
                )
              case i =>
                originalScalacOptions.updated(i + 1, rootDirectory.toString)
            }
          case None =>
            originalScalacOptions
        }
      } else {
        originalScalacOptions
      }
    },
    Compile / doc / scalacOptions := {
      val originalScalacOptions = (Compile / doc / scalacOptions).value
      (
        GitPlugin.gitRepositoryBuilder.?.value,
        GithubActionsEnvironmentVariables.githubRepository.?.value
      ) match {
        case (Some(repositoryBuilder), Some(slug)) =>
          val repository = repositoryBuilder.build()
          try {
            val hash = repository.resolve(Constants.HEAD).name
            if (VersionNumber(scalaVersion.value).numbers < Seq(3L)) {
              val sourceUrl =
                raw"https://github.com/$slug/blob/${hash}€{FILE_PATH}.scala"
              originalScalacOptions.indexOf("-doc-source-url") match {
                case -1 =>
                  originalScalacOptions ++ Seq("-doc-source-url", sourceUrl)
                case i =>
                  originalScalacOptions.updated(i + 1, sourceUrl)
              }
            } else {
              val pathPrefix =
                GitPlugin.gitWorkTree.value.fold("")(_.toString())
              originalScalacOptions :+ raw"-source-links:$pathPrefix=github://$slug/$hash"
            }
          } finally {
            repository.close()
          }
        case _ =>
          originalScalacOptions
      }
    }
  )

}
