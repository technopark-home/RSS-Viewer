plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("D:\\Android\\key\\KeysSign.jks")
            storePassword = "000000"
            keyPassword = "000000"
            keyAlias = "key0"
        }
    }
    namespace = "ru.paylab.app.rssviewer"
    compileSdk = 35

    defaultConfig {
        applicationId = "ru.paylab.app.rssviewer"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material3)
    implementation(libs.androidx.material3.adaptive.navigation.suite)
    implementation(libs.rxandroid)
    implementation(libs.rxkotlin)
    implementation(libs.rxjava)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.adapter.rxjava2)
    implementation(libs.retrofit)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.paging.compose.android)
    implementation(libs.coil.compose)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.constraintlayout.compose.android)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.material.icons.extended.android)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.coil.network.okhttp)
    implementation(libs.coil3.coil.gif)

    implementation(libs.dagger)
    implementation(libs.hilt.android)
    implementation(libs.androidx.foundation.layout.android)

    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android.gradle.plugin)
    implementation(libs.kotlinx.datetime)
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:localcache"))
    implementation(project(":feature:bookmark"))
    implementation(project(":feature:articledownload"))
    implementation(project(":feature:viewdownloaded"))
    implementation(project(":feature:categories"))
    implementation(project(":feature:searcharticles"))
    implementation(project(":feature:articlesviewer"))
    implementation(project(":feature:uploadedarticles"))
    implementation(project(":feature:articledescription"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:infonavbar"))

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.rxjava3)
    implementation(libs.androidx.room.rxjava2)
    implementation(libs.androidx.room.guava)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.androidx.room.testing)
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.datastore.preferences)
    implementation (libs.gson)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}
