package com.thoughtworks.sbtBestPractice.scalacOptions

import sbt._
import Keys._
import sbt.plugins.JvmPlugin

/**
  * @author 杨博 (Yang Bo)
  */
object ClasspathCache extends AutoPlugin {
  override def trigger = allRequirements

  override def requires = JvmPlugin

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    scalacOptions ++= {
      import scala.Ordering.Implicits._
      if (VersionNumber(scalaVersion.value).numbers >= Seq(2L, 12L, 5L)) {
        Seq("-Ycache-plugin-class-loader:last-modified", "-Ycache-macro-class-loader:last-modified")
      } else {
        Nil
      }
    }
  )
}
