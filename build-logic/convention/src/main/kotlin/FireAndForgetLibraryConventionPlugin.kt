import com.android.build.api.dsl.androidLibrary
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.publish.maven.MavenPom
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class FireAndForgetLibraryConventionPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("org.jetbrains.kotlin.multiplatform")
        apply("com.android.kotlin.multiplatform.library")
        apply("com.vanniktech.maven.publish")
        apply("signing")
      }

      val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

      val defaultNamespace = libs.findVersion("namespace").get().toString()

      // Configure Maven Publishing
      extensions.configure<MavenPublishBaseExtension> {
        publishToMavenCentral(validateDeployment = false)
        signAllPublications()

        pom { configurePom(this) }
      }

      extensions.configure<KotlinMultiplatformExtension> {
        applyDefaultHierarchyTemplate()
        withSourcesJar()

        androidLibrary {
          // Default namespace - modules MUST override this with their unique namespace
          namespace = defaultNamespace

          compileSdk = libs.findVersion("android-compileSdk").get().toString().toInt()
          minSdk = libs.findVersion("android-minSdk").get().toString().toInt()

          packaging {
            resources {
              excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
          }

          lint {
            checkReleaseBuilds = false
            abortOnError = false
          }

          compilations.configureEach {
            compileTaskProvider.configure {
              compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
              }
            }
          }
        }

        jvm()

        iosX64()
        iosArm64()
        iosSimulatorArm64()

        js(IR) {
          browser()
        }

        @OptIn(ExperimentalWasmDsl::class)
        wasmJs {
          browser()
        }
      }

      // Validate that module has overridden the namespace
      afterEvaluate {
        extensions.findByType<KotlinMultiplatformExtension>()?.let { kotlin ->
          kotlin.androidLibrary {
            val currentNamespace = namespace
            if (currentNamespace == defaultNamespace) {
              throw GradleException(
                """
                |Module '${project.path}' must override the namespace!
                |
                |Each library module must set a unique namespace in its build.gradle.kts:
                |
                |  kotlin {
                |    androidLibrary {
                |      namespace = libs.versions.namespace.get() + ".your.unique.suffix"
                |    }
                |  }
                |
                |Current namespace: $currentNamespace (this is the default and will cause conflicts)
                """.trimMargin()
              )
            }
          }
        }
      }
    }
  }

  private fun configurePom(pom: MavenPom) {
    pom.apply {
      name.set("FireAndForget")
      description.set("A lightweight Kotlin Multiplatform library for Compose Multiplatform that executes code once on first access, with optional auto-toggling behavior")
      url.set("https://github.com/alorma/FireAndForget")

      licenses {
        license {
          name.set("MIT License")
          url.set("https://github.com/alorma/FireAndForget/blob/main/LICENSE")
        }
      }

      developers {
        developer {
          id.set("alorma")
          name.set("Bernat Borrás")
          email.set("bernatbor15@gmail.com")
        }
      }

      scm {
        connection.set("scm:git:github.com/alorma/FireAndForget.git")
        developerConnection.set("scm:git:ssh://github.com/alorma/FireAndForget.git")
        url.set("https://github.com/alorma/FireAndForget/tree/main")
      }
    }
  }
}
