package com.thoughtworks.sbtBestPractice.scalacOptions

import com.thoughtworks.sbtBestPractice.scalacOptions.ScalacWarnings.allRequirements
import sbt.{Def, _}
import sbt.Keys._
import sbt.plugins.JvmPlugin

/** @author
  *   杨博 (Yang Bo)
  */
object Optimization extends AutoPlugin {

  override def requires: Plugins = JvmPlugin

  override def trigger: PluginTrigger = allRequirements

  object autoImport {
    val optimization =
      settingKey[Boolean]("Whether to enable scalac flags for optimization")
  }
  import autoImport._

  override def globalSettings: Seq[Def.Setting[_]] = Seq(
    optimization := false
  )

  override def projectSettings = Seq(
    scalacOptions ++= {
      if (optimization.value) {
        import scala.math.Ordering.Implicits._
        val versionNumbers = VersionNumber(scalaVersion.value).numbers
        if (versionNumbers < Seq(2L, 12L)) {
          Seq("-optimize", "-Yinline-warnings")
        } else if (versionNumbers < Seq(2L, 12L, 3L)) {
          Seq("-opt:l:project")
        } else {
          Seq("-opt:l:inline", "-opt-inline-from:<sources>")
        }
      } else {
        Nil
      }
    }
  )

}
