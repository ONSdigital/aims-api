logLevel := Level.Warn
ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always

addSbtPlugin("com.typesafe.play"      %  "sbt-plugin"            % "2.9.1" exclude("org.slf4j", "slf4j-simple"))
addSbtPlugin("net.virtual-void"       %  "sbt-dependency-graph"  % "0.9.2")
addSbtPlugin("com.eed3si9n"           %  "sbt-assembly"          % "2.2.0")
addSbtPlugin("com.github.sbt"         %  "sbt-jni"               % "1.7.0" )
addSbtPlugin("io.spray"               %  "sbt-revolver"          % "0.10.0")
addSbtPlugin("com.github.sbt"         %  "sbt-git"               % "2.0.1")
addSbtPlugin("io.github.play-swagger" %  "sbt-play-swagger"       % "1.7.0")
addSbtPlugin("org.scalastyle"         %  "scalastyle-sbt-plugin" % "1.0.0")
addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat"         % "1.2.3")
addSbtPlugin("org.scoverage"          %  "sbt-scoverage"          % "2.0.11")
addSbtPlugin("com.codacy"             %  "sbt-codacy-coverage"    % "3.0.3")
addSbtPlugin("com.github.sbt"         %  "sbt-javaagent"          % "0.1.7")
addDependencyTreePlugin

addSbtPlugin("com.timushev.sbt"       %  "sbt-updates"            % "0.6.4")