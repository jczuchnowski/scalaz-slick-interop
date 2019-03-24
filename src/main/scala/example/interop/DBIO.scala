package example.interop

import scalaz.zio.{ IO, UIO, ZIO }
import scalaz.zio.interop.reactiveStreams._
import scalaz.zio.stream.ZStream
import slick.basic.DatabasePublisher
import slick.dbio.{ DBIO, StreamingDBIO }
import slick.jdbc.JdbcBackend.Database

trait DatabaseProvider {
  def databaseProvider: DatabaseProvider.Service
}

object DatabaseProvider {
  trait Service {
    def db: UIO[Database]
  }
}

object dbio {

  implicit class IOObjOps(private val obj: IO.type) extends AnyVal {
    def fromDBIO[R](dbio: DBIO[R]): ZIO[DatabaseProvider, Throwable, R] =
      for {
        db <- ZIO.accessM[DatabaseProvider](_.databaseProvider.db)
        r  <- ZIO.fromFuture(ec => db.run(dbio))
      } yield r

    def fromStreamingDBIO[T](dbio: StreamingDBIO[_, T]): ZIO[DatabaseProvider, Throwable, ZStream[Any, Throwable, T]] =
      for {
        db <- ZIO.accessM[DatabaseProvider](_.databaseProvider.db)
        r  <- ZIO.effect(db.stream(dbio).toStream())
      } yield r
  }

}
