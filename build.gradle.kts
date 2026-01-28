plugins {
    alias(libs.plugins.androidApplication) apply false

    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false

    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinMultiplatformAndroidLibrary) apply false
}

buildscript {
  repositories {
    dependencies {
      classpath(libs.plugin.gradle.maven.publish)
    }
  }
}