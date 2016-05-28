package com.thoughtworks.sbtBestPractice.git

import org.eclipse.jgit.api.Git
import sbt.Keys._
import sbt._
import scala.collection.JavaConverters._

/**
  * Fill developers from git log
  */
object GitDevelopers extends AutoPlugin {

  override def trigger = allRequirements

  override def requires = GitInformation

  import GitInformation._

  override def projectSettings = Seq(
    developers ++= {
      if (gitDir.value.isDefined) {
        val repository = gitRepositoryBuilder.value.build
        try {
          val git = Git.wrap(repository)
          try {
            (for {
              commit <- git.log().call().asScala
            } yield {
              val author = commit.getAuthorIdent
              Developer("", author.getName, author.getEmailAddress, new java.net.URL("mailto", null, author.getEmailAddress))
            }).toSet.toList
          } finally {
            git.close()
          }
        } finally {
          repository.close()
        }
      } else {
        Nil
      }
    }
  )

}