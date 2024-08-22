plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.qatapultt.tryscannerndk"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.qatapultt.tryscannerndk"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
    packaging {
        resources {
            jniLibs.pickFirsts.add("lib/x86/libc++_shared.so")
            jniLibs.pickFirsts.add("lib/x86_64/libc++_shared.so")
            jniLibs.pickFirsts.add("lib/armeabi-v7a/libc++_shared.so")
            jniLibs.pickFirsts.add("lib/arm64-v8a/libc++_shared.so")
            jniLibs.pickFirsts.add("lib/x86/libopencv_java4.so")
            jniLibs.pickFirsts.add("lib/x86_64/libopencv_java4.so")
            jniLibs.pickFirsts.add("lib/armeabi-v7a/libopencv_java4.so")
            jniLibs.pickFirsts.add("lib/arm64-v8a/libopencv_java4.so")
        }
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(files("./libs/scanner-debug.aar"))
    implementation(files("./libs/opencv-prod-release.aar"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}