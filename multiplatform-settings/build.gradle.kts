plugins {
  id("fireAndForget.library")
}

kotlin {
  androidLibrary {
    namespace = libs.versions.namespace.get() + ".multiplatform.settings"
  }

  sourceSets {
    commonMain.dependencies {
      implementation(projects.core)
      implementation(libs.multiplatform.settings)
    }
  }
}