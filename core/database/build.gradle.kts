plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "ru.paylab.core.database"
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
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
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.rxjava3)
    implementation(libs.androidx.room.rxjava2)
    implementation(libs.androidx.room.guava)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.androidx.room.testing)
    implementation(libs.androidx.room.paging)

    implementation(libs.kotlinx.datetime)

    testImplementation(libs.junit)
    testImplementation( libs.kotlinx.coroutines.test )


    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation( libs.kotlinx.coroutines.test )
}