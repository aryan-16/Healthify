import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}
// Load local.properties file
// Load the local.properties file and retrieve the API key


// Load local.properties file and read the API key
val localProperties = rootProject.file("local.properties")
val mapsApiKey = if (localProperties.exists()) {
    Properties().apply {
        load(localProperties.inputStream())
    }.getProperty("MAPS_API_KEY") ?: ""
} else {
    ""
}

android {
    namespace = "com.example.healthify"
    compileSdk = 35

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
    defaultConfig {
        applicationId = "com.example.healthify"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "MAPS_API_KEY", "\"$mapsApiKey\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

        implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
        implementation(libs.kotlin.stdlib.jdk7) // Updated to latest Kotlin version
        implementation(libs.androidx.appcompat) // Updated AppCompat
        implementation(libs.androidx.core.ktx.v1101) // Updated Core KTX
        implementation(libs.androidx.constraintlayout.v214) // Updated ConstraintLayout
        testImplementation(libs.junit) // Updated JUnit
        androidTestImplementation(libs.androidx.junit.v115) // Updated AndroidX Test JUnit
        androidTestImplementation(libs.androidx.espresso.core.v351) // Updated Espresso Core

// Material Design
        implementation(libs.material.v190) // Updated Material Design

// Architectural Components
        implementation(libs.androidx.lifecycle.viewmodel.ktx) // Updated Lifecycle ViewModel KTX

// Room

    implementation(libs.androidx.room.runtime)  // Latest version of Room
    kapt("androidx.room:room-compiler:2.6.1")
    implementation(libs.androidx.room.ktx)

// Kotlin Extensions and Coroutines support for Room
        implementation(libs.androidx.room.ktx) // Updated Room KTX

// Coroutines
        implementation(libs.kotlinx.coroutines.core) // Updated Coroutines Core
        implementation(libs.kotlinx.coroutines.android) // Updated Coroutines Android

// Coroutine Lifecycle Scopes
        implementation(libs.androidx.lifecycle.viewmodel.ktx.v261) // Updated Lifecycle ViewModel KTX
        implementation(libs.androidx.lifecycle.runtime.ktx) // Updated Lifecycle Runtime KTX


// Navigation Components
        implementation(libs.androidx.navigation.fragment.ktx) // Updated Navigation Fragment KTX
        implementation(libs.androidx.navigation.ui.ktx) // Updated Navigation UI KTX

// Glide
        implementation(libs.glide) // Updated Glide
        kapt(libs.compiler) // Updated Glide Compiler

// Google Maps Location Services
        implementation(libs.play.services.location) // Updated Play Services Location
        implementation(libs.play.services.maps) // Updated Play Services Maps

// Dagger Core
        implementation(libs.dagger) // Updated Dagger Core
        kapt(libs.dagger.compiler) // Updated Dagger Compiler

// Dagger Android
        api(libs.dagger.android) // Updated Dagger Android
        api(libs.dagger.android.support) // Updated Dagger Android Support
        kapt(libs.dagger.android.processor) // Updated Dagger Android Processor

        implementation(libs.hilt.android)
        kapt(libs.hilt.android.compiler)

// Easy Permissions
        implementation(libs.easypermissions) // Latest version as of now

// Timber
        implementation(libs.timber) // Updated Timber

// MPAndroidChart
        implementation(libs.mpandroidchart) // Latest version as of now

        implementation(libs.androidx.lifecycle.extensions) // Note: lifecycle-extensions is deprecated
    }

