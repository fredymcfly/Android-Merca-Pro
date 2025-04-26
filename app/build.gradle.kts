plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.googleServices)

}

android {
    namespace = "com.example.merca_pro"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.merca_pro"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
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
    implementation(libs.firebase.common.ktx)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.animation.core.lint)
    implementation(libs.firebase.firestore.ktx)
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation ("androidx.compose.material:material:1.4.3")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Firebase
    implementation(platform(libs.firebase.bom))

    // Firebase Auth
    implementation(libs.firebase.auth)

    // Firebase Firestore
    implementation(libs.firebase.firestore.ktx)

    // LiveData
    implementation ("androidx.compose.runtime:runtime-livedata:1.3.2")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation(libs.androidx.navigation.compose)

    //cliente http
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.json:json:20231013")
    implementation("io.coil-kt:coil-compose:2.4.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)



        implementation("androidx.compose.ui:ui:1.4.0")
        //implementation("androidx.compose.material3:material3:1.1.0")
        implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")
        implementation("androidx.navigation:navigation-compose:2.5.3") // Para navegación
        implementation("com.google.firebase:firebase-auth-ktx:22.1.0") // Para autenticación con Firebase



    //implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    //implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    //implementation("com.google.firebase:firebase-firestore-ktx:24.8.1")
    //implementation("com.google.firebase:firebase-auth-ktx:22.1.1")
    //implementation("androidx.compose.ui:ui:1.6.1")


    //implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Para convertir JSON a objetos Kotlin
    //implementation("com.google.code.gson:gson:2.10.1")


}