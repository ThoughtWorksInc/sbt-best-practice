package com.thoughtworks.sbtBestPractice.git

/** sbt 2.x specific bindings: the pieces of the build API whose types differ
  * from sbt 1.x. Everything else is shared code.
  */
private[sbtBestPractice] object Compatibility:

  /** The type of `homepage`, `ScmInfo.browseUrl` and `Developer.url` (a
    * `java.net.URI` on sbt 2.x).
    */
  type URL = java.net.URI

  def mailtoUrl(emailAddress: String): URL =
    new java.net.URI("mailto", emailAddress, null)

  def httpsUrl(host: String, path: String): URL =
    new java.net.URI("https", host, path, null)
