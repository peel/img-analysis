organization  := "com.example"

version       := "0.1"

scalaVersion  := "2.11.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV = "2.3.7"
  val sprayV = "1.3.2"
  val scrimageV = "1.4.2"
  Seq(
    "com.sksamuel.scrimage" %% "scrimage-core" % scrimageV,
    "com.sksamuel.scrimage" %% "scrimage-canvas" % scrimageV,
    "com.sksamuel.scrimage" %% "scrimage-filters" % scrimageV,
    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-routing" % sprayV,
    "io.spray"            %%  "spray-httpx" % sprayV,
    "io.spray"            %%  "spray-json" % "1.3.1",
    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "org.specs2"          %%  "specs2-core"   % "2.3.11" % "test"
  )
}

Revolver.settings
