name := "scalecamp"

version := "1.0"

organization := "uk.co.bruntonspall"

scalaVersion := "2.9.1"

resolvers += "Objectify Repo" at "http://objectify-appengine.googlecode.com/svn/maven"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "0.9.26",
  "com.weiglewilczek.slf4s" %% "slf4s" % "1.0.7",
  "org.scalatra" %% "scalatra" % "2.0.4",
  "org.skife.com.typesafe.config" % "typesafe-config" % "0.3.0",
  "org.scribe" % "scribe" % "1.3.0",
  "com.googlecode.objectify" % "objectify" % "3.1",
  "net.liftweb" %% "lift-json" % "2.4-M4",
  "net.liftweb" %% "lift-json-ext" % "2.4-M4",
  "javax.persistence" % "persistence-api" % "1.0",
  "com.google.appengine" % "appengine-api-1.0-sdk" % "1.6.2",
  "org.scalatest" %% "scalatest" % "1.6.1" % "test",
  "javax.servlet" % "servlet-api" % "2.3" % "provided",
  "org.mortbay.jetty" % "jetty" % "6.1.22" % "container")

seq(appengineSettings: _*)

seq(Twirl.settings: _*)
