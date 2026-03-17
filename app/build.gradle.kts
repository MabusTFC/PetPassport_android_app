import java.util.Properties


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.petpassport_android_app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.petpassport_android_app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        val propertiesFile = project.rootProject.file("local.properties")
        if (propertiesFile.exists()) {
            properties.load(propertiesFile.inputStream())
        }

        val baseUrl = properties.getProperty("BASE_URL") ?: "\"https://mypetpassport.ru:4443\""
        buildConfigField("String", "BASE_URL", baseUrl)
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.runtime)
    implementation(libs.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)


    var retrofit = "2.9.0"

    implementation("com.squareup.retrofit2:retrofit:${retrofit}")
    implementation("com.squareup.retrofit2:converter-gson:${retrofit}")


    var okhttp = "4.11.0"
    implementation("com.squareup.okhttp3:logging-interceptor:${okhttp}")


    var hilt = "2.55"

    implementation("com.google.dagger:hilt-android:${hilt}")
    kapt("com.google.dagger:hilt-compiler:${hilt}")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    kapt("androidx.hilt:hilt-compiler:1.2.0")

    val voyager = "1.1.0-beta03"
    implementation("cafe.adriel.voyager:voyager-navigator:$voyager")
    implementation("cafe.adriel.voyager:voyager-transitions:$voyager")
    implementation("cafe.adriel.voyager:voyager-screenmodel:${voyager}")
    implementation("cafe.adriel.voyager:voyager-hilt:$voyager")



    val coil3 = "3.0.4"
    implementation("io.coil-kt.coil3:coil-compose:${coil3}")
    implementation("io.coil-kt.coil3:coil-network-okhttp:${coil3}")

}