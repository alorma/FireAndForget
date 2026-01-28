plugins {
  id("fireAndForget.library")
}

kotlin {
  androidLibrary {
    namespace = libs.versions.namespace.get() + ".core"
  }
}