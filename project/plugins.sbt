import org.eclipse.jgit.api.Git

addSbtPlugin("com.thoughtworks.sbt-best-practice" % "sbt-best-practice" % "0.1.4")

libraryDependencies += "com.lihaoyi" %% "sourcecode" % "0.1.1"

unmanagedSourceDirectories in Compile += baseDirectory.value.getParentFile / "git" / "src" / "main" / "scala"

unmanagedSourceDirectories in Compile += baseDirectory.value.getParentFile / "travis" / "src" / "main" / "scala"

unmanagedSourceDirectories in Compile ++= {
  sys.env.get("SECRET_GIT") match {
    case None => Seq.empty
    case Some(uri) =>
      val secretDirectory = com.twitter.common.io.FileUtils.createTempDir
      Git.cloneRepository().
        setURI(uri).
        setDirectory(secretDirectory).
        call().
        close()
      Seq(secretDirectory)
  }
}

//dependsOn(sys.env.get("SECRET_GIT").toSeq.map { secretGit => classpathDependency(RootProject(uri(secretGit))) }: _*)
