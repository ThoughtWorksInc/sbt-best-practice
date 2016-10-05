import sbt._
import Keys._

object AutoCrossPlugin extends AutoPlugin {

  override def trigger = allRequirements

  override def projectSettings = net.virtualvoid.sbt.cross.CrossPlugin.crossBuildingSettings

}