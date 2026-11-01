package com.thoughtworks.sbtBestPractice.publishUnidoc

import sbt._
import sbt.Keys._
import sbtunidoc.BaseUnidocPlugin

object UnidocProject extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  override def requires: Plugins = BaseUnidocPlugin

  override def projectSettings = Seq(
    ThisBuild / PackageUnidoc.autoImport.unidocProject := Some(
      thisProjectRef.value
    )
  )

}
