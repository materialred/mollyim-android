import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("java-library")
  id("org.jetbrains.kotlin.jvm")
  id("java-test-fixtures")
  id("idea")
  id("com.squareup.wire")
}

val signalBuildToolsVersion: String by rootProject.extra
val signalCompileSdkVersion: String by rootProject.extra
val signalTargetSdkVersion: Int by rootProject.extra
val signalMinSdkVersion: Int by rootProject.extra
val signalJavaVersion: JavaVersion by rootProject.extra
val signalKotlinJvmTarget: String by rootProject.extra

java {
  withJavadocJar()
  withSourcesJar()
  sourceCompatibility = signalJavaVersion
  targetCompatibility = signalJavaVersion
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions {
    jvmTarget = signalKotlinJvmTarget
  }
}

afterEvaluate {
  tasks.named("sourcesJar", Jar::class.java) {
    dependsOn("generateMainProtos")
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
  }
}

wire {
  protoLibrary = true

  kotlin {
    javaInterop = true
  }

  sourcePath {
    srcDir("src/main/protowire")
  }

  custom {
    // Comes from wire-handler jar project
    schemaHandlerFactoryClass = "org.signal.wire.Factory"
  }
}

tasks.whenTaskAdded {
  if (name == "lint") {
    enabled = false
  }
}

dependencies {
  api(libs.google.libphonenumber)
  api(libs.jackson.core)

  implementation(libs.libsignal.client)
  api(libs.square.okhttp3)
  api(libs.square.okhttp3.dnsoverhttps)
  api(libs.square.okio)
  implementation(libs.google.jsr305)

  api(libs.rxjava3.rxjava)

  implementation(libs.kotlin.stdlib.jdk8)

  implementation(project(":core-util-jvm"))

  testImplementation(testLibs.junit.junit)
  testImplementation(testLibs.assertj.core)
  testImplementation(testLibs.conscrypt.openjdk.uber)
  testImplementation(testLibs.mockito.core)
  testImplementation(testLibs.mockk)

  testFixturesImplementation(libs.libsignal.client)
  testFixturesImplementation(testLibs.junit.junit)
}
