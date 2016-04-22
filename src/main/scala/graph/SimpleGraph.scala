package flow

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

import scala.concurrent.Future

/**
 * Created by sinisalouc on 22/04/16.
 */
object SimpleGraph {

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem("QuickStart")
    implicit val materializer = ActorMaterializer()

    val source: Source[Int, NotUsed] = Source(1 to 3)

    val sink: Sink[Int, Future[_]] = Sink.foreach[Int](println)

    val invert: Flow[Int, Int, NotUsed]  = Flow[Int].map(elem => elem * -1)

    val doubler: Flow[Int, Int, NotUsed] = Flow[Int].map(elem => elem * 2)

    val runnable: RunnableGraph[NotUsed] = source via invert via doubler to sink

    runnable.run()

  }

}
