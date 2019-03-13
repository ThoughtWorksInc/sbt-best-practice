package com.thoughtworks.sbtBestPractice.publishUnidoc
import sbt.plugins.JvmPlugin
import sbt._
import Keys._
import com.thoughtworks.sbtBestPractice.scalacOptions.ScaladocTitle
import com.thoughtworks.sbtBestPractice.travis.Travis
import sbtunidoc.ScalaUnidocPlugin

/**
  * @author 杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object UnidocTitle extends AutoPlugin {
  override def requires: Plugins = Travis && JvmPlugin && ScalaUnidocPlugin && ScaladocTitle

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
