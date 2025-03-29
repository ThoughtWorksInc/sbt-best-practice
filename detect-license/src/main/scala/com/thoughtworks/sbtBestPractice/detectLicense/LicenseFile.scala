package com.thoughtworks.sbtBestPractice.detectLicense

import com.thoughtworks.sbtBestPractice.git.Git
import sbt._

object LicenseFile extends AutoPlugin {

  val licenseFileContent = SettingKey[Option[String]](
    "license-file-content",
    "The content of LICENSE file"
  )

  override def trigger = allRequirements

  override def requires = Git

  override def projectSettings = Seq(
    licenseFileContent := {
      Git.gitWorkTree.value.flatMap { workTree =>
        val licenseFile = workTree / "LICENSE"
        if (licenseFile.exists()) {
          Some(IO.read(licenseFile))
        } else {
          None
        }
      }
    }
  )
}
