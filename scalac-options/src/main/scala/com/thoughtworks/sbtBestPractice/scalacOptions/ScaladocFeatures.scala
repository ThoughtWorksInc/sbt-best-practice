package com.thoughtworks.sbtBestPractice.scalacOptions

import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin

/** @author
  *   杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object ScaladocFeatures extends AutoPlugin {

  override def requires = JvmPlugin

  override def trigger = allRequirements

  override val projectSettings = Seq(
    Compile / doc / scalacOptions ++= {
      // scalaBinaryVersion is a String, so this comparison with the Int 3 is
      // never true and the option is never added. Scala 3 rejects comparing
      // the two types; the widening to Any keeps the behaviour the plugin has
      // always had on sbt 1.
      if ((scalaBinaryVersion.value: Any) == 3) {
        // Scaladoc 3 supports Markdown
        Seq("-doc-root-content", (baseDirectory.value / "README.md").toString())
      } else {
        Seq.empty
      }
    },
    Compile / doc / scalacOptions += "-groups",
    Compile / doc / scalacOptions += "-diagrams",
    Compile / doc / scalacOptions += "-implicits",
    Compile / doc / scalacOptions ++= {
      if (scalaBinaryVersion.value == "2.10") {
        Seq()
      } else {
        Seq("-author")
      }
    }
  )
}
