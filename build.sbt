
val scala3Version = "3.0.1-RC1"

// library name
name := "misc-utils"

// library version
version := "1.0.1"

/////////////////////////////////////////////////////////////////
// begin maven etc. publishing information

// groupId, SCM, license information
organization := "org.maraist"
organizationName := "Atelier Maraist"
organizationHomepage := Some(url("https://maraist.org/"))
homepage := Some(url("https://github.com/jphmrst/ScalaUtils"))
scmInfo := Some(ScmInfo(
  url("https://github.com/jphmrst/ScalaUtils"),
  "git@github.com:jphmrst/ScalaUtils.git"))
developers := List(Developer(
  id    = "jphmrst",
  name  = "John Maraist",
  email = "via-github@maraist.org",
  url   = url("https://maraist.org/work/")))
licenses += (
  "Educational",
  url("https://github.com/jphmrst/ScalaUtils/blob/master/LICENSE.txt"))

// disable publish with scala version, otherwise artifact name will
// include scala version
// e.g cassper_2.11
crossPaths := false

// add sonatype repository settings
// snapshot versions publish to sonatype snapshot repository
// other versions publish to sonatype staging repository
pomIncludeRepository := { _ => false }
val nexus = "https://s01.oss.sonatype.org/"
publishTo := {
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
publishMavenStyle := true

ThisBuild / versionScheme := Some("semver-spec")

// end of maven etc. publishing section
/////////////////////////////////////////////////////////////////

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.9"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % "test"
Global / excludeLintKeys ++= Set(scalacOptions)
Compile / doc / scalacOptions ++= Seq(
  "-groups",
  "-doc-root-content", "src/main/rootdoc.txt"
)

lazy val utils = project
  .in(file("."))
  .settings(
    scalaVersion := scala3Version,
    compile / watchTriggers += baseDirectory.value.toGlob / "build.sbt",
    unmanagedSources / excludeFilter := ".#*",
    scalacOptions ++= Seq( "-source:future-migration" ),
  )
