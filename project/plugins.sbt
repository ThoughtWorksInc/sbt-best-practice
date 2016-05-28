addSbtPlugin("com.thoughtworks.sbt-best-practice" % "sbt-best-practice" % "0.1.5")

libraryDependencies += "com.lihaoyi" %% "sourcecode" % "0.1.1"

libraryDependencies += "org.eclipse.jgit" % "org.eclipse.jgit" % "4.3.0.201604071810-r"

libraryDependencies += "com.twitter.common" % "io" % "0.0.67"

resolvers += "Twitter Repository" at "http://maven.twttr.com"

unmanagedSourceDirectories in Compile += baseDirectory.value.getParentFile / "remote-sbt-file" / "src" / "main" / "scala"

unmanagedSourceDirectories in Compile += baseDirectory.value.getParentFile / "issue2514" / "src" / "main" / "scala"