package com.thoughtworks.sbtBestPractice.subdirectoryOrganization

import sbt.Keys.{baseDirectory, organization}
import sbt._

/**
  * @author 杨博 (Yang Bo)
  */
object SubdirectoryOrganization extends AutoPlugin {

  override def trigger = allRequirements

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    organization := {
      baseDirectory.value.relativeTo((ThisBuild / baseDirectory).value) match {
        case None =>
          organization.value
        case Some(relativeBaseDirectory) =>
          relativeBaseDirectory.getParent match {
            case null =>
              organization.value
            case parent =>
              parent.split('/').map(Project.normalizeModuleID).mkString(organization.value, ".", "")
          }
      }
    }
  )

}
