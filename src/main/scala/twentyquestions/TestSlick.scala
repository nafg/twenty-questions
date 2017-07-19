package twentyquestions

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import slick.jdbc.H2Profile.api._


object TestSlick extends App {
  val db = Database.forConfig("h2mem1")
  try {
    val q = sql"select 1 + 1".as[Int].head
    val eventualInt = db.run(q)
    val result = Await.result(eventualInt, Duration.Inf)
    println(result)
  } finally db.close
}
