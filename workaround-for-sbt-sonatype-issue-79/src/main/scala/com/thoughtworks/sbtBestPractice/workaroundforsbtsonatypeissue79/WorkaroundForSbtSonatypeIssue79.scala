package com.thoughtworks.sbtBestPractice.workaroundforsbtsonatypeissue79
import sbt.Keys._
import sbt._
import xerial.sbt.Sonatype.SonatypeKeys._

/**
  * @author 杨博 (Yang Bo)
  */
object WorkaroundForSbtSonatypeIssue79 extends AutoPlugin {
  override def trigger = allRequirements
  override def requires = super.requires
  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    sonatypeDefaultResolver := {
      val sonatypeRepo = "https://oss.sonatype.org/"
      val profileM = sonatypeStagingRepositoryProfile.?.value

      val staged = profileM.map { stagingRepoProfile =>
        "releases" at sonatypeRepo +
          "service/local/staging/deployByRepositoryId/" +
          stagingRepoProfile.repositoryId
      }
      staged.getOrElse(if (isSnapshot.value) {
        Opts.resolver.sonatypeSnapshots
      } else {
        Opts.resolver.sonatypeStaging
      })
    }
  )
}
