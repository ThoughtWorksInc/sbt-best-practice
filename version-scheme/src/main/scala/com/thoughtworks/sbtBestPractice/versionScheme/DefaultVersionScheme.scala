package com.thoughtworks.sbtBestPractice.versionScheme

import sbt.AutoPlugin
import sbt._, Keys._

object DefaultVersionScheme extends AutoPlugin {
  override def trigger = allRequirements
  override def buildSettings = Seq(
    ThisBuild / versionScheme := Some(VersionScheme.SemVerSpec)
  )
}
