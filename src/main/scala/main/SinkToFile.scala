package main

import java.io.File

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, IOResult}
import akka.stream.scaladsl.{FileIO, Flow, Keep, Sink, Source}
import akka.util.ByteString

import scala.concurrent.Future

/**
  * @author sinisalouc
  */
object SinkToFile {

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem("QuickStart")
    implicit val materializer = ActorMaterializer()

    val source: Source[String, NotUsed] = Source(1 to 5).map(_.toString)

    def lineSink(filename: String): Sink[String, Future[IOResult]] =
      Flow[String]
        .map(s => ByteString(s + "\n"))
        .toMat(FileIO.toFile(new File(filename)))(Keep.right)

    source.runWith(lineSink("someFile"))


  }

}
