package com.thoughtworks.sbtBestPractice

import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode
import org.eclipse.jgit.api.Git
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

  object autoImport {

    sealed trait GitCredential

    final case class PersonalAccessToken(token: String) extends GitCredential

    final case class SshKey(privateKeyFile: File) extends GitCredential

    val githubCredential = SettingKey[GitCredential]("github-credential", "Credential for git push")

    val travisGitConfig = TaskKey[Unit]("travis-git-config", "configure git from Travis environment variables")

  }

  import autoImport._

  override def projectSettings = Seq(
    travisGitConfig := {
      for (git <- managed(Git.open(baseDirectory.value))) {
        val travisBranch = "master" // sys.env("TRAVIS_BRANCH")
        val remoteSetUrl = git.remoteSetUrl();
        {
          import remoteSetUrl._
          setName("origin")
          setPush(true)
          githubCredential.value match {
            case PersonalAccessToken(key) =>
              setUri(new URIish(s"https://$key@github.com/$travisBranch.git"))
            case SshKey(privateKeyFile) =>
              setUri(new URIish(s"ssh://git@github.com:$travisBranch.git"))
          }
        }
        remoteSetUrl.call()

        git.branchCreate().
          setForce(true).
          setUpstreamMode(SetupUpstreamMode.TRACK).
          setName(travisBranch).
          setStartPoint(s"origin/$travisBranch")

        git.checkout().
          setName(travisBranch).
          call()
      }
    }
  )


}
