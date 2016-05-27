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

  object autoImport {

    val isGitDir = SettingKey[Boolean]("is-git-dir", "Determine if current project directory is a git work copy")

  }

  import autoImport._

  override def projectSettings = Seq(
    isGitDir := {
      (new RepositoryBuilder).findGitDir(baseDirectory.value).getGitDir != null
    },
    homepage := {
      if (isGitDir.value) {
        val git = Git.open(baseDirectory.value)
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
      if (isGitDir.value) {
        val git = Git.open(baseDirectory.value)
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
        None
      }
    },
    developers := {
      if (isGitDir.value) {
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
      } else {
        Nil
      }
    }
  )

}