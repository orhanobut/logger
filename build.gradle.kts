buildscript {
  repositories {
    google()
    mavenCentral()
  }
  dependencies {
    // Upgrade the compiler used by AGP's built-in Kotlin support.
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.20")
  }
}

plugins {
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.maven.publish) apply false
}

subprojects {
  group = providers.gradleProperty("GROUP").get()
  version = providers.gradleProperty("VERSION_NAME").get()
}

tasks.register<Delete>("clean") {
  delete(layout.buildDirectory)
}
