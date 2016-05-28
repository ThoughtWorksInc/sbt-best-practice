sbtPlugin in ThisBuild := true

organization in ThisBuild := "com.thoughtworks.sbt-best-practice"

unmanagedSourceDirectories in Compile += baseDirectory.value / "project" / "src" / "main" / "scala"

unmanagedSourceDirectories in Compile += baseDirectory.value / "project" / "project" / "src" / "main" / "scala"

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "0.5.0")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.3")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")

libraryDependencies += "org.eclipse.jgit" % "org.eclipse.jgit" % "4.3.0.201604071810-r"

libraryDependencies += "com.jsuereth" %% "scala-arm" % "1.4"
