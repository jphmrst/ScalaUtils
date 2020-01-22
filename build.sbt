ThisBuild / scalaVersion := "2.12.10"
ThisBuild / organization := "org.maraist"

lazy val hello = (project in file("."))
  .settings(
    name := "Maraist Utilities"
  )
