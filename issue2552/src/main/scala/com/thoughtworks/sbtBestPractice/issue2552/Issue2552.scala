package com.thoughtworks.sbtBestPractice.issue2552

import sbt.Keys._
import sbt.{AutoPlugin, Def, SimpleFileFilter}

/**
  * @see [[https://github.com/sbt/sbt/issues/2552]]
  */
object Issue2552 extends AutoPlugin {
  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    includeFilter in unmanagedSources := (includeFilter in unmanagedSources).value && new SimpleFileFilter(_.isFile)
  )
}
