import com.iheart.sbtPlaySwagger.SwaggerPlugin.autoImport.swaggerDomainNameSpaces
import com.typesafe.sbt.SbtNativePackager.autoImport.NativePackagerHelper._
import com.typesafe.sbt.packager.universal.ZipHelper
import com.typesafe.sbt.web.SbtWeb
import play.sbt.PlayScala
import play.sbt.routes.RoutesKeys._
import sbt.Keys.{mappings, packageBin, scalaVersion, _}
import sbt.Resolver.{file => _, url => _}
import sbt._
import sbtassembly.AssemblyPlugin.autoImport._
import spray.revolver.RevolverPlugin.autoImport.Revolver
import com.typesafe.sbt.packager.docker._

lazy val Versions = new {
  val elastic4s = "8.14.1"
  val scala = "2.13.13"
  val scapegoatVersion = "2.1.5"
  val akkaVersion = "2.8.5"
}

name := "address-index"
scmInfo := Some(
  ScmInfo(
    browseUrl = url("https://github.com/ONSdigital/address-index-api"),
    connection = "https://github.com/ONSdigital/address-index-api"
  )
)

lazy val assemblySettings: Seq[Def.Setting[_]] = Seq(
  Universal / mappings ++= directory("parsers/src/main/resources"),
  assembly / assemblyJarName := "ons-ai-api.jar",
  assembly / mainClass := Some("play.core.server.AkkaHttpServerProvider"),
  assembly / assemblyMergeStrategy := {
    // case PathList("META-INF", "io.netty.versions.properties", xs@_ *) => MergeStrategy.last
    case PathList("org", "joda", "time", "base", "BaseDateTime.class") => MergeStrategy.first // ES shades Joda
    case x =>
      val oldStrategy = (assembly / assemblyMergeStrategy).value
      oldStrategy(x)
  }
)

lazy val serverUniversalMappings: Seq[Def.Setting[_]] = Seq(
  // The following will cause parsers/.../resources directory to be added to the list of mappings recursively
  // excluding .md and .conf files
  Universal / mappings ++= {
    val rootDir = baseDirectory.value.getParentFile

    def directoryToAdd = rootDir / "parsers/src/main/resources"

    (directoryToAdd.allPaths * ("*" -- ("*.md" || "*.conf"))) pair relativeTo(rootDir)
  }
)

lazy val Resolvers: Seq[MavenRepository] = Seq(
  Resolver.typesafeRepo("releases"),
  Resolver.jcenterRepo,
  "elasticsearch-releases" at "https://maven.elasticsearch.org/releases",
  "Java.net Maven2 Repository" at "https://download.java.net/maven/2/",
  "Twitter Repository" at "https://maven.twttr.com",
  "Artima Maven Repository" at "https://repo.artima.com/releases",
  "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
)


lazy val localCommonSettings: Seq[Def.Setting[_]] = Seq(
  ThisBuild / scalaVersion  := Versions.scala,
  ThisBuild / scapegoatVersion := Versions.scapegoatVersion,
  ThisBuild / scalacOptions ++= Seq(
    "-release:21",
    "-encoding", "UTF-8",
    "-deprecation", // warning and location for usages of deprecated APIs
    "-feature", // warning and location for usages of features that should be imported explicitly
    "-unchecked", // additional warnings where generated code depends on assumptions
    "-Wconf:cat=lint-multiarg-infix:silent", // OK to suppress unless going Dotty
    "-Xlint", // recommended additional warnings
    "-Xlint:-byname-implicit", // scala 2.13.3 introduced, many false positives
    "-Xlint:-missing-interpolator",
    "-Xcheckinit", // runtime error when a val is not initialized due to trait hierarchies (instead of NPE somewhere else)
    "-Ywarn-value-discard", // Warn when non-Unit expression results are unused
    "-Ywarn-dead-code", // Warn when dead code is identified
    "-Ywarn-unused" // Warn when local and private vals, vars, defs, and types are unused
  ),
  // TODO: Fix the following errors highlighted by scapegoat. Remove the corresponding overrides below.
  Scapegoat / scalacOptions += "-P:scapegoat:overrideLevels:TraversableHead=Warning:OptionSize=Warning:ComparingFloatingPointTypes=Warning",
  scalaModuleInfo ~= (_.map(_.withOverrideScalaVersion(true))),
  resolvers ++= Resolvers,
  coverageExcludedPackages := ".*Routes.*;.*ReverseRoutes.*;.*javascript.*"
)

val commonDeps = Seq(
  "org.scalatest" %% "scalatest" % "3.2.18" % Test,
  "org.scalamock" %% "scalamock" % "6.0.0" % Test,
  "com.github.nscala-time" %% "nscala-time" % "2.32.0" % Test,
  "com.typesafe" % "config" % "1.4.3",
  "com.github.pureconfig" %% "pureconfig" % "0.17.6",
  "com.lihaoyi" %% "pprint" % "0.9.0",
  "nl.gn0s1s" %% "elastic4s-core" % Versions.elastic4s excludeAll ExclusionRule(organization = "org.apache.logging.log4j"),
  "nl.gn0s1s" %% "elastic4s-client-esjava" % Versions.elastic4s excludeAll ExclusionRule(organization = "org.apache.logging.log4j"),
  // testing
  "nl.gn0s1s" %% "elastic4s-testkit" % Versions.elastic4s % "test",
  "org.apache.logging.log4j" % "log4j-core" % "2.23.1" % Test,
  "org.apache.logging.log4j" % "log4j-api" % "2.23.1" % Test,
  "org.apache.commons" % "commons-lang3" % "3.14.0",
  "com.softwaremill.retry" %% "retry" % "0.3.6",
  "org.apache.httpcomponents" % "httpcore" % "4.4.16",
  "org.apache.httpcomponents" % "httpclient" % "4.5.14",
  "org.elasticsearch.client" % "elasticsearch-rest-client" % "8.14.3",
  "org.testcontainers" % "elasticsearch" % "1.20.0" % "test",
  guice
)

val modelDeps = Seq(ws) ++ commonDeps

val parsersDeps = commonDeps

val serverDeps = Seq(
  filters,
  specs2 % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test,
  "org.webjars" % "swagger-ui" % "5.13.0",
  "io.github.play-swagger" %% "play-swagger" % "2.0.0",
  "com.typesafe.akka" %% "akka-actor-typed" % Versions.akkaVersion,
  "com.typesafe.akka" %% "akka-protobuf-v3" % Versions.akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % Versions.akkaVersion,
  "com.typesafe.akka" %% "akka-serialization-jackson" % Versions.akkaVersion,
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.17.0"
) ++ commonDeps

lazy val `address-index` = project.in(file("."))
  .settings(
    publishLocal := {},
    publish := {}
  )
  .aggregate(
    `address-index-model`,
    `address-index-server`,
    `address-index-parsers`
  )

lazy val `address-index-model` = project.in(file("model"))
  .settings(localCommonSettings: _*)
  .settings(
    libraryDependencies ++= modelDeps
  ).dependsOn(
  `address-index-parsers`
)

lazy val `address-index-server` = project.in(file("server"))
  .settings(localCommonSettings: _*)
  .settings(serverUniversalMappings: _*)
  .settings(
    libraryDependencies ++= serverDeps,
    // "-Dlogback.debug=true" can be set to show which logfile is being used.
    dockerChmodType := DockerChmodType.UserGroupWriteExecute,
    dockerBaseImage := "eclipse-temurin:21",
    dockerCommands += ExecCmd("CMD", "-Dlogger.file=/opt/docker/conf/logback-gcp.xml"),
    routesGenerator := InjectedRoutesGenerator,
    swaggerV3 := true,
    swaggerDomainNameSpaces := Seq("uk.gov.ons.addressIndex.model.server.response"),
    Revolver.settings ++ Seq(
      reStart / mainClass := Some("play.core.server.ProdServerStart")
    ),
    Universal / packageBin := {
      // Get the name of the package being built
      val originalFileName = ( Universal / packageBin).value

      // Create a new temp file name.
      val (base, ext) = originalFileName.baseAndExt
      val newFileName = file(originalFileName.getParent) / (base + "_dist." + ext)

      // Unzip the zip archive created
      val extractedFiles = IO.unzip(originalFileName, file(originalFileName.getParent))

      // Move any files in "parsers" directory to root
      val mappings: Set[(File, String)] = extractedFiles.map(f => {
        val relativePathWithoutRootDir = f.getAbsolutePath.substring(originalFileName.getParent.length + base.length + 2)
        val relativePathWithRootDir = f.getAbsolutePath.substring(originalFileName.getParent.length + 1)

        val justFileName = f.getName

        // if path of file starts with parsers, then add to root of zip
        if (relativePathWithoutRootDir.startsWith("parsers")) {
          val (base, ext) = f.baseAndExt
          (f, justFileName)
        } else {
          (f, relativePathWithRootDir)
        }
      })

      // Set files under bin as executable just as the universalBin task does
      val binFiles = mappings.filter { case (file, path) => path.startsWith("bin/") }
      for (f <- binFiles) f._1.setExecutable(true)

      // Zip the files up and rename to the original zip name
      ZipHelper.zip(mappings, newFileName)
      IO.move(newFileName, originalFileName)

      // Create a copy of the original distribution to have a predictable name used by Jenkins jobs to
      // push application bundle to Cloud Foundry
      IO.copyFile(originalFileName, file(originalFileName.getParent) / s"${name.value}.zip")

      // Delete temporary directory
      IO.delete(file(originalFileName.getParent + "/" + originalFileName.base))

      originalFileName
    },
    Compile / resourceGenerators += Def.task {
      // Note that version.sbt inside server is the one being read
      val file = (Compile / resourceManaged).value / "version.app"
      IO.write(file, (ThisBuild / version).value)
      Seq(file)
    }.taskValue
  )
  .dependsOn(
    `address-index-model`
  )
  .enablePlugins(
    PlayScala,
    PlayAkkaHttpServer,
    LauncherJarPlugin,
    SbtWeb,
    JavaAppPackaging,
    GitVersioning,
    SwaggerPlugin,
    DockerPlugin,
    JavaAgent
  )

lazy val `address-index-parsers` = project.in(file("parsers"))
  .settings(localCommonSettings: _*)
  .settings(libraryDependencies ++= parsersDeps)

