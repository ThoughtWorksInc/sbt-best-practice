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
    scalacOptions in Compile += "-deprecation",
    scalacOptions += "-unchecked",
    scalacOptions ++= {
      import Ordering.Implicits._
      if (VersionNumber(scalaVersion.value).numbers >= Seq(2L, 11L)) {
        Some("-Ywarn-infer-any")
      } else {
        None
      }
    }
  )

}
