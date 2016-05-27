package com.thoughtworks.sbtBestPractice

import java.io.File

import com.thoughtworks.sbtBestPractice.TravisGitConfig.autoImport._
import sbt._
import sbt.Keys._
import sbtrelease.{Git, ReleasePlugin, Vcs}
import sbtrelease.ReleasePlugin.autoImport._
import sbtrelease.ReleaseStateTransformations._
import xerial.sbt.Sonatype


object SonatypeRelease extends AutoPlugin {

  override def trigger = allRequirements

  override def requires = Sonatype && ReleasePlugin

  override def projectSettings = Seq(
    releaseProcess := {
      releaseProcess.value.patch(releaseProcess.value.indexOf(pushChanges), Seq[ReleaseStep](releaseStepCommand(Sonatype.SonatypeCommand.sonatypeRelease)), 0)
    }
  )

}