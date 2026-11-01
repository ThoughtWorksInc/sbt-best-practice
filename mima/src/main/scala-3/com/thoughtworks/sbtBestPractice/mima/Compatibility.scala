package com.thoughtworks.sbtBestPractice.mima

import com.typesafe.tools.mima.plugin.MimaPlugin.autoImport.*
import sbt.*
import sbt.Keys.*

/** sbt 2.x specific bindings: the pieces of the build API whose types differ
  * from sbt 1.x. Everything else is shared code.
  */
private[mima] object Compatibility:

  /** Runs `mimaReportBinaryIssues` before the tests. On sbt 2.x `test` is an
    * input task that runs the tests incrementally, and `testFull` is the task
    * that runs all of them, as sbt 1.x's `test` did; both report first.
    */
  def testSettings: Seq[Def.Setting[?]] = Seq(
    Test / test := {
      mimaReportBinaryIssues.value
      (Test / test).evaluated
    },
    Test / testFull := {
      mimaReportBinaryIssues.value
      (Test / testFull).value
    }
  )
