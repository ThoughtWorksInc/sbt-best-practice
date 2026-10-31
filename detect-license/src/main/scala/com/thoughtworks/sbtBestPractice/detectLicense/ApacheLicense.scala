package com.thoughtworks.sbtBestPractice.detectLicense

import sbt.Keys._
import sbt._

/** Determine if this project is licensed under Apache License 2.0
  */
object ApacheLicense extends AutoPlugin {

  private val ApacheLicenseRegex =
    """^\s*Apache License\s*Version 2\.0, January 2004""".r

  override def trigger = allRequirements

  override def requires = LicenseFile

  override def projectSettings = Seq(
    licenses ++= {
      LicenseFile.licenseFileContent.value match {
        case Some(content)
            if ApacheLicenseRegex.findFirstMatchIn(content).isDefined =>
          Seq("Apache" -> url("http://www.apache.org/licenses/"))
        case _ =>
          Seq.empty
      }
    }
  )
}
