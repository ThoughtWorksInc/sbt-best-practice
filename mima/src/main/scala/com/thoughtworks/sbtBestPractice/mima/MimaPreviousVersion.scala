package com.thoughtworks.sbtBestPractice.mima

import com.typesafe.tools.mima.plugin.MimaPlugin
import sbt.Keys.projectID
import sbt.{AutoPlugin, Def, ModuleID}
import sbtdynver.DynVerPlugin

/** @author
  *   杨博 (Yang Bo)
  */
object MimaPreviousVersion extends AutoPlugin {

  override def trigger = allRequirements

  override def requires = DynVerPlugin && MimaPlugin

  import DynVerPlugin.autoImport._
  import MimaPlugin.autoImport._

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(
      mimaPreviousArtifacts ++= {
        val currentModuleID =
          projectID.value.withExplicitArtifacts(Vector.empty)
        previousStableVersion.value
          .map(currentModuleID.withRevision)
          .toSet: Set[ModuleID]
      }
    )

}
