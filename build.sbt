name := "PlayDataPresentation"
 
version := "1.0" 
      
lazy val `playdatapresentation` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
resolvers ++= Seq("guava" at "https://mvnrepository.com/artifact/com.google.guava/guava")
      
//scalaVersion := "2.12.2"
scalaVersion := "2.11.12"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )
libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.20.13-play26",
  "org.webjars" % "jquery" % "2.1.3",
  "org.apache.spark" %% "spark-core" % "2.4.7",
  "com.johnsnowlabs.nlp" %% "spark-nlp" % "2.6.2",
  "com.johnsnowlabs.nlp" %% "spark-nlp-gpu" % "2.6.2",
  "org.apache.spark" %% "spark-mllib" % "2.4.7",
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.8.8",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.3" % "test"
)

dependencyOverrides += "com.google.guava" % "guava" % "15.0"

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

routesGenerator := InjectedRoutesGenerator

      