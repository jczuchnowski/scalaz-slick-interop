package example.interop

import org.scalatest._

//import cats.effect.{ContextShift, IO}
import org.scalatest.{ WordSpec, Matchers }
import scalaz.zio.{ DefaultRuntime, IO, ZIO }
import scalaz.zio.console._
import scalaz.zio.stream.{ ZSink, ZStream }
import slick.dbio.{ DBIO, StreamingDBIO }
import slick.jdbc.{ H2Profile, JdbcProfile }
import slick.jdbc.JdbcBackend.Database
import scala.concurrent.ExecutionContext

import example.interop.dbio._

class DBIOSpec extends WordSpec with Matchers with DefaultRuntime {
    
  val profile: JdbcProfile = H2Profile
    
  object DBProvider extends DatabaseProvider {
    override val databaseProvider = new DatabaseProvider.Service {
      override val db = ZIO.succeed(Database.forURL("jdbc:h2:mem:"))
    }
  }

  "DBIO interop" should {

    "make it possible to run single action" in {
  
      val action: DBIO[Int] = {
        import profile.api._
        sql"select 1".as[Int].head
      }
  
      val a: ZIO[DatabaseProvider, Throwable, Int] = 
        IO.fromDBIO(action).either.map(_.fold(e => -1, s => s))

      val result: Int = unsafeRun(a.provide(DBProvider))

      result shouldBe 1
    }
    
    "make it possible to run streaming action" in {
  
      val action: StreamingDBIO[Seq[Int], Int] = {
        import profile.api._
        sql"select * from values (1), (2), (3)".as[Int]
      }
        
      val r = for {
        stream <- IO.fromStreamingDBIO(action)
        result <- stream.run(ZSink.collect[Int])
      } yield result

      val a = r.either.map(_.fold(e => List.empty, s => s))

      val result: List[Int] = unsafeRun(a.provide(DBProvider))

      result shouldBe List(1, 2, 3)
    }
  
  }
}