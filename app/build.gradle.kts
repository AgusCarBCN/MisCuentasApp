

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")

}

android {
    namespace = "carnerero.agustin.cuentaappandroid"
    compileSdk = 35

    defaultConfig {
        applicationId = "carnerero.agustin.cuentaappandroid"
        minSdk = 26
        //noinspection OldTargetApi
        targetSdk = 34
        versionCode = 47
        versionName = "3.11"

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

    buildFeatures {
        compose = true


    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {

    implementation(libs.androidx.documentfile)
    implementation(libs.androidx.window)
    val roomVersion = "2.6.1"


    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$roomVersion")

    //room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.compose.material3)
    annotationProcessor(libs.androidx.room.compiler)
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.coil.compose)
    runtimeOnly(libs.androidx.material.icons.extended)
    implementation(libs.androidx.constraintlayout.v220beta01)
    implementation(libs.compose.modified.snackbar)
    // To use constraintlayout in compose
    implementation(libs.androidx.constraintlayout.compose)
    implementation (libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation (libs.commons.csv)
    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
    implementation (libs.circleindicator)
    //Dagger Hilt
    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler)
    //Chart
    implementation (libs.mpandroidchart)
    implementation (libs.accompanist.permissions)
    //Google adMob
    implementation (libs.play.services.ads)
}

kapt {
    correctErrorTypes= true
}




