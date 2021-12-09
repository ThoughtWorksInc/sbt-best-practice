package com.thoughtworks.sbtBestPractice.scalacOptions

import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin

/** @author
  *   杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object ScaladocFeatures extends AutoPlugin {

  override def requires = JvmPlugin

  override def trigger = allRequirements

  override val projectSettings = Seq(
    Compile / doc / scalacOptions ++= Seq("-doc-root-content", (baseDirectory.value / "README.md").toString()),
    scalacOptions in Compile in doc += "-groups",
    scalacOptions in Compile in doc += "-diagrams",
    scalacOptions in Compile in doc += "-implicits",
    scalacOptions in Compile in doc ++= {
      if (scalaBinaryVersion.value == "2.10") {
        Seq()
      } else {
        Seq("-author")
      }
    }
  )
}
