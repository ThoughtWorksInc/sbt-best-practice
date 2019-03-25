libraryDependencies += "com.jsuereth" %% "scala-arm" % "2.0"

addSbtPlugin("com.eed3si9n" % "sbt-unidoc" % "0.4.2")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.26")

libraryDependencies += "org.yaml" % "snakeyaml" % "1.24"

libraryDependencies += "com.thoughtworks.dsl" %% "keywords-continue" % "1.0.0-RC22-43-199595f6"

libraryDependencies += "com.thoughtworks.dsl" %% "keywords-each" % "1.0.0-RC22-43-199595f6"

libraryDependencies += "com.thoughtworks.dsl" %% "keywords-return" % "1.0.0-RC22-43-199595f6"

addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-bangnotation" % "1.0.0-RC22-43-199595f6")

addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-reseteverywhere" % "1.0.0-RC22-43-199595f6")
