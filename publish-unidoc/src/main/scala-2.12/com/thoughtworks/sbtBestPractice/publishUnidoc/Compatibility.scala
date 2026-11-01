package com.thoughtworks.sbtBestPractice.publishUnidoc

import sbt._

/** sbt 1.x specific bindings: the pieces of the build API whose types differ
  * from sbt 2.x. Everything else is shared code.
  */
private[publishUnidoc] object Compatibility {

  /** The `packageDoc` mappings of the files under the unidoc output
    * directories.
    */
  def unidocMappings(
      unidocDirectories: Def.Initialize[Task[Seq[File]]]
  ): Def.Initialize[Task[Seq[(File, String)]]] =
    unidocDirectories.map(_.flatMap(Path.allSubpaths))
}
