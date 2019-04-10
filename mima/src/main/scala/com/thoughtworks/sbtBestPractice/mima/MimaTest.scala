package com.thoughtworks.sbtBestPractice.mima

import com.typesafe.tools.mima.plugin.MimaPlugin
import sbt._
import sbt.Keys._

/**
  * @author 杨博 (Yang Bo)
  */
object MimaTest extends AutoPlugin {

  override def trigger = allRequirements

  override def requires = MimaPlugin

  import MimaPlugin.autoImport._

  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    Test / test := {
      mimaReportBinaryIssues.value
      (Test / test).value
    },
  )

}
