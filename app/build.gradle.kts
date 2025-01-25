plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.healthify"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.healthify"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    dependencies {
        implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.10") // Updated to latest Kotlin version
        implementation("androidx.appcompat:appcompat:1.7.0") // Updated AppCompat
        implementation("androidx.core:core-ktx:1.10.1") // Updated Core KTX
        implementation("androidx.constraintlayout:constraintlayout:2.1.4") // Updated ConstraintLayout
        testImplementation("junit:junit:4.13.2") // Updated JUnit
        androidTestImplementation("androidx.test.ext:junit:1.1.5") // Updated AndroidX Test JUnit
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // Updated Espresso Core

// Material Design
        implementation("com.google.android.material:material:1.9.0") // Updated Material Design

// Architectural Components
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1") // Updated Lifecycle ViewModel KTX

// Room
        implementation("androidx.room:room-runtime:2.6.0") // Updated Room Runtime
        kapt("androidx.room:room-compiler:2.6.0") // Updated Room Compiler

// Kotlin Extensions and Coroutines support for Room
        implementation("androidx.room:room-ktx:2.6.0") // Updated Room KTX

// Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2") // Updated Coroutines Core
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.2") // Updated Coroutines Android

// Coroutine Lifecycle Scopes
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1") // Updated Lifecycle ViewModel KTX
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1") // Updated Lifecycle Runtime KTX

// Navigation Components
        implementation("androidx.navigation:navigation-fragment-ktx:2.7.1") // Updated Navigation Fragment KTX
        implementation("androidx.navigation:navigation-ui-ktx:2.7.1") // Updated Navigation UI KTX

// Glide
        implementation("com.github.bumptech.glide:glide:4.15.0") // Updated Glide
        kapt("com.github.bumptech.glide:compiler:4.15.0") // Updated Glide Compiler

// Google Maps Location Services
        implementation("com.google.android.gms:play-services-location:21.0.1") // Updated Play Services Location
        implementation("com.google.android.gms:play-services-maps:18.1.0") // Updated Play Services Maps

// Dagger Core
        implementation("com.google.dagger:dagger:2.48") // Updated Dagger Core
        kapt("com.google.dagger:dagger-compiler:2.48") // Updated Dagger Compiler

// Dagger Android
        api("com.google.dagger:dagger-android:2.48") // Updated Dagger Android
        api("com.google.dagger:dagger-android-support:2.4") // Updated Dagger Android Support
        kapt("com.google.dagger:dagger-android-processor:2.48") // Updated Dagger Android Processor

// Easy Permissions
        implementation("pub.devrel:easypermissions:3.0.0") // Latest version as of now

// Timber
        implementation("com.jakewharton.timber:timber:5.0.1") // Updated Timber

// MPAndroidChart
        implementation("com.github.PhilJay:MPAndroidChart:v3.1.0") // Latest version as of now

        implementation("androidx.lifecycle:lifecycle-extensions:2.2.0") // Note: lifecycle-extensions is deprecated
    }

}