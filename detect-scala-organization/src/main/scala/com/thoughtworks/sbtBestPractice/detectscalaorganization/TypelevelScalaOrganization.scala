package com.thoughtworks.sbtBestPractice.detectscalaorganization

import sbt.plugins.JvmPlugin
import sbt._, Keys._

object TypelevelScalaOrganization extends AutoPlugin {
  override def trigger = allRequirements

  override def requires = JvmPlugin

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    updateSbtClassifiers / scalaOrganization := {
      (Global / scalaOrganization).value
    },
    scalaOrganization := {
      val unchanged = scalaOrganization.value
      scalaVersion.value match {
        case VersionNumber(_, tags, _) if tags.contains("typelevel") =>
          "org.typelevel"
        case _ =>
          unchanged
      }
    }
  )
}
