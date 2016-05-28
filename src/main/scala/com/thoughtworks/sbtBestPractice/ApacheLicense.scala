package com.thoughtworks.sbtBestPractice

import sbt._
import sbt.Keys._

/**
  * Determine if this project is licensed under Apache License 2.0
  */
object ApacheLicense extends AutoPlugin {

  private val ApacheLicenseRegex = """(?s)\s*Apache License\s*Version 2\.0, January 2004\s*.*""".r

  override def trigger = allRequirements

  override def requires = LicenseFile

  override def projectSettings = Seq(
    licenses ++= {
      LicenseFile.licenseFileContent.value match {
        case Some(ApacheLicenseRegex()) => Seq("Apache" -> url("http://www.apache.org/licenses/"))
        case _ => Seq.empty
      }
    }
  )
}