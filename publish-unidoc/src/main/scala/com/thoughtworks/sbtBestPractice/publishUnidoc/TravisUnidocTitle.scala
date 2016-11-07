package com.thoughtworks.sbtBestPractice.publishUnidoc

import com.thoughtworks.sbtBestPractice.travis.Travis
import sbt.Keys._
import sbt._

/**
  * @author 杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object TravisUnidocTitle extends AutoPlugin {
  override def requires: Plugins = Travis && StandaloneUnidoc

  override def trigger: PluginTrigger = allRequirements

  override def projectSettings = Seq(
    scalacOptions in Compile in doc := {
      val originalScalacOptions = (scalacOptions in Compile in doc).value
      Travis.travisRepoSlug.?.value match {
        case Some(slug) =>
          originalScalacOptions.indexOf("-doc-title") match {
            case -1 =>
              originalScalacOptions ++ Seq("-doc-title", slug)
            case i =>
              originalScalacOptions.updated(i + 1, slug)
          }
        case None =>
          originalScalacOptions
      }
    }
  )

}
