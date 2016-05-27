package com.thoughtworks.sbtBestPractice

import sbt._
import sbt.Keys._

import scala.io.Source

/**
  * Determine if this project is licensed under Apache License 2.0
  */
object `ApacheLicense2.0` extends AutoPlugin {

  private val Line1 = """\s*Apache License\s*""".r
  private val Line2 = """\s*Version 2\.0, January 2004\s*""".r

  override def trigger = allRequirements

  override def projectSettings = Seq(
    licenses ++= {
      val licenseFile = baseDirectory.value / "LICENSE"
      if (licenseFile.exists()) {
        Source.fromFile(licenseFile).getLines().take(2).toSeq match {
          case Seq(Line1(), Line2()) => Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))
          case _ => Seq.empty
        }
      } else {
        Seq.empty
      }
    }
  )
}