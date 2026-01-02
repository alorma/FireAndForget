import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  id("fireAndForget.library")
}

kotlin {

  androidLibrary {
    namespace = libs.versions.namespace.get() + "multiplatform.settings"
  }

  sourceSets {
    commonMain.dependencies {
      implementation(projects.core)
      implementation(libs.multiplatform.settings)
    }
  }
}