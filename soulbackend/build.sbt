name := "faithservice"

version := "1.0"

lazy val `faithservice` = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(javaJdbc, cache, javaWs, filters,
  "com.amazonaws" % "aws-java-sdk" % "1.11.194",
  "org.mongodb.morphia" % "morphia" % "1.3.2",
  "org.mongodb.morphia" % "morphia-validation" % "1.3.2",
  "commons-io" % "commons-io" % "2.4")

// Not include the API documentation in the generated package
sources in(Compile, doc) := Seq.empty

publishArtifact in(Compile, packageDoc) := false
