package com.thoughtworks.sbtBestPractice.git

import sbt.Keys._
import sbt._
import scala.collection.JavaConverters._

/**
  * Fill developers from git log
  */
object GitDevelopers extends AutoPlugin {

  override def trigger = allRequirements

  override def requires = Git

  import Git._

  override def projectSettings = Seq(
    developers := {
      if (gitDir.value.isDefined) {
        val repository = gitRepositoryBuilder.value.build
        try {
          val git = org.eclipse.jgit.api.Git.wrap(repository)
          try {
            developers.value ++ (for {
              commit <- git.log().call().asScala
            } yield {
              val author = commit.getAuthorIdent
              Developer("", author.getName, author.getEmailAddress, new java.net.URL("mailto", null, author.getEmailAddress))
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

}