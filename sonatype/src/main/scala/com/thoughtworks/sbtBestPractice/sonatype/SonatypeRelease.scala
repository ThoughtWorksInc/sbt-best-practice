package com.thoughtworks.sbtBestPractice.sonatype

import com.typesafe.sbt.SbtPgp
import com.typesafe.sbt.pgp.PgpKeys
import sbt._
import sbtrelease.ReleasePlugin
import sbtrelease.ReleasePlugin.autoImport._
import xerial.sbt.Sonatype
import sbtrelease.ReleaseStateTransformations._

object SonatypeRelease extends AutoPlugin {

  override def trigger = noTrigger

  override def requires = Sonatype && ReleasePlugin && SbtPgp

  override def projectSettings = Seq(
    releaseProcess := {
      releaseProcess.value.patch(releaseProcess.value.indexOf(pushChanges),
                                 Seq[ReleaseStep](releaseStepCommand(Sonatype.SonatypeCommand.sonatypeRelease)),
                                 0)
    }
  )

}
