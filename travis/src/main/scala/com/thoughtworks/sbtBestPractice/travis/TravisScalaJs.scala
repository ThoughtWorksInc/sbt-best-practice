package com.thoughtworks.sbtBestPractice.travis

import sbt.{AutoPlugin, Def}
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

/**
  * @author 杨博 (Yang Bo)
  */
object TravisScalaJs extends AutoPlugin {

  override def requires = Travis && ScalaJSPlugin

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    scalaJSLinkerConfig := {
      scalaJSLinkerConfig.value.withBatchMode(Travis.travisRepoSlug.?.value.isDefined)
    }
  )
  override def trigger = allRequirements

}
