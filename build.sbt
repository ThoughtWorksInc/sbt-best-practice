sbtPlugin in ThisBuild := true

organization in ThisBuild := "com.thoughtworks.sbt-best-practice"

lazy val `detect-license` = project dependsOn git

lazy val travis = project dependsOn git

lazy val git = project

lazy val `scalac-options` = project

lazy val `publish-unidoc` = project

lazy val `detect-scala-organization` = project

lazy val `subdirectory-organization` = project

lazy val mima = project

lazy val `skip-duplicate-java-publish` = project

dependsOn(
  `scalac-options`,
  `detect-license`,
  travis,
  git,
  `publish-unidoc`,
  `detect-scala-organization`,
  `subdirectory-organization`,
  `skip-duplicate-java-publish`
)

libraryDependencies += "com.lihaoyi" %% "sourcecode" % "0.2.1"

addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % "3.0.0")

enablePlugins(ScalaUnidocPlugin)

sys.env.get("GITHUB_REPOSITORY") match {
  case None =>
    Seq.empty
  case Some(slug) =>
    Seq(
      ThisBuild / homepage := Some(new URL("https", "github.com", raw"""/$slug""")),
      ThisBuild / scmInfo :=
        Some(
          ScmInfo(
            new URL("https", "github.com", raw"""/$slug"""),
            raw"""https://github.com/$slug.git""",
            Some(raw"""git@github.com:$slug.git""")
          )
        )
    )
}
