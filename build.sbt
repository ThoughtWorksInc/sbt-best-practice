sbtPlugin in ThisBuild := true

organization in ThisBuild := "com.thoughtworks.sbt-best-practice"

lazy val `detect-license` = project dependsOn git

lazy val travis = project dependsOn git

lazy val sonatype = project

lazy val issue2514 = project

lazy val git = project

lazy val `disable-deploy` = project dependsOn git

dependsOn(`detect-license`, travis, sonatype, issue2514, git, `disable-deploy`)