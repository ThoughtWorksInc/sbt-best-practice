sbtPlugin in ThisBuild := true

organization in ThisBuild := "com.thoughtworks.sbt-best-practice"

lazy val `remote-sbt-file` = project dependsOn issue2514

lazy val `detect-license` = project dependsOn git

lazy val travis = project dependsOn git

lazy val sonatype = project

lazy val issue2514 = project

lazy val git = project

lazy val `disable-deploy` = project dependsOn git

lazy val `cross-release` = project

lazy val `scalac-options` = project

dependsOn(`scalac-options`, `remote-sbt-file`, `detect-license`, travis, sonatype, issue2514, git, `disable-deploy`, `cross-release`)

libraryDependencies += "com.lihaoyi" %% "sourcecode" % "0.1.1"

addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % "0.2.3")
