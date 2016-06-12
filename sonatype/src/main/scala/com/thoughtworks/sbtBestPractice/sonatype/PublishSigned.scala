package com.thoughtworks.sbtBestPractice.sonatype

import com.typesafe.sbt.SbtPgp
import com.typesafe.sbt.pgp.PgpKeys
import sbt.AutoPlugin
import sbtrelease.ReleasePlugin.autoImport._
import xerial.sbt.Sonatype

/**
  * Sets-up [[sbtrelease.ReleasePlugin.autoImport#releasePublishArtifactsAction]] for all projects
  * regardless [[sbtrelease.ReleasePlugin]] is enabled.
  * @author 杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object PublishSigned  extends AutoPlugin {

  override def trigger = noTrigger

  override def requires = Sonatype && SbtPgp

  override def projectSettings = Seq(
    releasePublishArtifactsAction := PgpKeys.publishSigned.value
  )

}