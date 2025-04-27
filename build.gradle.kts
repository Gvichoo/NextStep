// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    //Hilt
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    //Firebase
    id("com.google.gms.google-services") version "4.4.2" apply false
    // Navigation safe args
    alias(libs.plugins.androidx.navigation.safeargs.kotlin) apply false
    //Compose
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}