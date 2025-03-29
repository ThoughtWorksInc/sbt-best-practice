package com.thoughtworks.sbtBestPractice.travis

import java.io.File

import org.eclipse.jgit.lib.ConfigConstants._
import org.eclipse.jgit.lib.Constants._
import org.eclipse.jgit.transport.URIish
import resource._
import sbt._
import com.thoughtworks.sbtBestPractice.git.{Git => GitPlugin}
import sbt.Keys._

import scala.sys.process.Process

object TravisGithub extends AutoPlugin {

  object autoImport {

    sealed trait GitCredential

    final case class PersonalAccessToken(token: String) extends GitCredential

    final case class SshKey(privateKeyFile: File) extends GitCredential

    val githubCredential =
      SettingKey[GitCredential]("github-credential", "Credential for git push")

    val travisGitConfig = TaskKey[Unit](
      "travis-git-config",
      "Configure git from Travis environment variables"
    )

  }

  import autoImport._

  private val RemoteName = "origin"

  override def trigger = allRequirements

  override def requires = Travis && GitPlugin

  override def projectSettings = Seq(
    travisGitConfig := {
      (Travis.travisBranch.?.value, Travis.travisRepoSlug.?.value) match {
        case (Some(branch), Some(slug)) =>
          val credential = githubCredential.?.value
          for (
            repository <- managed(GitPlugin.gitRepositoryBuilder.value.build());
            git <- managed(org.eclipse.jgit.api.Git.wrap(repository))
          ) {
            {
              val command = git.remoteSetUrl()
              command.setName(RemoteName)
              command.setPush(true)
              credential match {
                case Some(PersonalAccessToken(key)) =>
                  command.setUri(
                    new URIish(s"https://$key@github.com/$slug.git")
                  )
                case Some(SshKey(privateKeyFile)) =>
                  command.setUri(new URIish(s"ssh://git@github.com:$slug.git"))
                case _ =>
                  throw new MessageOnlyException("githubCredential is not set")
              }
              command.call()
            }

            git.branchCreate().setForce(true).setName(branch).call()

            {
              val config = git.getRepository.getConfig
              config.setString(
                CONFIG_BRANCH_SECTION,
                branch,
                CONFIG_KEY_REMOTE,
                RemoteName
              )
              config.setString(
                CONFIG_BRANCH_SECTION,
                branch,
                CONFIG_KEY_MERGE,
                raw"""$R_HEADS$branch"""
              )
              config.save()
            }

            git.checkout().setName(branch).call()
          }
        case _ =>
          throw new MessageOnlyException(
            "travisBranch or travisRepoSlug is not set"
          )
      }
    }
  )

}
