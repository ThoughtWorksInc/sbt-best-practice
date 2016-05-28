package com.thoughtworks.sbtBestPractice

import com.typesafe.sbt.SbtPgp
import com.typesafe.sbt.pgp.PgpKeys
import sbt._
import sbtrelease.ReleasePlugin
import sbtrelease.ReleasePlugin.autoImport._
import sbtrelease.ReleaseStateTransformations._
import xerial.sbt.Sonatype


object SonatypeRelease extends AutoPlugin {

  override def trigger = allRequirements

  override def requires = Sonatype && ReleasePlugin && SbtPgp

  override def projectSettings = Seq(
    releaseProcess := {
      releaseProcess.value.patch(releaseProcess.value.indexOf(pushChanges), Seq[ReleaseStep](releaseStepCommand(Sonatype.SonatypeCommand.sonatypeRelease)), 0)
    },
    releasePublishArtifactsAction := PgpKeys.publishSigned.value
  )

}