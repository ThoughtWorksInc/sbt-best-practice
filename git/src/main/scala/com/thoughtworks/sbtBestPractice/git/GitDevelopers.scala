package com.thoughtworks.sbtBestPractice.git

import sbt.Keys._
import sbt._
import scala.collection.JavaConverters._

/** Fill developers from git log
  */
object GitDevelopers extends AutoPlugin {

  override def trigger = allRequirements

  override def requires = Git

  import Git._

  private def settings = Seq(
    developers := {
      if (gitDir.value.isDefined) {
        val repository = gitRepositoryBuilder.value.build()
        try {
          val git = org.eclipse.jgit.api.Git.wrap(repository)
          try {
            val logCommand =
              (gitWorkTree.value, gitSubrepoWorkTree.value) match {
                case (Some(workTree), Some(subrepoWorkTree))
                    if subrepoWorkTree != workTree.getCanonicalFile =>
                  git
                    .log()
                    .addPath(
                      workTree.getCanonicalFile.toPath
                        .relativize(subrepoWorkTree.toPath)
                        .iterator
                        .asScala
                        .mkString("/")
                    )
                case _ =>
                  git.log()
              }
            developers.value ++ (for {
              commit <- logCommand.call().asScala
            } yield {
              val author = commit.getAuthorIdent
              Developer(
                "",
                author.getName,
                author.getEmailAddress,
                Compatibility.mailtoUrl(author.getEmailAddress)
              )
            }).toSet
          } finally {
            git.close()
          }
        } finally {
          repository.close()
        }
      } else {
        developers.value
      }
    }
  )

  override def buildSettings = settings

  override def projectSettings = settings

}
