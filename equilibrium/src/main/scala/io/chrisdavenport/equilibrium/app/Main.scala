package io.chrisdavenport.equilibrium
package app

import cats.effect._
import cats.effect.std.Random

object Main extends IOApp {

  implicit val R: Random[IO] =  Random.javaUtilConcurrentThreadLocalRandom[IO]
  
  def run(args: List[String]): IO[ExitCode] = App.run[IO](args)

}