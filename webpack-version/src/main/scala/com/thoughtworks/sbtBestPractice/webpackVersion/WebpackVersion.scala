package com.thoughtworks.sbtBestPractice.webpackVersion

import org.scalajs.sbtplugin.ScalaJSPlugin
import sbt._
import sbt.Keys._
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._

import scala.language.reflectiveCalls

object WebpackVersion extends AutoPlugin {

  override def trigger = allRequirements

  override def requires = {
    try {
      super.requires && ScalaJSPlugin
    } catch {
      case _: NoClassDefFoundError =>
        super.requires
    }
  }

  override def projectSettings: Seq[Def.Setting[_]] = {
    Seq(
      webpack / version := {
        VersionNumber((webpack / version).value) match {
          case VersionNumber(Seq(3L, _*), _, _) => "3.12.0"
          case VersionNumber(Seq(5L, _*), _, _) => "5.98.0"
          case _                                => (webpack / version).value
        }
      }
    )
  }
}
