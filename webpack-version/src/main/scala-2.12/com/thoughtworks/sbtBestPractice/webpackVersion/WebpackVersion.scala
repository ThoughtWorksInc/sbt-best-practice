package com.thoughtworks.sbtBestPractice.webpackVersion

import org.scalajs.sbtplugin.ScalaJSPlugin
import sbt._
import sbt.Keys._
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._

import scala.language.reflectiveCalls

/** An sbt plugin to upgrade the webpack version to a newer version that is
  * compatible with Node.js 18 or later.
  *
  * @see
  *   https://github.com/scalacenter/scalajs-bundler/pull/456
  */
object WebpackVersion extends AutoPlugin {

  /** Returns `allRequirements` if ScalaJSPlugin is available, `noTrigger` *
    * otherwise.
    */
  override def trigger = {
    if (requires == empty) {
      noTrigger
    } else {
      allRequirements
    }
  }

  override val requires = {
    try {
      ScalaJSBundlerPlugin
    } catch {
      case _: NoClassDefFoundError =>
        empty
    }
  }

  private def scalaJSVersion06 = {
    // The type of ScalaJSPlugin v0.6
    type ScalaJSPlugin06 = {
      def autoImport: {
        def scalaJSVersion: String
      }
    }
    ScalaJSPlugin.asInstanceOf[ScalaJSPlugin06].autoImport.scalaJSVersion
  }

  override def projectSettings: Seq[Def.Setting[_]] = {
    val scalaJSVersion =
      try {
        // sbt-scalajs 1.x
        ScalaJSPlugin.autoImport.scalaJSVersion
      } catch {
        case _: NoClassDefFoundError =>
          // sbt-scalajs 0.6.x
          scalaJSVersion06
      }

    Seq(
      webpack / version := {
        if (
          VersionNumber(scalaJSVersion).matchesSemVer(SemanticSelector("<1"))
        ) {
          "3.12.0"
        } else {
          "5.98.0"
        }
      }
    )
  }
}
