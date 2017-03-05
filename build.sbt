name := "food"
organization := "com.chrisbenincasa"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.8"

//fork in run := true

javaOptions ++= Seq(
  "-Dlog.service.output=/dev/stdout",
  "-Dlog.access.output=/dev/stdout"
)

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  "Twitter Maven" at "https://maven.twttr.com"
)

lazy val versions = new {
  val finatra = "2.8.0"
  val logback = "1.2.1"
  val mockito = "2.7.11"
  val scalatest = "3.0.1"

  val slick = "3.2.0"
  val h2 = "1.4.193"
  val flyway = "4.1.1"

  val ficus = "1.4.0"
}

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % versions.logback,
  "ch.qos.logback" % "logback-classic" % versions.logback % "test",

  "com.twitter" %% "finatra-http" % versions.finatra,

  "com.twitter" %% "finatra-http" % versions.finatra % "test",
  "com.twitter" %% "inject-server" % versions.finatra % "test",
  "com.twitter" %% "inject-app" % versions.finatra % "test",
  "com.twitter" %% "inject-core" % versions.finatra % "test",
  "com.twitter" %% "inject-modules" % versions.finatra % "test",
  "com.twitter" %% "finatra-http" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-server" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-app" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-core" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-modules" % versions.finatra % "test" classifier "tests",

  "org.mockito" % "mockito-core" % versions.mockito % "test",
  "org.scalatest" %% "scalatest" % versions.scalatest % "test",

  "com.typesafe.slick" %% "slick" % versions.slick,
  "com.h2database" % "h2" % versions.h2,
  "org.flywaydb" % "flyway-core" % versions.flyway,

  "com.iheart" %% "ficus" % versions.ficus,
  "com.github.scopt" %% "scopt" % "3.5.0",

  "org.apache.shiro" % "shiro-core" % "1.3.2",
  "org.apache.shiro" % "shiro-web" % "1.3.2",
  "org.apache.shiro" % "shiro-guice" % "1.3.2"
)

mainClass in (Compile, run) := Some("com.chrisbenincasa.food.ServerMain")
