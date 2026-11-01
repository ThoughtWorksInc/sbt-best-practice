package com.thoughtworks.sbtBestPractice.mima

import com.typesafe.tools.mima.plugin.MimaPlugin
import sbt._

/** @author
  *   杨博 (Yang Bo)
  */
object MimaTest extends AutoPlugin {

  override def trigger = allRequirements

  override def requires = MimaPlugin

  override def projectSettings: Seq[Def.Setting[_]] =
    Compatibility.testSettings

}
