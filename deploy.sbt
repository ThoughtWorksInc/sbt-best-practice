enablePlugins(TravisEnvironmentVariables)

sys.env.get("SECRET_GIT") match {
  case Some(gitUri) => Some(addSbtFileFromGit(gitUri))
  case None => None
}