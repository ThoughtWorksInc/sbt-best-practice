package com.thoughtworks.sbtBestPractice.travis

import com.thoughtworks.sbtBestPractice.git.{Git => GitPlugin}
import org.eclipse.jgit.lib.Constants
import sbt.Keys._
import sbt._

/** @author
  *   杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object ScaladocSourceUrl extends AutoPlugin {
  override def requires: Plugins = Travis && GitPlugin

  override def trigger: PluginTrigger = allRequirements

  override def projectSettings = Seq(
    scalacOptions in Compile in doc := {
      val originalScalacOptions = (scalacOptions in Compile in doc).value
      GitPlugin.gitWorkTree.value match {
        case Some(rootDirectory) =>
          originalScalacOptions.indexOf("-sourcepath") match {
            case -1 =>
              originalScalacOptions ++ Seq(
                "-sourcepath",
                rootDirectory.toString
              )
            case i =>
              originalScalacOptions.updated(i + 1, rootDirectory.toString)
          }
        case None =>
          originalScalacOptions
      }
    },
    scalacOptions in Compile in doc := {
      val originalScalacOptions = (scalacOptions in Compile in doc).value
      (
        GitPlugin.gitRepositoryBuilder.?.value,
        Travis.travisRepoSlug.?.value
      ) match {
        case (Some(repositoryBuilder), Some(slug)) =>
          val repository = repositoryBuilder.build()
          try {
            val hash = repository.resolve(Constants.HEAD).name
            val sourceUrl =
              raw"https://github.com/$slug/blob/${hash}€{FILE_PATH}.scala"
            originalScalacOptions.indexOf("-doc-source-url") match {
              case -1 =>
                originalScalacOptions ++ Seq("-doc-source-url", sourceUrl)
              case i =>
                originalScalacOptions.updated(i + 1, sourceUrl)
            }
          } finally {
            repository.close()
          }
        case _ =>
          originalScalacOptions
      }
    }
  )

}
