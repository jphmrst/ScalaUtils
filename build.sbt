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
      "-doc-external-doc:" ++ (
        "/home/jm/.sbt/boot/scala-3.0.0/lib/scala-library.jar#"
          ++ "https://www.scala-lang.org/api/current/,"

        ++ "/home/jm/Lib/Scala/Utils/target/scala-3.0.0/classes/"
          ++ "file:///home/jm/Lib/Scala/Utils/target/scala-3.0.0/api/"

        ++ "/home/jm/Lib/Scala/scalatest-app_2.11-3.0.1.jar#"
          ++ "http://doc.scalatest.org/3.0.0/,")
    )
  )
