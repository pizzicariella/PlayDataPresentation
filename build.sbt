name := "PlayDataPresentation"
 
version := "1.0" 
      
lazy val `playdatapresentation` = (project in file(".")).enablePlugins(PlayScala)
//val akkaVersion = "2.5.31"

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
      
//scalaVersion := "2.12.2"
scalaVersion := "2.11.12"

/*lazy val myproject = project.settings(
  scalaVersion := "2.11.12", // 2.11.12, or 2.13.4
   addCompilerPlugin(scalafixSemanticdb), // enable SemanticDB
  scalacOptions ++= List(
       "-Yrangepos",          // required by SemanticDB compiler plugin
       "-Ywarn-unused-import" // required by `RemoveUnused` rule
  )
)
scalafixDependencies in ThisBuild ++= Seq(
  "org.reactivemongo" %% "reactivemongo-scalafix" % "1.0.0")*/


libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )
libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.20.13-play26",
  "org.webjars" % "jquery" % "2.1.3",
  "org.apache.spark" %% "spark-core" % "2.4.7",
  "com.johnsnowlabs.nlp" %% "spark-nlp" % "2.6.2",
  "com.johnsnowlabs.nlp" %% "spark-nlp-gpu" % "2.6.2",
  "org.apache.spark" %% "spark-mllib" % "2.4.7"
  //"org.apache.spark" %% "spark-sql" % "2.4.7",
  /*"com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-core" % akkaVersion,
  "com.typesafe.akka" %% "akka-parsing" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion*/
)

//excludeDependencies += "com.typesafe.akka"
unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

routesGenerator := InjectedRoutesGenerator

      