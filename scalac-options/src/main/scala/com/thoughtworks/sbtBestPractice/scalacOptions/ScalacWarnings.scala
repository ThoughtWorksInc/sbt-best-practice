package com.thoughtworks.sbtBestPractice.scalacOptions

import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

/**
  * Enable scalac warnings
  */
object ScalacWarnings extends AutoPlugin {

  override def requires = JvmPlugin

  override def trigger = allRequirements

  override def projectSettings = Seq(
    scalacOptions += "-feature",
    scalacOptions += "-deprecation",
    scalacOptions += "-unchecked",
    scalacOptions += "-Ywarn-infer-any"
  )

}
