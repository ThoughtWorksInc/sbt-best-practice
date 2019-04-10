package com.thoughtworks.sbtBestPractice.mima

import com.typesafe.tools.mima.plugin.MimaPlugin
import sbt.Keys._
import sbt._

/**
  * @author 杨博 (Yang Bo)
  */
object MimaSkip extends AutoPlugin {

  override def trigger = allRequirements

  override def requires = MimaPlugin

  import MimaPlugin.autoImport._

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      mimaPreviousClassfiles / skip := (mimaPreviousClassfiles / skip).value || (publish / skip).value || !(Compile / packageBin / publishArtifact).value,
      mimaPreviousClassfiles := Def.taskDyn {
        val default = mimaPreviousClassfiles.taskValue
        if ((mimaPreviousClassfiles / skip).value) {
          Def.task(Map.empty[ModuleID, File])
        } else {
          Def.task(default.value)
        }
      }.value,
    )

}
