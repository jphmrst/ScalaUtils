val scala3Version = "3.0.0"

lazy val utils = project
  .in(file("."))
  .settings(
    name := "Maraist Utils",
    version := "0.1.0",
    scalaVersion := scala3Version

    // libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
  )
