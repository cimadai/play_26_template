name := "play_26_clean_architecture_example"

version := "0.1.0.0-SNAPSHOT"


val PROJECT_SCALA_VERSION = "2.11.11"
val SLICK_VERSION = "3.0.2"
val webpackCommand = "webpack"

import play.sbt.PlayImport.PlayKeys.playRunHooks
def runWebpack(file: File): Int =
  Process(webpackCommand, file, "BUILD_ENV" -> "production").run().exitValue()
lazy val webpack = taskKey[Unit]("Run webpack when packaging the application")

val commonLibraries = Seq(
  "com.google.inject" % "guice" % "4.1.0"
)

val webLibraries = Seq(
  "com.typesafe.play" %% "play-json" % "2.6.7",
  "com.h2database" % "h2" % "1.4.196",
  "org.postgresql" % "postgresql" % "42.1.4.jre7",
  "org.julienrf" %% "play-jsmessages" % "3.0.0",
  "com.typesafe.play" %% "play-slick" % SLICK_VERSION,
  "com.typesafe.play" %% "play-slick-evolutions" % SLICK_VERSION,
  "com.github.karelcemus" %% "play-redis" % "1.6.1",
  "io.swagger" %% "swagger-play2" % "1.6.0",
  "com.sksamuel.scrimage" %% "scrimage-core" % "2.1.8",
  "com.typesafe.play" %% "play-iteratees" % "2.6.1",
  "com.notnoop.apns" % "apns" % "1.0.0.Beta6",
  guice,
  cacheApi,
  ws,
  filters,
  evolutions,
  "org.scalactic" %% "scalactic" % "3.0.4",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test"
)

val PROJECT_SCALA_OPTIONS = Seq(
      "-target:jvm-1.8",
      "-encoding", "UTF-8",
      "-unchecked",
      "-deprecation",
      "-Xfuture",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-Ywarn-unused"
    )

lazy val defaultSettings = Def.SettingsDefinition
  .wrapSettingsDefinition(Seq(
    updateOptions := updateOptions.value.withCachedResolution(true),
    organization := "net.cimadai",
    resolvers += Resolver.sonatypeRepo("snapshots"),
    scalacOptions ++= PROJECT_SCALA_OPTIONS,
    scalaVersion := PROJECT_SCALA_VERSION,
    sources in(Compile, doc) := Seq.empty,
    publishArtifact in(Compile, packageDoc) := false
  ))

lazy val root = (project in file("."))
  .aggregate(web, usecase, domain)

lazy val web = project
  .dependsOn(domain, usecase)
  // 共通の設定
  .settings(defaultSettings)
  // プロジェクト固有の設定
  .enablePlugins(PlayScala)
  .settings(
    libraryDependencies ++= webLibraries,
    dist := (dist dependsOn webpack).value,
    stage := (stage dependsOn webpack).value,
    playRunHooks += RunSubProcess(s"$webpackCommand --progress --colors --watch"),
    webpack := {
      if(runWebpack(baseDirectory.value) != 0) {
        throw new Exception("Something goes wrong when running webpack.")
      }
    }
  )
  // SbtWebの設定
  .enablePlugins(SbtWeb)
  .settings(
    sourceDirectory in Assets := (sourceDirectory in Compile).value / "framework" / "assets",
    pipelineStages := Seq(filter),
    // TODO: ↓効いてないのであとで見る
    includeFilter in filter := "*.ts" || "*.sass" || "*.scss" || "*.json"
  )
  // BuildInfoPluginの設定
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "helpers"
  )

lazy val domain = (project in file("./domain"))
  // 共通の設定
  .settings(defaultSettings)
  // 個別の設定
  .settings(
    libraryDependencies ++= commonLibraries
  )

lazy val usecase = (project in file("./usecase"))
  .dependsOn(domain)
  // 共通の設定
  .settings(defaultSettings)
  // 個別の設定
  .settings(
    libraryDependencies ++= commonLibraries
  )
