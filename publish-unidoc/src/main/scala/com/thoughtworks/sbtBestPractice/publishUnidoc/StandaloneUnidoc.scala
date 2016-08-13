package com.thoughtworks.sbtBestPractice.publishUnidoc

import com.thoughtworks.sbtBestPractice.travis.Travis
import com.thoughtworks.sbtBestPractice.scalacOptions.ScaladocTitle
import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin
import sbtunidoc.Plugin.UnidocKeys

object StandaloneUnidoc extends AutoPlugin {

  override def requires: Plugins = JvmPlugin && Travis && ScaladocTitle

  override def projectSettings = sbtunidoc.Plugin.scalaJavaUnidocSettings ++ Seq(
    doc in Compile := (UnidocKeys.unidoc in Compile).value.head,
    publishArtifact in packageSrc := false,
    publishArtifact in packageBin := false,
    scalacOptions in Compile in doc := {
      val originalScalacOptions = (scalacOptions in Compile in doc).value
      originalScalacOptions.indexOf("-doc-title") match {
        case -1 =>
          originalScalacOptions ++ Seq("-doc-title", Travis.travisRepoSlug.value)
        case i =>
          originalScalacOptions.patch(i + 1, Seq(Travis.travisRepoSlug.value), 1)
      }
    }
  )

}