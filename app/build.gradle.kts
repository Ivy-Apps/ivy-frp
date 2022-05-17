import com.ivy.frp.buildsrc.Project
import com.ivy.frp.buildsrc.appModuleDependencies


plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Project.compileSdkVersion

    defaultConfig {
        applicationId = Project.applicationId
        minSdk = Project.minSdk
        targetSdk = Project.targetSdk
        versionCode = Project.versionCode
        versionName = Project.versionName

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }


    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            isDefault = true

            signingConfig = signingConfigs.getByName("debug")

            applicationIdSuffix = ".debug"
            resValue("string", "app_name", "Ivy FRP Sample")
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
        kotlinCompilerExtensionVersion = com.ivy.frp.buildsrc.GlobalVersions.compose
    }

    lint {
//        isCheckReleaseBuilds = true
//        isAbortOnError = false
    }

    packagingOptions {
        //Exclude this files so Jetpack Compose UI tests can build
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
        //-------------------------------------------------------
    }

    testOptions {
        unitTests.all {
            //Required by Kotest
            it.useJUnitPlatform()
        }
    }
}

dependencies {
    appModuleDependencies()
}
