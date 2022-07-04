package io.chrisdavenport.equilibrium

import cats.syntax.all._
import cats.effect._
import cats.effect.std.{Console, Random}
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import fs2.io.file.Files

import org.http4s.circe.middleware.JsonDebugErrorHandler
import com.comcast.ip4s._

object App {
  def run[F[_]: Random: Console: Async](args: List[String]): F[Nothing] = {
    val _ = args
    for {
      client <- EmberClientBuilder.default[F].build
      config <- Resource.eval(
        Files[F].currentWorkingDirectory.flatMap(path => 
          Files[F].readAll(path / "equilibrium.yaml")
            .through(fs2.text.utf8.decode)
            .compile.string
            .flatMap(Config.loadYaml[F](_))
        )
      )
      _ <- Resource.eval(Console[F].println(config))
      servers = Http.servers(config, client)
      _ <- servers.toNel.traverse{ case (port, routes) => 
        val app = JsonDebugErrorHandler(routes, _ => false).orNotFound
        EmberServerBuilder.default[F]
          .withHost(host"0.0.0.0")
          .withPort(port)
          .withHttp2
          .withHttpApp(app)
          .build
      }
      
    } yield ExitCode.Success
  }.useForever
}