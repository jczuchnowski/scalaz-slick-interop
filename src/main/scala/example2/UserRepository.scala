package example2

import java.sql.SQLException
import scalaz.zio.{ IO, Task, ZIO }

trait UserRepository {
    def addUser(email: String): IO[SQLException, Long]
    def getUser(id: Long): IO[SQLException, Option[(Long, String)]]
}