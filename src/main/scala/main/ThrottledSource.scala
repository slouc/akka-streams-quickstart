package main

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ThrottleMode}
import akka.stream.scaladsl.Source

import scala.concurrent.duration.DurationInt

/**
  * Created by sinisalouc on 19/04/16.
  */
object ThrottledSource {

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem("QuickStart")
    implicit val materializer = ActorMaterializer()

    val source: Source[Int, NotUsed] = Source(2 to 5)
    val sourceFact: Source[Int, NotUsed] = source.scan(1)((acc, next) => acc * next)

    // WARN: throws division by zero if > 1.second
    val throttledSource: Source[Int, NotUsed] =
      sourceFact.throttle(1, 1.second, 1, ThrottleMode.shaping)

    throttledSource.runForeach(println)

  }
}
