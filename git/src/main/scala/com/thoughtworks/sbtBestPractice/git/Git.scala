package com.thoughtworks.sbtBestPractice.git

import org.eclipse.jgit.lib.RepositoryBuilder
import sbt.Keys._
import sbt._
import sbt.plugins.CorePlugin

import scala.collection.JavaConverters._

/** Detect GIT directories from git log
  */
object Git extends AutoPlugin {

  override def requires = CorePlugin

  override def trigger = allRequirements

  val gitDir =
    SettingKey[Option[File]]("git-dir", ".git directory for current project")

  val gitWorkTree = SettingKey[Option[File]](
    "git-work-tree",
    "Root work tree of current project"
  )

  val gitRepositoryBuilder =
    SettingKey[RepositoryBuilder]("git-repository-builder", "")

  private def settings = Seq(
    gitRepositoryBuilder := {
      val builder = (new RepositoryBuilder).findGitDir(baseDirectory.value)
      if (builder.getWorkTree != null || builder.getGitDir != null) {
        builder.setup()
      }
      builder
    },
    gitWorkTree := Option(gitRepositoryBuilder.value.getWorkTree),
    gitDir := Option(gitRepositoryBuilder.value.getGitDir)
  )

  override def buildSettings = settings

  override def projectSettings = settings

}
