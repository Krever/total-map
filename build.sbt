

lazy val root = (project in file("."))
  .aggregate(core, enumeratum, pureconfig, docs)
  .settings(noPublishSettings)

lazy val docs = (project in file("docs"))
  .settings(commonSettings, noPublishSettings)
  .settings(name := "total-map")
  .dependsOn(core, enumeratum, pureconfig)
  .enablePlugins(MicrositesPlugin)
  .settings(
    name := "total-map",
    micrositeCompilingDocsTool := WithMdoc,
    micrositeName := "TotalMap",
    micrositeDescription := "Simple library for total maps",
    micrositeUrl := "http://w.pitula.me",
    micrositeBaseUrl := "/total-map",
    micrositeGitterChannel := true,
    micrositeGithubOwner := "krever",
    micrositeGithubRepo := "total-map",
    mdocIn := file("."),
    mdocVariables := Map(
      "VERSION" -> version.value
    ),
    micrositePushSiteWith := GitHub4s,
    micrositeGithubToken := sys.env.get("GITHUB_TOKEN")
  )

lazy val core = (project in file("core"))
  .settings(commonSettings, publishSettings)

lazy val enumeratum = (project in file("modules/enumeratum"))
  .settings(commonSettings, publishSettings)
  .dependsOn(core)
  .settings(
    libraryDependencies += "com.beachape" %% "enumeratum" % "1.5.13"
  )

lazy val pureconfig = (project in file("modules/pureconfig"))
  .settings(commonSettings, publishSettings)
  .dependsOn(core)
  .settings(
    libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.10.2"
  )


lazy val commonSettings = Seq(
  organization := "com.github.krever.totalmap",
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  )
)

lazy val publishSettings = commonSettings ++ Seq(
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
//  pgpPublicRing := file("pubring.asc"),
//  pgpSecretRing := file("secring.asc"),
//  pgpPassphrase := sys.env.get("PGP_PASSPHRASE").map(_.toArray),
  credentials ++= (
    for {
      username <- sys.env.get("SONATYPE_USER")
      password <- sys.env.get("SONATYPE_PASSWORD")
    } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)
    ).toList
)

val noPublishSettings = commonSettings ++ Seq(
  publishArtifact := false,
  publish := (),
  publishLocal := ()
  //  publishLocal := ()
)