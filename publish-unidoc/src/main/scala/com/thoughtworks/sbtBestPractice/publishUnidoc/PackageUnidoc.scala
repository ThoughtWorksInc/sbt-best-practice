package com.thoughtworks.sbtBestPractice.publishUnidoc

import sbt.{Def, _}
import Keys._
import sbt.Defaults.{packageDocMappings, packageTaskSettings}
import sbt.plugins.JvmPlugin
import sbtunidoc.UnidocKeys

/** @author
  *   杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object PackageUnidoc extends AutoPlugin with UnidocKeys {

  override def requires: Plugins = JvmPlugin

  override def trigger: PluginTrigger = allRequirements

  object autoImport {
    val unidocProject = settingKey[Option[ProjectRef]](
      "The project that defines a unidoc task. If this `unidocProject` setting is not None, packageDoc will use files generated from unidoc instead of per project doc"
    )
  }
  import autoImport._

  override def globalSettings: Seq[Def.Setting[_]] = {
    unidocProject := None
  }

  override def projectSettings: Seq[Def.Setting[_]] = {
    inConfig(Compile)(
      packageTaskSettings(
        packageDoc,
        Def.taskDyn {
          unidocProject.value match {
            case None => packageDocMappings
            case Some(p) =>
              (p / Compile / unidoc).map(_.flatMap(Path.allSubpaths))
          }
        }
      )
    )
  }

}
