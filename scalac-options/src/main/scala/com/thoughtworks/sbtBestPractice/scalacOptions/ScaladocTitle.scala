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
    Compile / doc / scalacOptions ++= {
      Seq("-doc-title", name.value)
    },
    Compile / doc / scalacOptions ++= {
      Seq("-doc-version", version.value)
    }
  )

}
