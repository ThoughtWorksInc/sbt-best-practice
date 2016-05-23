addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.3")

libraryDependencies += "org.eclipse.jgit" % "org.eclipse.jgit" % "4.3.0.201604071810-r"

libraryDependencies += "com.jsuereth" %% "scala-arm" % "1.4"

dependsOn(sys.env.get("SECRET_GIT").toSeq.map { secretGit => classpathDependency(RootProject(uri(secretGit))) }: _*)