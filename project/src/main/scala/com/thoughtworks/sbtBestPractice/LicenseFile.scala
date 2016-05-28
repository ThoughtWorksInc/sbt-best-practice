package com.thoughtworks.sbtBestPractice

import sbt._

object LicenseFile extends AutoPlugin {

  val licenseFileContent = SettingKey[Option[String]]("license-file-content", "The content of LICENSE file")

  override def trigger = allRequirements

  override def requires = GitInformation

  override def projectSettings = Seq(
    licenseFileContent := {
      GitInformation.gitWorkTree.value.map { workTree =>
        val licenseFile = workTree / "LICENSE"
        IO.read(licenseFile)
      }
    }
  )
}