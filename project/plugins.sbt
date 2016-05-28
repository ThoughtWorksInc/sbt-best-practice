import org.eclipse.jgit.api.Git

//addSbtPlugin("com.thoughtworks.sbt-best-practice" % "sbt-best-practice" % "0.1.4")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "0.5.0")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.3")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")

libraryDependencies += "com.lihaoyi" %% "sourcecode" % "0.1.1"

libraryDependencies += "com.jsuereth" %% "scala-arm" % "1.4"

libraryDependencies += "org.eclipse.jgit" % "org.eclipse.jgit" % "4.3.0.201604071810-r"

unmanagedSourceDirectories in Compile += baseDirectory.value.getParentFile / "git" / "src" / "main" / "scala"

unmanagedSourceDirectories in Compile += baseDirectory.value.getParentFile / "issue2514" / "src" / "main" / "scala"

unmanagedSourceDirectories in Compile += baseDirectory.value.getParentFile / "disable-deploy" / "src" / "main" / "scala"

unmanagedSourceDirectories in Compile += baseDirectory.value.getParentFile / "detect-license" / "src" / "main" / "scala"

unmanagedSourceDirectories in Compile += baseDirectory.value.getParentFile / "sonatype" / "src" / "main" / "scala"

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
