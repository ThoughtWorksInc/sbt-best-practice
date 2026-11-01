package com.thoughtworks.sbtBestPractice.detectLicense

import sbt._

/** sbt 1.x specific bindings: the pieces of the build API whose types differ
  * from sbt 2.x. Everything else is shared code.
  */
private[detectLicense] object Compatibility {

  /** An element of `licenses`, a `(String, URL)` pair on sbt 1.x. */
  def license(name: String, licenseUrl: String): (String, URL) =
    name -> url(licenseUrl)
}
