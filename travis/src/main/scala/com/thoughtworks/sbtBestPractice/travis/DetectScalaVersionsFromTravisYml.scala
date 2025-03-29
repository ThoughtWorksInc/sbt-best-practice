package com.thoughtworks.sbtBestPractice.travis
import java.nio.file.Files

import com.thoughtworks.dsl.keywords._
import com.thoughtworks.sbtBestPractice.git.Git
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.nodes._

import scala.collection.JavaConverters._
import sbt.Keys.scalaVersion
import sbt.Keys.crossScalaVersions
import sbt.plugins.JvmPlugin
import sbt.{Node => _, _}

/** @author
  *   杨博 (Yang Bo)
  */
object DetectScalaVersionsFromTravisYml extends AutoPlugin {
  override def requires = JvmPlugin && Git
  override def trigger = allRequirements

  private def extractScalaVersions(travisYmlRoot: Node): Seq[String] = {
    travisYmlRoot match {
      case root: MappingNode =>
        val pair = !Each(root.getValue.asScala)
        pair.getKeyNode match {
          case keyNode: ScalarNode if keyNode.getValue == "matrix" =>
            pair.getValueNode match {
              case matrixNode: MappingNode =>
                val pair = !Each(matrixNode.getValue.asScala)
                pair.getKeyNode match {
                  case keyNode: ScalarNode if keyNode.getValue == "include" =>
                    pair.getValueNode match {
                      case sequenceNode: SequenceNode =>
                        !Each(sequenceNode.getValue.asScala) match {
                          case elementNode: MappingNode =>
                            val pair = !Each(elementNode.getValue.asScala)
                            pair.getKeyNode match {
                              case keyNode: ScalarNode
                                  if keyNode.getValue == "scala" =>
                                pair.getValueNode match {
                                  case valueNode: ScalarNode =>
                                    !Return(valueNode.getValue)
                                  case _ =>
                                    throw new MessageOnlyException(
                                      "The value of `matrix/include/scala` field in .travis.yml should be a scalar."
                                    )
                                }
                              case _ =>
                                !Continue
                            }
                          case _ =>
                            !Continue
                        }
                      case _ =>
                        !Continue
                    }
                  case _ =>
                    !Continue
                }
              case _ =>
                !Continue
            }
          case keyNode: ScalarNode if keyNode.getValue == "scala" =>
            pair.getValueNode match {
              case valueNode: ScalarNode =>
                !Return(valueNode.getValue)
              case sequenceNode: SequenceNode =>
                !Each(sequenceNode.getValue.asScala) match {
                  case valueNode: ScalarNode =>
                    !Return(valueNode.getValue)
                  case _ =>
                    throw new MessageOnlyException(
                      "The value of `scala` field in .travis.yml should be a scalar or a sequence of scalars."
                    )
                }
              case _ =>
                throw new MessageOnlyException(
                  "The value of `scala` field in .travis.yml  should be a scalar or a sequence of scalars."
                )
            }
          case _ =>
            !Continue
        }
      case _ =>
        !Continue
    }
  }

  private def scalaVersionSetting = {
    scalaVersion := {
      Git.gitWorkTree.value match {
        case None =>
          scalaVersion.value
        case Some(gitWorkTree) =>
          val travisYmlPath = (gitWorkTree / ".travis.yml").toPath
          if (Files.exists(travisYmlPath)) {
            val versions = extractScalaVersions {
              val reader = Files.newBufferedReader(
                travisYmlPath,
                scala.io.Codec.UTF8.charSet
              )
              try {
                new Yaml().compose(reader)
              } finally {
                reader.close()
              }
            }
            versions.headOption.getOrElse(scalaVersion.value)
          } else {
            scalaVersion.value
          }
      }
    }
  }

  private def crossScalaVersionsSetting = (
    crossScalaVersions := {
      Git.gitWorkTree.value match {
        case None =>
          crossScalaVersions.value
        case Some(gitWorkTree) =>
          val travisYmlPath = (gitWorkTree / ".travis.yml").toPath

          if (Files.exists(travisYmlPath)) {
            extractScalaVersions {
              val reader = Files.newBufferedReader(
                travisYmlPath,
                scala.io.Codec.UTF8.charSet
              )
              try {
                new Yaml().compose(reader)
              } finally {
                reader.close()
              }
            }
          } else {
            crossScalaVersions.value
          }
      }
    }
  )

  override def projectSettings = crossScalaVersionsSetting

  override def buildSettings =
    Seq(crossScalaVersionsSetting, scalaVersionSetting)
}
