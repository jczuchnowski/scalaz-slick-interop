import sbt._

object Dependencies {
  lazy val scalazZio = "org.scalaz"         %% "scalaz-zio" % "1.0-RC1"
  lazy val slick      = "com.typesafe.slick" %% "slick"      % "3.3.0"
  lazy val scalaTest  = "org.scalatest"      %% "scalatest"  % "3.0.5"
}
