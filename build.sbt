
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
    scalacOptions ++= Seq( "-source:future-migration" ),
    doc / scalacOptions ++= Seq(
      "-doc-title", name.value,
      "-doc-root-content", "src/main/rootdoc.txt",
      "-external-mappings:" ++ (
        ".*scala.*::scaladoc3::" ++ "http://dotty.epfl.ch/api/"
          // ++ "/home/jm/Lib/Scala/scalatest-app_2.11-3.0.1.jar#"
          //   ++ "http://doc.scalatest.org/3.0.0/,"
      )
      // "-doc-external-doc:" ++ (
      //   // ...
      //   ++ "/home/jm/Lib/Scala/scalatest-app_2.11-3.0.1.jar#"
      //     ++ "http://doc.scalatest.org/3.0.0/,")
    )
  )
