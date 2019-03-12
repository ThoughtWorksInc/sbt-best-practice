package com.thoughtworks.sbtBestPractice.sonatype

import sbt._
import ch.epfl.scala.sbt.release.AutoImported._
import ch.epfl.scala.sbt.release.ReleaseEarlyPlugin
import xerial.sbt.Sonatype

/**
  * @author 杨博 (Yang Bo)
  */
object SonatypeReleaseEarly extends AutoPlugin {
  override def trigger = allRequirements
  override def requires = Sonatype && ReleaseEarlyPlugin

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    releaseEarlyWith := SonatypePublisher,
    Keys.aggregate in releaseEarly := false,
  )
}
