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
      implementation(projects.multiplatformSettings)

      implementation(libs.multiplatform.settings)
      implementation(libs.multiplatform.settings.no.arg)

      implementation(libs.compose.runtime)
      implementation(libs.compose.foundation)
      implementation(libs.compose.material3)
      implementation(libs.compose.ui)
      implementation(libs.compose.components.resources)
      implementation(libs.compose.ui.tooling.preview)
    }
  }
}
