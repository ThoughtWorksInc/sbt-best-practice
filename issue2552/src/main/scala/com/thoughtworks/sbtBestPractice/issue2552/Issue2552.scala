package com.thoughtworks.sbtBestPractice.issue2552

import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

/**
  * @see [[https://github.com/sbt/sbt/issues/2552]]
  */
object Issue2552 extends AutoPlugin {

  override def requires = JvmPlugin

  override def trigger: PluginTrigger = allRequirements

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    includeFilter in unmanagedSources := (includeFilter in unmanagedSources).value && new SimpleFileFilter(_.isFile)
  )

}
