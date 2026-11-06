package com.thoughtworks.sbtBestPractice.mima

import com.typesafe.tools.mima.plugin.MimaPlugin.autoImport._
import sbt._
import sbt.Keys._

/** sbt 1.x specific bindings: the pieces of the build API whose types differ
  * from sbt 2.x. Everything else is shared code.
  */
private[mima] object Compatibility {

  /** Runs `mimaReportBinaryIssues` before the tests. On sbt 1.x `test` is a
    * task.
    */
  def testSettings: Seq[Def.Setting[_]] = Seq(
    Test / test := {
      mimaReportBinaryIssues.value
      (Test / test).value
    }
  )
}
