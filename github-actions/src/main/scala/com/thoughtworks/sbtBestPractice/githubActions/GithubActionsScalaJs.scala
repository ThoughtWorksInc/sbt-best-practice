package com.thoughtworks.sbtBestPractice.githubActions

import sbt._
import org.scalajs.sbtplugin.ScalaJSPlugin
import scala.language.reflectiveCalls

/** @author
  *   杨博 (Yang Bo)
  */
object GithubActionsScalaJs extends AutoPlugin {
  private def reflectiveLinkerSetting[
      StandardConfig <: {
        def withBatchMode(batchMode: Boolean): StandardConfig
      }
  ](key: SettingKey[StandardConfig]): Def.Setting[StandardConfig] = {
    key := {
      key.value.withBatchMode(
        GithubActionsEnvironmentVariables.githubRepository.?.value.isDefined
      )
    }
  }

  private def scalaJSLinkerConfig06[
      StandardConfig <: {
        def withBatchMode(batchMode: Boolean): StandardConfig
      }
  ] = {
    // The type of ScalaJSPlugin v0.6
    type ScalaJSPlugin06 = {
      def autoImport: {
        def scalaJSLinkerConfig: SettingKey[StandardConfig]
      }
    }
    ScalaJSPlugin.asInstanceOf[ScalaJSPlugin06].autoImport.scalaJSLinkerConfig
  }

  override def projectSettings: Seq[Def.Setting[_]] = {
    Seq(try {
      // sbt-scalajs 1.x
      reflectiveLinkerSetting(ScalaJSPlugin.autoImport.scalaJSLinkerConfig)
    } catch {
      case _: NoClassDefFoundError =>
        // sbt-scalajs 0.6.x
        reflectiveLinkerSetting(scalaJSLinkerConfig06)
    })
  }

  override def requires = {
    try {
      ScalaJSPlugin && GithubActionsEnvironmentVariables
    } catch {
      case _: NoClassDefFoundError =>
        GithubActionsEnvironmentVariables
    }
  }

  override def trigger = {
    if (requires == GithubActionsEnvironmentVariables) {
      noTrigger
    } else {
      allRequirements
    }
  }
}
