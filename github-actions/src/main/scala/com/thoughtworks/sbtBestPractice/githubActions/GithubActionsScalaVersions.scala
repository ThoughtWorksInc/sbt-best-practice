package com.thoughtworks.sbtBestPractice.travis
import java.nio.file.Files
import scala.language.implicitConversions
import com.thoughtworks.dsl.Dsl.reset
import com.thoughtworks.dsl.keywords._
import com.thoughtworks.sbtBestPractice.git.Git
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.nodes._

import scala.collection.JavaConverters._
import sbt.Keys.scalaVersion
import sbt.Keys.crossScalaVersions
import sbt.plugins.JvmPlugin
import sbt.{Node => _, _}
import sbt.nio.file.FileTreeView
import com.thoughtworks.Extractor._

/** @author
  *   杨博 (Yang Bo)
  */
object GithubActionsScalaVersions extends AutoPlugin {
  override def requires = JvmPlugin && Git
  override def trigger = allRequirements

  val githubActionsMatrixScalaVersions = settingKey[Seq[String]](
    "The Scala versions extracted from the build matrices of Github Actions"
  )

  private implicit final class YamlPathOps(private val nodes: Seq[Node])
      extends AnyVal {
    private def keyValue: PartialFunction[NodeTuple, (Node, Node)] = {
      case tuple =>
        (tuple.getKeyNode(), tuple.getValueNode())
    }

    def childNodes(key: String): Seq[Node] = {
      nodes.flatMap {
        case mappingNode: MappingNode =>
          mappingNode.getValue().asScala.collect {
            case keyValue.extract(keyNode: ScalarNode, value)
                if keyNode.getValue() == key =>
              value
          }
        case _ =>
          Seq.empty
      }
    }

    def childNodes: Seq[Node] = {
      nodes.flatMap {
        case mappingNode: MappingNode =>
          mappingNode.getValue().asScala.collect {
            case keyValue.extract(_, value) =>
              value
          }
        case sequence: SequenceNode =>
          sequence.getValue().asScala
        case _ =>
          Seq.empty
      }
    }

  }

  private implicit def yamlPathOps(node: Node): YamlPathOps = new YamlPathOps(
    Seq(node)
  )

  override def projectSettings = Seq(
    crossScalaVersions := githubActionsMatrixScalaVersions.value
  )
  override def buildSettings =
    Seq(
      githubActionsMatrixScalaVersions ++= {
        Git.gitWorkTree.value match {
          case None =>
            Seq.empty
          case Some(gitWorkTree) =>
            FileTreeView.default
              .list(gitWorkTree.toGlob / ".github/workflows/*.{yml,yaml}")
              .flatMap { case (workflowYmlPath, _) =>
                val reader = Files.newBufferedReader(
                  workflowYmlPath,
                  scala.io.Codec.UTF8.charSet
                )
                try {
                  val matrix = new Yaml()
                    .compose(reader)
                    .childNodes("jobs")
                    .childNodes
                    .childNodes("strategy")
                    .childNodes("matrix")
                  val scalaNodes = matrix
                    .childNodes("scala")
                    .childNodes
                  val includeScalaNodes = matrix
                    .childNodes("include")
                    .childNodes
                    .childNodes("scala")
                  (scalaNodes ++ includeScalaNodes).collect {
                    case scalarNode: ScalarNode =>
                      scalarNode.getValue()
                  }.distinct
                } finally {
                  reader.close()
                }
              }
        }
      },
      scalaVersion := githubActionsMatrixScalaVersions.value.headOption
        .getOrElse(scalaVersion.value),
      crossScalaVersions := githubActionsMatrixScalaVersions.value
    )

  override def globalSettings = Seq(
    githubActionsMatrixScalaVersions := Seq.empty
  )
}
