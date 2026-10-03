plugins {
  alias(libs.plugins.android.application)
}

android {
  namespace = "com.orhanobut.sample"
  compileSdk = libs.versions.compileSdk.get().toInt()
  buildToolsVersion = libs.versions.buildTools.get()

  defaultConfig {
    applicationId = "com.orhanobut.sample"
    minSdk = 23
    targetSdk = libs.versions.compileSdk.get().toInt()
    versionCode = 1
    versionName = "1.0"
  }

  buildFeatures {
    buildConfig = true
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
}

dependencies {
  implementation(project(":logger"))
}
