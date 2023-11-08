plugins {
  val indraVersion = "3.1.3"
  id("com.diffplug.spotless") version "6.22.0"
  id("net.kyori.indra") version indraVersion
  id("net.kyori.indra.checkstyle") version indraVersion
  id("net.kyori.indra.publishing") version indraVersion
  id("net.kyori.indra.publishing.sonatype") version indraVersion
  id("net.ltgt.errorprone") version "3.1.0"
}

group = "com.seiama"
description = "A set of extensions for Guice-based dependency injection"
version = "1.0.0-SNAPSHOT"

indra {
  github("seiama", "injection") {
    ci(true)
  }

  mitLicense()

  javaVersions {
    target(17)
  }

  configurePublications {
    pom {
      developers {
        developer {
          id.set("kashike")
          name.set("Riley Park")
          timezone.set("America/Vancouver")
        }
      }
    }
  }
}

indraSonatype {
  useAlternateSonatypeOSSHost("s01")
}

spotless {
  java {
    endWithNewline()
    importOrderFile(rootProject.file(".spotless/seiama.importorder"))
    indentWithSpaces(2)
    licenseHeaderFile(rootProject.file("license_header.txt"))
    trimTrailingWhitespace()
  }
}

tasks.named<Jar>(JavaPlugin.JAR_TASK_NAME) {
  indraGit.applyVcsInformationToManifest(manifest)
}

repositories {
  mavenCentral()
}

dependencies {
  annotationProcessor("ca.stellardrift:contract-validator:1.0.1")
  checkstyle("ca.stellardrift:stylecheck:0.2.1")
  errorprone("com.google.errorprone:error_prone_core:2.23.0")
  compileOnlyApi("org.jetbrains:annotations:24.0.1")
  compileOnlyApi("org.jspecify:jspecify:0.3.0")
  api("com.google.inject:guice:7.0.0")
  api("com.google.inject.extensions:guice-assistedinject:7.0.0")
  testImplementation("com.google.guava:guava-testlib:32.1.3-jre")
  testImplementation(platform("org.junit:junit-bom:5.10.1"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
