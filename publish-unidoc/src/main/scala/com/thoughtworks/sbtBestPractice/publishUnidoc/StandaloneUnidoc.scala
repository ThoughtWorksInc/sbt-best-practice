package com.thoughtworks.sbtBestPractice.publishUnidoc

import com.thoughtworks.sbtBestPractice.scalacOptions.ScaladocTitle
import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin

object StandaloneUnidoc extends AutoPlugin {

  override def requires: Plugins = JvmPlugin && ScaladocTitle

  override def projectSettings = sbtunidoc.Plugin.scalaJavaUnidocSettings ++ Seq(
    PackageUnidoc.autoImport.unidocProject in ThisBuild := Some(thisProjectRef.value),
    publishArtifact := false
  )

}
