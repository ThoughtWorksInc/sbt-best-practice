package com.thoughtworks.sbtBestPractice.subdirectoryOrganization

import sbt.Keys._
import sbt._
import scala.collection.JavaConverters._

/** @author
  *   杨博 (Yang Bo)
  */
object SubdirectoryOrganization extends AutoPlugin {

  override def trigger = allRequirements

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    organization := {
      baseDirectory.value.relativeTo((ThisBuild / baseDirectory).value) match {
        case None =>
          organization.value
        case Some(relativeBaseDirectory) =>
          val pathElements = relativeBaseDirectory.toPath.asScala.toSeq
          pathElements.lastIndexWhere(_.toString == name.value) match {
            case -1 =>
              organization.value
            case pathDepth =>
              val parentPathElements = pathElements.view(0, pathDepth)
              (organization.value match {
                case "" =>
                  parentPathElements
                case oldOrganization =>
                  oldOrganization +: parentPathElements
              }).mkString(".")
          }
      }
    }
  )

}
