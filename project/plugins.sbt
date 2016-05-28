import org.eclipse.jgit.api.Git

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "0.5.0")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.3")

libraryDependencies += "org.eclipse.jgit" % "org.eclipse.jgit" % "4.3.0.201604071810-r"

libraryDependencies += "com.jsuereth" %% "scala-arm" % "1.4"

libraryDependencies += "com.lihaoyi" %% "sourcecode" % "0.1.1"

unmanagedSourceDirectories in Compile ++= {
  sys.env.get("SECRET_GIT") match {
    case None => Seq.empty
    case Some(uri) =>
      val secretDirectory = com.twitter.common.io.FileUtils.createTempDir
      Git.cloneRepository()
        .setURI(uri)
        .setDirectory(secretDirectory)
        .call()
      Seq(secretDirectory)
  }
}

//dependsOn(sys.env.get("SECRET_GIT").toSeq.map { secretGit => classpathDependency(RootProject(uri(secretGit))) }: _*)