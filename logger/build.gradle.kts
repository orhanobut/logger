import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.maven.publish)
}

android {
  namespace = "com.orhanobut.logger"
  compileSdk = libs.versions.compileSdk.get().toInt()
  buildToolsVersion = libs.versions.buildTools.get()

  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  lint {
    textReport = true
  }
}

dependencies {
  api(libs.kotlin.stdlib) {
    // Kotlin metadata carries nullability; no annotation jar is needed at runtime.
    exclude(group = "org.jetbrains", module = "annotations")
  }
  testImplementation(libs.junit)
  // Android supplies org.json; JVM tests need a real implementation.
  testImplementation(libs.json)
}

mavenPublishing {
  configure(AndroidSingleVariantLibrary(
      JavadocJar.Javadoc(), SourcesJar.Sources(), "release"))
  publishToMavenCentral()
}
