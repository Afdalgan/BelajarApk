plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.dicoding.picodiploma.loginwithanimation"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.dicoding.picodiploma.loginwithanimation"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
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
        viewBinding = true
    }
    packaging {
        resources {
            excludes.addAll(
                listOf(
                    "META-INF/DEPENDENCIES",
                    "META-INF/NOTICE",
                    "META-INF/LICENSE"
                )
            )
        }
    }
}

dependencies {

    // Core Dependencies
    implementation("androidx.activity:activity:1.9.3")
    implementation("androidx.activity:activity-ktx:1.9.3")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.fragment:fragment-ktx:1.8.5")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // Paging Dependencies
    implementation("androidx.paging:paging-runtime-ktx:3.3.5")
    implementation("androidx.paging:paging-common-ktx:3.3.5")

    // Network Libraries
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Glide for Image Loading
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Google Maps
    implementation("com.google.android.gms:play-services-maps:19.0.0")

    // Firebase
    implementation("com.google.firebase:firebase-appdistribution-gradle:5.0.0")

    // Unit Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("androidx.paging:paging-common-ktx:3.3.5")
    testImplementation("org.mockito:mockito-core:5.7.0")
    testImplementation("io.mockk:mockk:1.13.10")

    // Espresso & Android UI Testing
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // MockWebServer for API Testing
    testImplementation("com.squareup.okhttp3:mockwebserver:4.11.0")
}
