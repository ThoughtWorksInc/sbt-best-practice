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

  val gitSubrepoWorkTree = SettingKey[Option[File]](
    "git-subrepo-work-tree",
    "The nearest directory holding a .gitrepo file (a git-subrepo), searched from baseDirectory up to gitWorkTree; gitWorkTree when there is none"
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
    gitSubrepoWorkTree := gitWorkTree.value.map(_.getCanonicalFile).map {
      workTree =>
        Iterator
          .iterate(baseDirectory.value.getCanonicalFile)(_.getParentFile)
          .takeWhile(directory => directory != null && directory != workTree)
          .find(directory => (directory / ".gitrepo").isFile)
          .getOrElse(workTree)
    },
    gitDir := Option(gitRepositoryBuilder.value.getGitDir)
  )

  override def buildSettings = settings

  override def projectSettings = settings

}
