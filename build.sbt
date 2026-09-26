ThisBuild / tlBaseVersion := "0.0" // your current series x.y

ThisBuild / organization := "io.chrisdavenport"
ThisBuild / organizationName := "Christopher Davenport"
ThisBuild / licenses := Seq(License.MIT)
ThisBuild / developers := List(
  tlGitHubDev("christopherdavenport", "Christopher Davenport")
)
ThisBuild / tlCiReleaseBranches := Seq()


val Scala213 = "2.13.18"

ThisBuild / crossScalaVersions := Seq(Scala213, "3.3.8")
ThisBuild / scalaVersion := Scala213

ThisBuild / testFrameworks += new TestFramework("munit.Framework")

val catsV = "2.13.0"
val catsEffectV = "3.7.1"
val fs2V = "3.14.0"
val http4sV = "0.23.37"
val circeV = "0.14.2"
val doobieV = "1.0.0-RC2"
val munitCatsEffectV = "2.2.1"



// Projects
lazy val `equilibrium` = tlCrossRootProject
  .aggregate(core)

lazy val core = project
  .in(file("core"))
  .settings(
    name := "equilibrium-core",

    libraryDependencies ++= Seq(
      "org.typelevel"               %% "cats-core"                  % catsV,
      "org.typelevel"               %% "cats-effect"                % catsEffectV,

      "co.fs2"                      %% "fs2-core"                   % fs2V,
      "co.fs2"                      %% "fs2-io"                     % fs2V,

      "org.http4s"                  %% "http4s-ember-server"        % http4sV,
      "org.http4s"                  %% "http4s-ember-client"        % http4sV,
      "org.http4s"                  %% "http4s-circe"               % http4sV,

      "io.circe"                    %% "circe-core"                 % circeV,
      "io.circe"                    %% "circe-generic"              % circeV,
      "io.circe"                    %% "circe-parser"               % circeV,
      "io.circe"                    %% "circe-yaml"                 % "0.14.1",

      "org.typelevel"               %% "munit-cats-effect"        % munitCatsEffectV         % Test,

    )
  )

lazy val app = project
  .in(file("equilibrium"))
  .enablePlugins(NoPublishPlugin)
  .settings(
    name := "equilibrium"
  ).dependsOn(core)

lazy val site = project.in(file("site"))
  .enablePlugins(TypelevelSitePlugin)
  .settings(
    laikaTheme := tlSiteHelium.value.site
      .topNavigationBar(
        homeLink = laika.helium.config.IconLink.internal(laika.ast.Path.Root / "index.md", laika.helium.config.HeliumIcon.home)
      )
      .build
  )
  .dependsOn(core)
