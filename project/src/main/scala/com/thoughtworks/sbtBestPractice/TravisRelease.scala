package com.thoughtworks.sbtBestPractice

import java.io.File

import com.thoughtworks.sbtBestPractice.TravisGitConfig.autoImport._
import sbt._
import sbt.Keys._
import sbtrelease.{Git, ReleasePlugin, Vcs}
import sbtrelease.ReleasePlugin.autoImport._
import sbtrelease.ReleaseStateTransformations._


object TravisRelease extends AutoPlugin {

  override def trigger = allRequirements

  override def requires = TravisGitConfig && ReleasePlugin

  override def projectSettings = Seq(
    releaseVcs := {
      Some(new Git(baseDirectory.value) {

        override def isBehindRemote = {
          TravisEnvironmentVariables.travisRepoSlug.?.value match {
            case None => super.isBehindRemote
            case Some(_) => false
          }
        }

        override def hasUpstream = {
          TravisEnvironmentVariables.travisRepoSlug.?.value match {
            case None => super.hasUpstream
            case Some(_) => true
          }
        }

        override def trackingRemote = {
          TravisEnvironmentVariables.travisRepoSlug.?.value match {
            case None => super.trackingRemote
            case Some(slug) =>
              githubCredential.value match {
                case _: SshKey =>
                  raw"""git@github.com:$slug.git"""
                case _: PersonalAccessToken =>
                  raw"""https://github.com/$slug.git"""
              }
          }
        }

        override def currentHash = TravisEnvironmentVariables.travisCommit.?.value.getOrElse((super.currentHash))

        override def currentBranch = TravisEnvironmentVariables.travisBranch.?.value.getOrElse(super.currentBranch)

        override def cmd(args: Any*) = {
          githubCredential.value match {
            case SshKey(privateKeyFile) =>
              Process(executableName(commandName) +: args.map(_.toString), baseDir, "GIT_SSH_COMMAND" -> raw"""ssh -i "${privateKeyFile.getAbsolutePath}" """)
            case PersonalAccessToken(key) =>
              super.cmd(args: _*)
          }
        }
      })
    }
  )

}