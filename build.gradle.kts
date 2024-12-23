plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    kotlin("kapt") version "1.7.20" apply false  // Menambahkan kapt untuk Kotlin
}
