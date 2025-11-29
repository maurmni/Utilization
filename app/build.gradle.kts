plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.utilization"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.utilization"
        minSdk = 25
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.room.runtime)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    annotationProcessor(libs.room.compiler)
    implementation(libs.room.runtime.android)
    testImplementation(libs.junit)
    testImplementation("junit:junit:4.13.2")
    implementation("androidx.room:room-runtime:2.8.4")

    annotationProcessor("androidx.room:room-compiler:2.8.4")
    implementation("androidx.room:room-common:2.8.4")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

