package com.thoughtworks.sbtBestPractice.git

/** sbt 1.x specific bindings: the pieces of the build API whose types differ
  * from sbt 2.x. Everything else is shared code.
  */
private[sbtBestPractice] object Compatibility {

  /** The type of `homepage`, `ScmInfo.browseUrl` and `Developer.url`. */
  type URL = java.net.URL

  def mailtoUrl(emailAddress: String): URL =
    new java.net.URL("mailto", null, emailAddress)

  def httpsUrl(host: String, path: String): URL =
    new java.net.URL("https", host, path)
}
