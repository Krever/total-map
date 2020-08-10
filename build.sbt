lazy val root = (project in file("."))
  .aggregate(core, enumeratum, pureconfig, docs)
  .settings(noPublishSettings)
  .settings(
    name := "total-map"
  )

lazy val docs = (project in file("docs"))
  .settings(commonSettings, noPublishSettings)
  .dependsOn(core, enumeratum, pureconfig)
  .enablePlugins(MicrositesPlugin)
  .settings(
    name := "docs",
    micrositeName := "TotalMap",
    micrositeDescription := "Simple library for total maps",
    micrositeAuthor := "Wojtek Pituła",
    micrositeGithubOwner := "Krever",
    micrositeGithubRepo := "total-map",
    micrositeBaseUrl := "/total-map",
    micrositeUrl := "http://w.pitula.me",
    micrositeGitterChannel := true,
    micrositeCompilingDocsTool := WithMdoc,
    mdocIn := file("docs/mdoc"),
    mdocVariables := Map(
      "VERSION" -> version.value
    ),
    micrositePushSiteWith := GitHub4s,
    micrositeGithubToken := sys.env.get("GITHUB_TOKEN")
  )

lazy val core = (project in file("core"))
  .settings(commonSettings, publishSettings)
  .settings(
    name := "totalmap-core"
  )

lazy val enumeratum = (project in file("modules/enumeratum"))
  .settings(commonSettings, publishSettings)
  .dependsOn(core)
  .settings(
    name := "totalmap-enumeratum",
    libraryDependencies += "com.beachape" %% "enumeratum" % "1.6.1"
  )

lazy val pureconfig = (project in file("modules/pureconfig"))
  .settings(commonSettings, publishSettings)
  .dependsOn(core)
  .settings(
    name := "totalmap-pureconfig",
    libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.13.0"
  )

lazy val commonSettings = Seq(
  organization := "com.github.krever",
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  )
)

lazy val publishSettings = Seq(
  pomExtra :=
    <developers>
      <developer>
        <id>krever</id>
        <name>Wojciech Pituła</name>
        <url>http://w.pitula.me</url>
      </developer>
    </developers>,
  homepage := Some(url(s"https://w.pitula.me/total-map")),
  licenses := Seq("MIT License" -> url("http://opensource.org/licenses/mit-license.php")),
  scmInfo := Some(
    ScmInfo(
      url(s"https://github.com/krever/total-map"),
      s"scm:git:git@github.com:krever/total-map.git"
    )
  ),
  useGpg := false,
  pgpPublicRing := file("./pubring.asc"),
  pgpSecretRing := file("./secring.asc"),
  pgpPassphrase := sys.env.get("PGP_PASSPHRASE").map(_.toArray)
)

val noPublishSettings = commonSettings ++ Seq(
  skip in publish := true,
  publishArtifact := false,
  publish := ((): Unit),
  publishLocal := ((): Unit),
  //  publishLocal := ()
)

ThisBuild / publishTo := sonatypePublishToBundle.value
ThisBuild / credentials ++= (
  for {
    username <- sys.env.get("SONATYPE_USER")
    password <- sys.env.get("SONATYPE_PASSWORD")
  } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)
).toList
