package com.thoughtworks.sbtBestPractice.remoteSbtFile

import com.thoughtworks.sbtBestPractice.issue2514.DslEntries
import org.eclipse.jgit.api.Git
import sbt._
import java.io.File

import org.eclipse.jgit.transport.CredentialsProvider
import sbt.internals.ProjectManipulation

/**
  * @author 杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object RemoteSbtFile extends AutoPlugin {

  object autoImport {

    implicit class RichProject(project: Project) {
      def addSbtFilesFromGit(gitUri: String, relativeSbtFiles: File*) = {
        val secretDirectory = IO.createTemporaryDirectory
        Git.cloneRepository().
          setURI(gitUri).
          setDirectory(secretDirectory).
          call().
          close()

        val resolvedFiles = for {
          relativeSbtFile <- relativeSbtFiles
        } yield (secretDirectory / relativeSbtFile.toString)
        project.addSbtFiles(resolvedFiles: _*)
      }
    }

    def addAllSbtFilesFromGit(gitUri: String): ProjectManipulation = {
      addAllSbtFilesFromGit(gitUri, CredentialsProvider.getDefault)
    }

    def addAllSbtFilesFromGit(gitUri: String, credential: CredentialsProvider): ProjectManipulation = {
      val secretDirectory = IO.createTemporaryDirectory
      Git.cloneRepository().
        setURI(gitUri).
        setDirectory(secretDirectory).
        setCredentialsProvider(credential).
        call().
        close()
      DslEntries.autoImport.addSbtFiles((secretDirectory ** "*.sbt").get: _*)
    }

    def addSbtFilesFromGit(gitUri: String, relativeSbtFiles: File*): ProjectManipulation = {
      addSbtFilesFromGit(gitUri, CredentialsProvider.getDefault, relativeSbtFiles: _*)
    }

    def addSbtFilesFromGit(gitUri: String, credential: CredentialsProvider, relativeSbtFiles: File*): ProjectManipulation = {
      val secretDirectory = IO.createTemporaryDirectory
      Git.cloneRepository().
        setURI(gitUri).
        setDirectory(secretDirectory).
        call().
        close()

      val resolvedFiles = for {
        relativeSbtFile <- relativeSbtFiles
      } yield (secretDirectory / relativeSbtFile.toString)

      DslEntries.autoImport.addSbtFiles(resolvedFiles: _*)
    }

  }

}
