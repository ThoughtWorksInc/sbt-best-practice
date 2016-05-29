package com.thoughtworks.sbtBestPractice.travis

import java.io.File

import org.eclipse.jgit.lib.ConfigConstants._
import org.eclipse.jgit.lib.Constants._
import org.eclipse.jgit.transport.URIish
import resource._
import sbt._
import com.thoughtworks.sbtBestPractice.git.{Git => GitPlugin}
import sbt.Keys._
import sbtrelease.{Git => GitVcs, ReleasePlugin}
import sbtrelease.ReleasePlugin.autoImport._

object TravisRelease extends AutoPlugin {

  object autoImport {

    sealed trait GitCredential

    final case class PersonalAccessToken(token: String) extends GitCredential

    final case class SshKey(privateKeyFile: File) extends GitCredential

    val githubCredential = SettingKey[GitCredential]("github-credential", "Credential for git push")

  }

  import autoImport._

  private val RemoteName = "origin"

  val travisGitConfig = TaskKey[Unit]("travis-git-config", "configure git from Travis environment variables")

  override def trigger = allRequirements

  override def requires = Travis && ReleasePlugin && GitPlugin

  override def projectSettings = Seq(

    travisGitConfig := {
      val branch = Travis.travisBranch.value
      val slug = Travis.travisRepoSlug.value
      for (repository <- managed(GitPlugin.gitRepositoryBuilder.value.build()); git <- managed(org.eclipse.jgit.api.Git.wrap(repository))) {
        {
          val command = git.remoteSetUrl()
          command.setName(RemoteName)
          command.setPush(true)
          githubCredential.value match {
            case PersonalAccessToken(key) =>
              command.setUri(new URIish(s"https://$key@github.com/$slug.git"))
            case SshKey(privateKeyFile) =>
              command.setUri(new URIish(s"ssh://git@github.com:$slug.git"))
          }
          command.call()
        }

        git.branchCreate().
          setForce(true).
          setName(branch).
          call()

        {
          val config = git.getRepository.getConfig
          config.setString(CONFIG_BRANCH_SECTION, branch, CONFIG_KEY_REMOTE, RemoteName)
          config.setString(CONFIG_BRANCH_SECTION, branch, CONFIG_KEY_MERGE, raw"""$R_HEADS$branch""")
          config.save()
        }

        git.checkout().
          setName(branch).
          call()

      }
    },
    releaseProcess := {
      if (GitPlugin.gitDir.value.isDefined && Travis.travisRepoSlug.?.value.isDefined) {
        ReleaseStep(releaseStepTask(travisGitConfig)) +: releaseProcess.value
      } else {
        releaseProcess.value
      }
    },
    releaseVcs := {
      Some(new GitVcs(baseDirectory.value) {
        override def isBehindRemote = {
          Travis.travisRepoSlug.?.value match {
            case None => super.isBehindRemote
            case Some(_) => false
          }
        }

        override def hasUpstream = {
          Travis.travisRepoSlug.?.value match {
            case None => super.hasUpstream
            case Some(_) => true
          }
        }

        override def currentHash = Travis.travisCommit.?.value.getOrElse((super.currentHash))

        override def currentBranch = Travis.travisBranch.?.value.getOrElse(super.currentBranch)

        override def cmd(args: Any*) = {
          args match {
            case Seq("push", rest@_*) =>
              githubCredential.value match {
                case SshKey(privateKeyFile) =>
                  Process(executableName(commandName) +: args.map(_.toString), baseDir, "GIT_SSH_COMMAND" -> raw"""ssh -i "${privateKeyFile.getAbsolutePath}" """)
                case PersonalAccessToken(key) =>
                  super.cmd("push" +: "--quiet" +: rest: _*)
              }
            case _ =>
              super.cmd(args: _*)
          }
        }
      })
    }
  )

}