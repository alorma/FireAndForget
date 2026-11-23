rootProject.name = "FireAndForget"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  includeBuild("build-logic")
  repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
  }
}

include(":core")
include(":multiplatform-settings")
include(":samples:shared")
include(":samples:androidApp")
include(":samples:desktopApp")
