package com.thoughtworks.sbtBestPractice.publishUnidoc

import com.thoughtworks.sbtBestPractice.travis.Travis
import com.thoughtworks.sbtBestPractice.scalacOptions.ScaladocTitle
import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin
import sbtunidoc.Plugin.UnidocKeys

object StandaloneUnidoc extends AutoPlugin {

  override def requires: Plugins = JvmPlugin && ScaladocTitle

  override def projectSettings = sbtunidoc.Plugin.scalaJavaUnidocSettings ++ Seq(
    doc in Compile := (UnidocKeys.unidoc in Compile).value.head,
    publishArtifact in packageSrc := false,
    publishArtifact in packageBin := false
  )

}