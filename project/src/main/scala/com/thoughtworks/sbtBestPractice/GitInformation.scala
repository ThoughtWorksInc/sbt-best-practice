package com.thoughtworks.sbtBestPractice

import org.eclipse.jgit.api.Git
import sbt._
import sbt.Keys._
import scala.collection.JavaConverters._

/**
  * Fill project informations from git log
  */
object GitInformation extends AutoPlugin {

  override def trigger = allRequirements

  override def projectSettings = Seq(
    developers := {
      val git = Git.open(baseDirectory.value)
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
    }
  )

}