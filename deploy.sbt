enablePlugins(TravisEnvironmentVariables)

sys.env.get("SECRET_GIT") match {
  case Some(gitUri) => Some(addSbtFileFromGit(gitUri, file("secret.sbt")))
  case None => None
}