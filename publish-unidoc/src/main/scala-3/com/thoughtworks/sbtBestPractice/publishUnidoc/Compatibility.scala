package com.thoughtworks.sbtBestPractice.publishUnidoc

import sbt.*
import sbt.Keys.fileConverter
import xsbti.HashedVirtualFileRef

/** sbt 2.x specific bindings: the pieces of the build API whose types differ
  * from sbt 1.x. Everything else is shared code.
  */
private[publishUnidoc] object Compatibility:

  /** The `packageDoc` mappings of the files under the unidoc output
    * directories. sbt 2.x maps virtual file references, which only regular
    * files have, so the directories are left out, as sbt 2.x's own
    * `packageDocMappings` does.
    */
  def unidocMappings(
      unidocDirectories: Def.Initialize[Task[Seq[File]]]
  ): Def.Initialize[Task[Seq[(HashedVirtualFileRef, String)]]] =
    Def.task {
      val converter = fileConverter.value
      unidocDirectories.value
        .flatMap(Path.allSubpaths)
        .filter { case (file, _) => file.isFile() }
        .map { case (file, path) =>
          (converter.toVirtualFile(file.toPath()): HashedVirtualFileRef) -> path
        }
    }
