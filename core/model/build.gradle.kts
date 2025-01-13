plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "ru.paylab.core.model"
    compileSdk = 35

    defaultConfig {
        minSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.dagger)
    implementation(libs.hilt.android)
    implementation(libs.androidx.ui.text.android)
    implementation(libs.androidx.lifecycle.livedata.core)
    implementation(libs.core.ktx)
    implementation(libs.androidx.junit.ktx)

    ksp(libs.hilt.android.compiler)
    implementation(libs.rxandroid)
    implementation(libs.rxkotlin)
    implementation(libs.rxjava)
    implementation(libs.androidx.runtime.livedata)
    implementation (libs.gson)
    implementation(project(":core:database"))
    implementation(project(":core:localcache"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}