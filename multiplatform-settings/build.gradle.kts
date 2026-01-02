import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.kotlinMultiplatformAndroidLibrary)
}

kotlin {
  applyDefaultHierarchyTemplate()
  withSourcesJar()

  androidLibrary {
    namespace = libs.versions.namespace.get() + "multiplatform.settings"
    minSdk = libs.versions.android.minSdk.get().toInt()
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_11)
    }
  }

  iosX64()
  iosArm64()
  iosSimulatorArm64()

  jvm()

  js(IR) {
    browser()
  }

  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser()
  }

  sourceSets {
    commonMain.dependencies {
      implementation(projects.core)

      implementation(libs.multiplatform.settings)
    }
  }
}