import  ghoudan.ayoub.movieBest.Deps

plugins {
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }
    }
}

dependencies {

    implementation(project(":local_models"))
    implementation(project(":networking"))

    implementation(Deps.Androidx.CoreKtx.coreKtx)
    implementation(Deps.Androidx.AppCompat.appCompat)
    implementation(Deps.Androidx.Core.coreSplashScreen)
    implementation(Deps.Google.Material.material)
    implementation(Deps.Androidx.ConstraintLayout.constraintLayout)
    implementation(Deps.Androidx.Navigation.navigationFragmentKtx)
    implementation(Deps.Androidx.Navigation.navigationUiKtx)
    implementation(Deps.Androidx.Lifecycle.liveDataKtx)

    testImplementation(Deps.Test.JUnit.junit)
    androidTestImplementation(Deps.Androidx.Test.JUnit.jUnit)
    androidTestImplementation(Deps.Androidx.Test.Espresso.espressoCore)

    implementation(Deps.Hilt.hilt)
    kapt(Deps.Hilt.hilt_compiler)
}
