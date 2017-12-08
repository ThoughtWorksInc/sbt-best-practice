package com.thoughtworks.sbtBestPractice.publishUnidoc

import com.thoughtworks.sbtBestPractice.scalacOptions.ScaladocTitle
import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin
import sbtunidoc.UnidocKeys

object StandaloneUnidoc extends AutoPlugin with UnidocKeys {

  override def requires: Plugins =
    JvmPlugin && ScaladocTitle && sbtunidoc.ScalaUnidocPlugin && sbtunidoc.JavaUnidocPlugin

  override def projectSettings = Seq(
    PackageUnidoc.autoImport.unidocProject in ThisBuild := Some(thisProjectRef.value),
    publishArtifact := false
  )

}
