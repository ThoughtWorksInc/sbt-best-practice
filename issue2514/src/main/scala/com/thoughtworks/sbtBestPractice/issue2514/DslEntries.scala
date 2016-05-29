package com.thoughtworks.sbtBestPractice.issue2514

import sbt._
import sbt.internals._

/**
  * See [[https://github.com/sbt/sbt/issues/2514]]
  *
  * @author 杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object DslEntries extends AutoPlugin {

  // Ah ha, you forgot to mark DslEnablePlugins final. Let me hack it!
  private def configure(transform: Project => Project): ProjectManipulation = new DslEnablePlugins(Nil) {
    override val toFunction = transform
  }

  object autoImport {

    import scala.language.implicitConversions

    implicit def optionalDslEntry(entryOption: Option[DslEntry]): DslEntry = {
      entryOption match {
        case Some(entry) => entry
        case None => configure(Predef.identity)
      }
    }

    def aggregate(refs: ProjectReference*) = configure(_.aggregate(refs: _*))

    def dependsOn(deps: ClasspathDep[ProjectReference]*) = configure(_.dependsOn(deps: _*))

    def addSbtFiles(files: File*) = configure(_.addSbtFiles(files: _*))

  }

}
