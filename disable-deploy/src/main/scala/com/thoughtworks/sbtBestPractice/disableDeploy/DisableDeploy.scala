package com.thoughtworks.sbtBestPractice.disableDeploy

import sbt.Keys._
import sbt._
import sbtrelease.ReleasePlugin
import sbtrelease.ReleasePlugin._
import sbtrelease.ReleaseStateTransformations._
import sbtrelease.ReleasePlugin.autoImport._
import com.thoughtworks.sbtBestPractice.git.Git
import resource._

object DisableDeploy extends AutoPlugin {

  val disableDeploy = TaskKey[Unit]("disable-deploy", "Rename deploy.sbt to deploy.sbt.disabled.")

  override def trigger = allRequirements

  override def requires = ReleasePlugin && Git

  override def projectSettings = Seq(

    disableDeploy := {
      val log = (streams in disableDeploy).value.log
      val fromFile = baseDirectory.value / "deploy.sbt"
      val toFile = baseDirectory.value / "deploy.sbt.disabled"
      IO.move(fromFile, toFile)
      for (repository <- managed(Git.gitRepositoryBuilder.value.build()); git <- managed(org.eclipse.jgit.api.Git.wrap(repository))) {
        git.add().addFilepattern(toFile.relativeTo(repository.getWorkTree).get.toString).call()
        git.rm().addFilepattern(fromFile.relativeTo(repository.getWorkTree).get.toString).call()
      }
    },

    releaseProcess := {
      releaseProcess.value.patch(releaseProcess.value.indexOf(commitReleaseVersion), Seq[ReleaseStep](releaseStepTask(disableDeploy)), 0)
    }

  )

}