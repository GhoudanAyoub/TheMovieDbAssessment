import  com.gws.ussd.Deps

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

    implementation(project(":ui_core"))
    implementation(project(":local_models"))
    implementation(project(":networking"))
    implementation(project(":common"))

    implementation(Deps.Androidx.CoreKtx.coreKtx)
    implementation(Deps.Androidx.AppCompat.appCompat)
    implementation(Deps.Androidx.Core.coreSplashScreen)
    implementation(Deps.Google.Material.material)
    implementation(Deps.Androidx.ConstraintLayout.constraintLayout)
    implementation(Deps.Androidx.Navigation.navigationFragmentKtx)
    implementation(Deps.Androidx.Navigation.navigationUiKtx)
    implementation(Deps.Androidx.Lifecycle.liveDataKtx)

    androidTestImplementation(Deps.Androidx.Test.JUnit.jUnit)
    androidTestImplementation(Deps.Androidx.Test.JUnit.jUnitrunner)
    androidTestImplementation(Deps.Androidx.Test.JUnit.jUnitext)
    androidTestImplementation(Deps.Androidx.Test.JUnit.jUnitcore)
    androidTestImplementation(Deps.Androidx.Test.Espresso.espressoCore)
    androidTestImplementation(Deps.Androidx.Test.Espresso.espressoContrib)
    implementation(Deps.Androidx.Test.Espresso.espressoResource)

    implementation( "androidx.work:work-runtime-ktx:2.7.1")
    implementation(Deps.Hilt.hilt)
    kapt(Deps.Hilt.hilt_compiler)

    implementation(Deps.Timber.timber)
    implementation(Deps.Glide.glide)
    implementation(Deps.Androidx.libphonenumber.libphonenumber)
    implementation(Deps.Androidx.phoneApi.play_services_auth)
    implementation(Deps.Androidx.phoneApi.play_services_auth_api_phone)
    kapt(Deps.Glide.compiler)

    implementation( "com.github.romellfudi.VoIpUSSD:kotlin-ussd-library:1.4.a")
}
