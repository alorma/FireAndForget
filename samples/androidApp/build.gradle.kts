plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidApplication)
}

android {
  namespace = libs.versions.namespace.get() + ".android"

  compileSdk = libs.versions.android.compileSdk
    .get()
    .toInt()

  defaultConfig {
    applicationId = libs.versions.namespace.get() + ".android"
    minSdk = libs.versions.android.minSdkSample
      .get()
      .toInt()
    targetSdk = libs.versions.android.targetSdk
      .get()
      .toInt()

    versionCode = 1
    versionName = "1.0"
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
}

kotlin {
  androidTarget()

  sourceSets {
    androidMain.dependencies {
      implementation(projects.samples.shared)
      implementation(libs.androidx.activity.compose)
    }
  }
}
