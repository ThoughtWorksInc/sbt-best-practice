package com.thoughtworks.sbtBestPractice.travis

import sbt._

/** Configure sbt from Travis environment variables
  *
  * @note
  *   This plugin should only be enabled when sbt is running on Travis CI
  *
  * @author
  *   杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object Travis extends AutoPlugin {

  val travisBranch = settingKey[String](
    "For builds not triggered by a pull request this is the name of the branch currently being built; whereas for builds triggered by a pull request this is the name of the branch targeted by the pull request (in many cases this will be master)."
  )
  val travisRepoSlug = settingKey[String](
    "The slug (in form: owner_name/repo_name) of the repository currently being built. (for example, “travis-ci/travis-build”)."
  )
  val travisCommit =
    settingKey[String]("The commit that the current build is testing.")

  private val Variables = Seq(
    "TRAVIS_BRANCH" -> travisBranch,
    "TRAVIS_REPO_SLUG" -> travisRepoSlug,
    "TRAVIS_COMMIT" -> travisCommit
  )

  override def globalSettings = {
    Variables.flatMap { case (variableName, key) =>
      sys.env.get(variableName) match {
        case None =>
          Seq.empty
        case Some(value) =>
          Seq(key := value)
      }
    }
  }

  override def trigger = {
    if (sys.env.isDefinedAt("TRAVIS_REPO_SLUG")) {
      allRequirements
    } else {
      noTrigger
    }
  }

}
