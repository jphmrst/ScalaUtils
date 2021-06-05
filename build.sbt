
val scala3Version = "3.0.1-RC1"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.9"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % "test"
Global / excludeLintKeys ++= Set(scalacOptions)
Compile / doc / scalacOptions ++= Seq(
  "-doc-root-content", "src/main/rootdoc.txt",
  "-external-mappings:" ++ (
    ".*scala.*::scaladoc3::" ++ "http://dotty.epfl.ch/api/,"
      ++ "org\\.scalatest.*::scaladoc3::#" ++ "http://doc.scalatest.org/3.0.0/"
  )
)

lazy val utils = project
  .in(file("."))
  .settings(
    name := "Maraist Utils",
    version := "1.0.0",
    scalaVersion := scala3Version,
    compile / watchTriggers += baseDirectory.value.toGlob / "build.sbt",
    unmanagedSources / excludeFilter := ".#*",
    scalacOptions ++= Seq( "-source:future-migration" ),
  )
