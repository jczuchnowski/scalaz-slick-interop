package example

import slick.driver.H2Driver.api._
import scalaz.zio.{ IO, UIO, ZIO }
import example.interop.dbio._
import example.interop.DatabaseProvider

class Users(tag: Tag) extends Table[(Long, String)](tag, "USERS") {
  def id = column[Long]("ID", O.PrimaryKey)
  def name = column[String]("NAME")
  def * = (id, name)
}

case class User(id: Long, name: String)

trait SlickUserRepository extends UserRepository with DatabaseProvider { self =>

  val userRepository = new UserRepository.Service {
    val users = TableQuery[Users]

    def getUser(id: Long): ZIO[Any, Throwable, Option[(Long, String)]] =
      IO.fromDBIO(users.filter(_.id === id).result.headOption).provide(self)
  }

}

trait UserRepository {
  def userRepository: UserRepository.Service
}

object UserRepository {
  trait Service {
    def getUser(id: Long): IO[Throwable, Option[(Long, String)]]
  }
}

