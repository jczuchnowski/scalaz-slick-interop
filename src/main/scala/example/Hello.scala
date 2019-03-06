package example

import example.interop.DatabaseProvider
import scalaz.zio.ZIO
import slick.driver.H2Driver.api._

object Hello extends Greeting with App {
  println(greeting)
}

trait Greeting {
  lazy val greeting: String = "hello"
}

trait Live extends DatabaseProvider {
  override val databaseProvider = new DatabaseProvider.Service {
    override def getDb() = ZIO.effectTotal(Database.forConfig("h2mem1"))
  }
}
