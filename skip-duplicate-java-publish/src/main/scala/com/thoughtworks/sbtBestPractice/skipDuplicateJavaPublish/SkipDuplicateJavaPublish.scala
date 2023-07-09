package com.thoughtworks.sbtBestPractice.skipDuplicateJavaPublish

import sbt.Keys._
import sbt.{AutoPlugin, Def}

/** Skip duplicate publish.
  *
  * If cross paths are disabled, then publish will be skipped, unless current
  * scala version is the same as the first cross scala version.
  *
  * @author
  *   杨博 (Yang Bo)
  */
object SkipDuplicateJavaPublish extends AutoPlugin {
  override def trigger = allRequirements

  override def projectSettings: Seq[Def.Setting[_]] = {
    publish / skip := {
      (publish / skip).value || {
        crossScalaVersions.value match {
          case Seq(firstScalaVersion, _*) if !crossPaths.value =>
            scalaVersion.value != firstScalaVersion
          case _ =>
            false
        }
      }
    }
  }
}
