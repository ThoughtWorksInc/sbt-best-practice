package com.thoughtworks.sbtBestPractice.scalacOptions

import sbt.Keys.scalacOptions
import sbt._
import sbt.plugins.JvmPlugin

/** @author
  *   杨博 (Yang Bo)
  */
object CheckInit extends AutoPlugin {

  override def requires = JvmPlugin

  override def trigger = allRequirements

  override def projectSettings = Seq(
    scalacOptions in Test += "-Xcheckinit"
  )

}
