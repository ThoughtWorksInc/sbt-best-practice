val sonatypeStaging = "Sonatype OSS Staging" at "https://oss.sonatype.org/content/groups/staging/"
resolvers in ThisBuild ~= (Resolver.mavenLocal +: sonatypeStaging +: _)
