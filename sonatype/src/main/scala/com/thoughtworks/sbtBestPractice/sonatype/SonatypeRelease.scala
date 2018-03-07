package com.thoughtworks.sbtBestPractice.sonatype

import com.thoughtworks.sbtBestPractice.scalacOptions.Optimization
import com.typesafe.sbt.SbtPgp
import sbt.{Def, _}
import sbtrelease.ReleasePlugin
import sbtrelease.ReleasePlugin.autoImport._
import xerial.sbt.Sonatype
import sbtrelease.ReleaseStateTransformations._

object SonatypeRelease extends AutoPlugin {

  override def trigger = noTrigger

  override def requires = Sonatype && ReleasePlugin && SbtPgp

  override def buildSettings: Seq[Def.Setting[_]] = Seq(
    Optimization.autoImport.optimization := true
  )

  override def projectSettings = Seq(
    releaseProcess := {
      releaseProcess.value.patch(releaseProcess.value.indexOf(pushChanges),
                                 Seq[ReleaseStep](releaseStepCommand(Sonatype.SonatypeCommand.sonatypeRelease)),
                                 0)
    }
  )

}
