package com.thoughtworks.sbtBestPractice.sonatype

import com.typesafe.sbt.SbtPgp
import com.typesafe.sbt.pgp.PgpKeys
import sbt.Keys.{isSnapshot, publishTo}
import sbt.{AutoPlugin, Opts}
import sbtrelease.ReleasePlugin.autoImport._
import xerial.sbt.Sonatype

/**
  * Sets-up [[sbtrelease.ReleasePlugin.autoImport#releasePublishArtifactsAction]] for all projects
  * regardless [[sbtrelease.ReleasePlugin]] is enabled.
  * @author 杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object SonatypePublish extends AutoPlugin {

  override def trigger = allRequirements

  override def requires = Sonatype && SbtPgp

  override def projectSettings = Seq(
    publishTo := Some(
      if (isSnapshot.value)
        Opts.resolver.sonatypeSnapshots
      else
        Opts.resolver.sonatypeStaging
    ),
    releasePublishArtifactsAction := PgpKeys.publishSigned.value
  )

}
