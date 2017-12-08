package com.thoughtworks.sbtBestPractice.sonatype

import com.typesafe.sbt.SbtPgp
import sbt.Keys._
import sbt._
import sbtrelease.ReleasePlugin
import sbtrelease.ReleasePlugin.autoImport._
import xerial.sbt.Sonatype
import sbtrelease.ReleaseStateTransformations._

object SonatypeRelease extends AutoPlugin {

  override def trigger = noTrigger

  override def requires = Sonatype && ReleasePlugin && SbtPgp

  override def projectSettings = Seq(
    publishTo := Some(
      if (isSnapshot.value)
        Opts.resolver.sonatypeSnapshots
      else
        Opts.resolver.sonatypeStaging
    ),
    releaseProcess := {
      releaseProcess.value.patch(releaseProcess.value.indexOf(pushChanges),
                                 Seq[ReleaseStep](releaseStepCommand(Sonatype.SonatypeCommand.sonatypeRelease)),
                                 0)
    }
  )

}
