package main

import java.io.File
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.IOResult
import akka.stream.ThrottleMode
import akka.stream.scaladsl.FileIO
import akka.stream.scaladsl.Flow
import akka.stream.scaladsl.Keep
import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.Source
import akka.util.ByteString

/**
 * @author sinisalouc
 */
object Main {

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem("QuickStart")
    implicit val materializer = ActorMaterializer()

    /*** SOURCES ***/

    val source: Source[Int, NotUsed] = Source(1 to 5)
    val sourceFact: Source[Int, NotUsed] = source.scan(1)((acc, next) => acc * next)
    val sourceFactString: Source[String, NotUsed] = sourceFact.map(_.toString)
    val sourceFactThrottled: Source[String, NotUsed] = sourceFact
      .zipWith(Source(0 to 100))((num, idx) => s"$idx! = $num")

    /*** SINKS ***/

    def lineSink(filename: String): Sink[String, Future[IOResult]] =
      Flow[String]
        .map(s => ByteString(s + "\n"))
        .toMat(FileIO.toFile(new File(filename)))(Keep.right)

    // TODO: close and finish
    // sourceFactString.runWith(lineSink("someFile"))

    // WARN: throws division by zero if > 1.second
    val throttledSource3: Source[String, NotUsed] =
      sourceFactThrottled.throttle(1, 1.second, 1, ThrottleMode.shaping)

    throttledSource3.runForeach(println)

  }

}
