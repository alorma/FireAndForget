plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.kotlinMultiplatformAndroidLibrary)

  alias(libs.plugins.composeCompiler)
  alias(libs.plugins.composeMultiplatform)
}

kotlin {
  androidLibrary {
    namespace = libs.versions.namespace.get() + ".shared"
    minSdk = libs.versions.android.minSdkSample.get().toInt()
    compileSdk = libs.versions.android.compileSdk.get().toInt()
  }

  jvm()

  sourceSets {
    commonMain.dependencies {
      implementation(projects.core)
      implementation(compose.runtime)
      implementation(compose.foundation)
      implementation(compose.material3)
      implementation(compose.ui)
      implementation(compose.components.resources)
      implementation(compose.components.uiToolingPreview)
    }
  }
}
