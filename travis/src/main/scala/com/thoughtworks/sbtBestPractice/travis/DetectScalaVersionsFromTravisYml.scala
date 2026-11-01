package com.thoughtworks.sbtBestPractice.travis
import java.nio.file.Files

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
        root.getValue.asScala.toSeq.flatMap { pair =>
          pair.getKeyNode match {
            case keyNode: ScalarNode if keyNode.getValue == "matrix" =>
              pair.getValueNode match {
                case matrixNode: MappingNode =>
                  matrixNode.getValue.asScala.toSeq.flatMap { pair =>
                    pair.getKeyNode match {
                      case keyNode: ScalarNode
                          if keyNode.getValue == "include" =>
                        pair.getValueNode match {
                          case sequenceNode: SequenceNode =>
                            sequenceNode.getValue.asScala.toSeq.flatMap {
                              case elementNode: MappingNode =>
                                elementNode.getValue.asScala.toSeq.flatMap {
                                  pair =>
                                    pair.getKeyNode match {
                                      case keyNode: ScalarNode
                                          if keyNode.getValue == "scala" =>
                                        pair.getValueNode match {
                                          case valueNode: ScalarNode =>
                                            Seq(valueNode.getValue)
                                          case _ =>
                                            throw new MessageOnlyException(
                                              "The value of `matrix/include/scala` field in .travis.yml should be a scalar."
                                            )
                                        }
                                      case _ =>
                                        Seq.empty
                                    }
                                }
                              case _ =>
                                Seq.empty
                            }
                          case _ =>
                            Seq.empty
                        }
                      case _ =>
                        Seq.empty
                    }
                  }
                case _ =>
                  Seq.empty
              }
            case keyNode: ScalarNode if keyNode.getValue == "scala" =>
              pair.getValueNode match {
                case valueNode: ScalarNode =>
                  Seq(valueNode.getValue)
                case sequenceNode: SequenceNode =>
                  sequenceNode.getValue.asScala.toSeq.map {
                    case valueNode: ScalarNode =>
                      valueNode.getValue
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
              Seq.empty
          }
        }
      case _ =>
        Seq.empty
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
