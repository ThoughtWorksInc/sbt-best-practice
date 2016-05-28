package com.thoughtworks.sbtBestPractice

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.ConfigConstants._
import org.eclipse.jgit.lib.RepositoryBuilder
import sbt._
import sbt.Keys._

import scala.collection.JavaConverters._

/**
  * Fill project informations from git log
  */
object GitInformation extends AutoPlugin {

  override def trigger = allRequirements

  val gitDir = SettingKey[Option[File]]("git-dir", ".git directory for current project")

  val gitWorkTree = SettingKey[Option[File]]("git-work-tree", "Root work tree of current project")

  val gitRepositoryBuilder = SettingKey[RepositoryBuilder]("git-repository-builder", "")

  override def projectSettings = Seq(
    gitRepositoryBuilder := {
      val builder = (new RepositoryBuilder).findGitDir(baseDirectory.value)
      builder.setup()
      builder
    },
    gitWorkTree := Option(gitRepositoryBuilder.value.getWorkTree),
    gitDir := Option(gitRepositoryBuilder.value.getGitDir),
    developers ++= {
      if (gitDir.value.isDefined) {
        val git = Git.wrap(gitRepositoryBuilder.value.build)
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
      } else {
        Nil
      }
    }
  )

}