package com.thoughtworks.sbtBestPractice

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.ConfigConstants._
import org.eclipse.jgit.lib.Constants._
import sbt._
import sbt.Keys._
import org.eclipse.jgit.transport.URIish
import resource.managed

/**
  * Configure git from Travis environment variables
  *
  * @author 杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object TravisGitConfig extends AutoPlugin {

  override def requires = TravisEnvironmentVariables

  object autoImport {

    sealed trait GitCredential

    final case class PersonalAccessToken(token: String) extends GitCredential

    final case class SshKey(privateKeyFile: File) extends GitCredential

    val githubCredential = SettingKey[GitCredential]("github-credential", "Credential for git push")

    val travisGitConfig = TaskKey[Unit]("travis-git-config", "configure git from Travis environment variables")

  }

  import autoImport._
  import TravisEnvironmentVariables.autoImport._

  private val RemoteName = "origin"

  override def projectSettings = Seq(
    travisGitConfig := {
      val branch = travisBranch.value
      val slug = travisRepoSlug.value
      for (git <- managed(Git.open(baseDirectory.value))) {
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
    }
  )


}
