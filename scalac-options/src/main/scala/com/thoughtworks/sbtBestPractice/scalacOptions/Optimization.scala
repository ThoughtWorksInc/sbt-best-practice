package com.thoughtworks.sbtBestPractice.scalacOptions

import com.thoughtworks.sbtBestPractice.scalacOptions.ScalacWarnings.allRequirements
import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin

/**
  * @author 杨博 (Yang Bo)
  */
object Optimization extends AutoPlugin {

  override def requires: Plugins = JvmPlugin

  override def trigger: PluginTrigger = noTrigger

  override def buildSettings = Seq(
    scalacOptions ++= {
      import scala.math.Ordering.Implicits._
      val versionNumers = VersionNumber(scalaVersion.value).numbers
      if (versionNumers < Seq(2L, 12L)) {
        Seq("-optimize", "-Yinline-warnings")
      } else if (versionNumers < Seq(2L, 12L, 3L)) {
        Seq("-opt:l:project")
      } else {
        Seq("-opt:l:inline", "-opt-inline-from:<sources>")
      }
    }
  )

}
