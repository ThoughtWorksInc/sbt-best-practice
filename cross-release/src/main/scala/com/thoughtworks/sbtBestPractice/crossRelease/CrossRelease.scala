package com.thoughtworks.sbtBestPractice.crossRelease

import sbt._
import sbt.Keys._
import sbtrelease._
import sbtrelease.ReleasePlugin.autoImport._

/**
  * Detect if sbt-release should publish versions
  */
object CrossRelease extends AutoPlugin {

  override def trigger = allRequirements

  override def requires = ReleasePlugin

  override def projectSettings = Seq(
    releaseCrossBuild := crossScalaVersions.value.length > 1
  )

}