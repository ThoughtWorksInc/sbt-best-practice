package com.thoughtworks.sbtBestPractice.detectLicense

import sbt.Keys._
import sbt._

/** @author
  *   杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object MitLicense extends AutoPlugin {

  private val MitLicenseRegex = """^\s*(?:The )?MIT License\s+""".r

  override def trigger = allRequirements

  override def requires = LicenseFile

  override def projectSettings = Seq(
    licenses ++= {
      LicenseFile.licenseFileContent.value match {
        case Some(content)
            if MitLicenseRegex.findFirstMatchIn(content).isDefined =>
          Seq("MIT" -> url("http://opensource.org/licenses/MIT"))
        case _ =>
          Seq.empty
      }
    }
  )
}
