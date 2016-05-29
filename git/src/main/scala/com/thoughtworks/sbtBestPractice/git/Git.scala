package com.thoughtworks.sbtBestPractice.git

import org.eclipse.jgit.lib.RepositoryBuilder
import sbt.Keys._
import sbt._
import scala.collection.JavaConverters._

/**
  * Detect GIT directories from git log
  */
object Git extends AutoPlugin {

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
        val repository = gitRepositoryBuilder.value.build
        try {
          val git = org.eclipse.jgit.api.Git.wrap(repository)
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