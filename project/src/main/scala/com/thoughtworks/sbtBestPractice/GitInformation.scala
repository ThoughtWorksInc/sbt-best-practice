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

  override def trigger = noTrigger

  val gitDir = SettingKey[Option[File]]("git-dir", ".git directory for current project")

  val gitWorkTree = SettingKey[Option[File]]("git-work-tree", "Root work tree of current project")

  val gitRepositoryBuilder = SettingKey[RepositoryBuilder]("git-repository-builder", "")

  override def projectSettings = Seq(
    gitRepositoryBuilder := (new RepositoryBuilder).findGitDir(baseDirectory.value),
    gitWorkTree := Option(gitRepositoryBuilder.value.getWorkTree),
    gitDir := Option(gitRepositoryBuilder.value.getGitDir),
    homepage := {
      if (gitDir.value.isDefined ) {
        val git = Git.wrap(gitRepositoryBuilder.value.build)
        try {
          val remoteName = git.getRepository.getConfig.getString(CONFIG_BRANCH_SECTION, git.getRepository.getBranch, CONFIG_KEY_REMOTE)
          val Some(remote) = git.remoteList().call().asScala.find(_.getName == remoteName)
          val url = remote.getURIs.asScala.head
          Some(new URL("https", url.getHost, url.getPath match {
            case abstractPath if abstractPath.startsWith("/") =>
              abstractPath
            case relativePath =>
              raw"""/$relativePath"""
          }))
        } finally {
          git.close()
        }
      } else {
        homepage.value
      }
    },
    scmInfo := {
      if (gitDir.value.isDefined ) {
        val git = Git.wrap(gitRepositoryBuilder.value.build)
        try {
          val remoteName = git.getRepository.getConfig.getString(CONFIG_BRANCH_SECTION, git.getRepository.getBranch, CONFIG_KEY_REMOTE)
          val Some(remote) = git.remoteList().call().asScala.find(_.getName == remoteName)
          val url = remote.getURIs.asScala.head
          Some(ScmInfo(
            new URL("https", url.getHost, url.getPath match {
              case abstractPath if abstractPath.startsWith("/") =>
                abstractPath
              case relativePath =>
                raw"""/$relativePath"""
            }),
            url.toString,
            None
          ))
        } finally {
          git.close()
        }
      } else {
        scmInfo.value
      }
    },
    developers ++= {
      if (gitDir.value.isDefined ) {
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