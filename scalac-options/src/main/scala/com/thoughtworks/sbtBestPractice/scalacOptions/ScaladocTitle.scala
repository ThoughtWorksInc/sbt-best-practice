package com.thoughtworks.sbtBestPractice.scalacOptions

import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin

/** @author
  *   杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object ScaladocTitle extends AutoPlugin {

  override def requires = JvmPlugin

  override def trigger = allRequirements

  override val projectSettings = Seq(
    scalacOptions in Compile in doc ++= {
      Seq("-doc-title", name.value)
    },
    scalacOptions in Compile in doc ++= {
      Seq("-doc-version", version.value)
    }
  )

}
