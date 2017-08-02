package com.thoughtworks.sbtBestPractice.sonatype

import com.typesafe.sbt.SbtPgp
import com.typesafe.sbt.pgp.PgpKeys
import sbt._
import sbt.Keys._
import sbtrelease.ReleasePlugin
import sbtrelease.ReleasePlugin.autoImport._
import xerial.sbt.Sonatype
import sbtrelease.ReleaseStateTransformations._

object SonatypeRelease extends AutoPlugin {

  override def trigger = noTrigger

  override def requires = Sonatype && ReleasePlugin && SbtPgp

  override def projectSettings = Seq(
    releaseProcess := {
      releaseProcess.value.patch(releaseProcess.value.indexOf(pushChanges),
                                 Seq[ReleaseStep](releaseStepCommand(Sonatype.SonatypeCommand.sonatypeRelease)),
                                 0)
    },
    apiURL := {
      val projectId = projectID.value
      val fullVersion = scalaVersion.value
      val binaryVersion = scalaBinaryVersion.value
      val makeArtifactName = (artifactName in packageDoc).value
      val docArtifact = (artifact in packageDoc).value
      val fileName = makeArtifactName(ScalaVersion(fullVersion, binaryVersion), projectId, docArtifact)
      val crossName = CrossVersion(projectId.crossVersion, fullVersion, binaryVersion).getOrElse(identity[String] _)
      val moduleName = crossName(projectId.name)
      val organizationName = projectId.organization.replace('.', '/')

      Some(new URL(
        raw"""https://oss.sonatype.org/service/local/repositories/public/archive/$organizationName/$moduleName/${projectId.revision}/$fileName/!/index.html"""))
    }
  )

}
