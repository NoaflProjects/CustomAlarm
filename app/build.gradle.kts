plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ktfmt)
    alias(libs.plugins.sonar)
    id("jacoco")
}

android {
    namespace = "com.android.customalarm"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.android.customalarm"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isMinifyEnabled = false
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
            applicationIdSuffix = ".debug" // Add application ID suffix for debug builds
            versionNameSuffix = "-DEBUG" // Add version name suffix for debug builds
        }
    }

    testCoverage {
        jacocoVersion = "0.8.8"
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
}


// ---------------- SonarCloud ----------------
sonar {
    properties {
        property("sonar.projectKey", "NoaflProjects_CustomAlarm")
        property("sonar.organization", "noaflprojects")
        property("sonar.host.url", "https://sonarcloud.io")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            layout.buildDirectory.get().asFile.resolve("reports/jacoco/jacocoTestReport/jacocoTestReport.xml").absolutePath
        )
    }
}

dependencies {

    // ------------------- Implementation (main/production code) -------------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // ------------------- Unit tests (src/test) -------------------
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test) // Coroutine testing utilities

    // ------------------- Android instrumented tests (src/androidTest) -------------------
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // ------------------- Debug-only dependencies -------------------
    debugImplementation(libs.androidx.compose.ui.tooling)          // UI preview & inspection
    debugImplementation(libs.androidx.compose.ui.test.manifest)   // Compose test manifest
}

// ---------------- Jacoco ----------------
tasks.register<JacocoReport>("jacocoTestReport") {
    mustRunAfter("testDebugUnitTest", "connectedDebugAndroidTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*"
    )

    val debugTree = fileTree(layout.buildDirectory.get().asFile.resolve("tmp/kotlin-classes/debug")) {
        exclude(fileFilter)
    }

    val mainSrc = layout.projectDirectory.dir("src/main/java").asFile

    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(debugTree))
    executionData.setFrom(
        fileTree(layout.buildDirectory.get().asFile) {
            include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
            include("outputs/code_coverage/debugAndroidTest/connected/*/coverage.ec")
        }
    )
}