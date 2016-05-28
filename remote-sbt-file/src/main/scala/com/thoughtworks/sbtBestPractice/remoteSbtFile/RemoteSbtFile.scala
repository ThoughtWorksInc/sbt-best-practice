package com.thoughtworks.sbtBestPractice.remoteSbtFile

import com.thoughtworks.sbtBestPractice.issue2514.DslEntries
import com.twitter.common.io.FileUtils
import org.eclipse.jgit.api.Git
import sbt._
import java.io.File

/**
  * @author 杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object RemoteSbtFile extends AutoPlugin {

  object autoImport {

    def addAllSbtFilesFromGit(gitUri: String) = {
      val secretDirectory = FileUtils.createTempDir
      Git.cloneRepository().
        setURI(gitUri).
        setDirectory(secretDirectory).
        call().
        close()
      DslEntries.autoImport.addSbtFile((secretDirectory ** "*.sbt").get: _*)
    }

    def addSbtFileFromGit(gitUri: String, relativeSbtFiles: File*) = {
      val secretDirectory = FileUtils.createTempDir
      Git.cloneRepository().
        setURI(gitUri).
        setDirectory(secretDirectory).
        call().
        close()

      val resolvedFiles = for {
        relativeSbtFile <- relativeSbtFiles
      } yield (secretDirectory / relativeSbtFile.toString)

      DslEntries.autoImport.addSbtFile(resolvedFiles: _*)
    }

  }

}
