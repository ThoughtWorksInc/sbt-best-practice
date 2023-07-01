package com.thoughtworks.sbtBestPractice.scalacOptions

import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

/** Enable scalac warnings
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
      VersionNumber(scalaVersion.value).numbers match {
        case numbers if numbers >= Seq(3L) =>
          None
        case numbers if numbers >= Seq(2L, 13L) =>
          Some("-Xlint:infer-any")
        case numbers if numbers >= Seq(2L, 11L) =>
          Some("-Ywarn-infer-any")
        case _ =>
          None
      }
    }
  )

}
