val scala3Version = "3.0.0"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.9"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % "test"

lazy val utils = project
  .in(file("."))
  .settings(
    name := "Maraist Utils",
    version := "0.1.0",
    scalaVersion := scala3Version,
    compile / watchTriggers += baseDirectory.value.toGlob / "build.sbt",
    unmanagedSources / excludeFilter := ".#*",
    scalacOptions ++= Seq( "-source:future-migration" )

    // libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
  )
