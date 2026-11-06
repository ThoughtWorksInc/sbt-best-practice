package com.thoughtworks.sbtBestPractice.detectLicense

import sbt.*

/** sbt 2.x specific bindings: the pieces of the build API whose types differ
  * from sbt 1.x. Everything else is shared code.
  */
private[detectLicense] object Compatibility:

  /** An element of `licenses`, a `License` on sbt 2.x. */
  def license(name: String, licenseUrl: String): License =
    License(name, url(licenseUrl))
