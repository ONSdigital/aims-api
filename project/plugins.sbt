logLevel := Level.Warn
ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
//ThisBuild / scalaVersion := "2.13.13"
addSbtPlugin("com.typesafe.play"      %  "sbt-plugin"            % "2.8.8" exclude("org.slf4j", "slf4j-simple"))
addSbtPlugin("net.virtual-void"       %  "sbt-dependency-graph"  % "0.9.2")
addSbtPlugin("com.eed3si9n"           %  "sbt-assembly"          % "1.0.0")
//addSbtPlugin("com.github.sbt"         %  "sbt-native-packager"   % "1.9.2")
addSbtPlugin("com.github.sbt"         %  "sbt-jni"               % "1.5.3" )
addSbtPlugin("io.spray"               %  "sbt-revolver"          % "0.9.1")
addSbtPlugin("com.typesafe.sbt"       %  "sbt-git"               % "1.0.0")
addSbtPlugin("com.iheart"             %  "sbt-play-swagger"      % "0.10.6-PLAY2.8")

addSbtPlugin("org.scalastyle"         %  "scalastyle-sbt-plugin" % "1.0.0")
addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat"         % "1.2.3")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.8.2")
addSbtPlugin("com.codacy" % "sbt-codacy-coverage" % "3.0.3")

addDependencyTreePlugin

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.6.0")