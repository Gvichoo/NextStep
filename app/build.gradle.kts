plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    //Hilt
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    //Firebase
    id("com.google.gms.google-services")
    // Navigation safe args
    alias(libs.plugins.androidx.navigation.safeargs.kotlin)
}

android {
    namespace = "com.tbacademy.nextstep"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tbacademy.nextstep"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.runtime.android)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.work)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //Retrofit
    implementation(libs.retrofit)
    //Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    //Coroutine
    implementation(libs.kotlinx.coroutines.android)
    //Glide
    implementation (libs.glide)
    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    //FireStore
    implementation(libs.firebase.firestore)
    //DataStore Preferences
    implementation(libs.androidx.datastore.preferences)
    //FirebaseGoogle
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    implementation (libs.firebase.auth.ktx)
    implementation (libs.play.services.auth)

    implementation (libs.google.firebase.auth.ktx)
    // Google Identity Services
    implementation (libs.play.services.auth)

    //WorkManager
    implementation("androidx.work:work-runtime-ktx:2.10.0")

    implementation ("androidx.hilt:hilt-work:1.1.0")
    kapt ("androidx.hilt:hilt-compiler:1.2.0")


}