libraryDependencies += "com.jsuereth" %% "scala-arm" % "2.0"

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.26")

libraryDependencies += "org.yaml" % "snakeyaml" % "1.24"

libraryDependencies += "com.thoughtworks.dsl" %% "keywords-continue" % "1.0.0-RC22"

libraryDependencies += "com.thoughtworks.dsl" %% "keywords-each" % "1.0.0-RC22"

libraryDependencies += "com.thoughtworks.dsl" %% "keywords-return" % "1.0.0-RC22"

addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-bangnotation" % "1.0.0-RC22")

addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-reseteverywhere" % "1.0.0-RC22")
