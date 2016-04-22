package flow

import java.io.File

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, IOResult}
import akka.stream.scaladsl.{FileIO, Flow, Keep, Sink, Source}
import akka.util.ByteString

import scala.concurrent.Future

/**
 * Created by sinisalouc on 19/04/16.
 */
object SinkToFile {

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem("QuickStart")
    implicit val materializer = ActorMaterializer()

    val source: Source[String, NotUsed] = Source(1 to 5).map(_.toString)

    def flow: Flow[String, ByteString, NotUsed] =
      Flow[String].map(s => ByteString(s + "\n"))

    def sink(flow: Flow[String, ByteString, NotUsed])(filename: String) =
      flow.toMat(FileIO.toFile(new File(filename)))(Keep.right)

    source.runWith(sink(flow)("someFile"))


  }

}
