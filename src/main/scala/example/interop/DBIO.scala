package example.interop

import scalaz.zio.{ IO, UIO, ZIO }
import slick.driver.H2Driver.api._

import scala.concurrent.ExecutionContext

trait DatabaseProvider {
  def databaseProvider: DatabaseProvider.Service
}

object DatabaseProvider {
  trait Service {
    def getDb(): UIO[Database]
  }
}

object dbio {

  implicit class IOObjOps(private val obj: IO.type) extends AnyVal {
    def fromDBIO[A](dbio: DBIO[A]): ZIO[DatabaseProvider, Throwable, A] =
      for {
        db <- ZIO.accessM[DatabaseProvider, Nothing, Database](_.databaseProvider.getDb())
        r  <- ZIO.fromFuture(ec => db.run(dbio))
      } yield r
  }

}
