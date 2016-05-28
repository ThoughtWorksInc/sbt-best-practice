package com.thoughtworks.sbtBestPractice

import resource._
import sbt._
import sbt.Keys._
import sbtrelease.ReleasePlugin
import sbtrelease.ReleasePlugin.autoImport._


object DisableDeploy extends AutoPlugin {

  val disableDeploy = TaskKey[Unit]("disable-deploy", "Rename deploy.sbt to deploy.sbt.disabled.")

  override def trigger = allRequirements

  override def requires = ReleasePlugin && GitInformation

  override def projectSettings = Seq(

    disableDeploy := {
      val log = (streams in disableDeploy).value.log
      val fromFile = baseDirectory.value / "deploy.sbt"
      val toFile = baseDirectory.value / "deploy.sbt.disabled"
      IO.move(fromFile, toFile)
      for (repository <- managed(GitInformation.gitRepositoryBuilder.value.build()); git <- managed(org.eclipse.jgit.api.Git.wrap(repository))) {
        git.add().addFilepattern(fromFile.toString).addFilepattern(toFile.toString).call()
      }
    },

    releaseProcess := {
      import ReleaseTransformations._
      releaseProcess.value.patch(releaseProcess.value.indexOf(commitReleaseVersion), Seq[ReleaseStep](releaseStepTask(disableDeploy)), 0)
    }

  )

}