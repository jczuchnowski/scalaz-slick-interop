import sbt._

object Dependencies {
  val scalazVersion = "0.16"//"0.11+47-4b3e2c23-SNAPSHOT"
  lazy val scalazZio   = "org.scalaz"         %% "scalaz-zio" % scalazVersion
  lazy val scalazZioRS = "org.scalaz"         %% "scalaz-zio-interop-reactivestreams" % scalazVersion
  lazy val slick       = "com.typesafe.slick" %% "slick"      % "3.3.0"
  lazy val scalaTest   = "org.scalatest"      %% "scalatest"  % "3.0.5" 
  lazy val h2          = "com.h2database"     %  "h2"         % "1.4.199"
}
