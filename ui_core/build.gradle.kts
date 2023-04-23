import ghoudan.ayoub.movieBest.Deps

plugins {
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "ghoudan.ayoub.movieBest.ui_core"
}

dependencies {
    implementation(Deps.Androidx.CoreKtx.coreKtx)
    implementation(Deps.Androidx.AppCompat.appCompat)
    implementation(Deps.Google.Material.material)

    implementation(Deps.Hilt.hilt)
    kapt(Deps.Hilt.hilt_compiler)

    implementation(Deps.Networking.logging_interceptor)
    implementation(Deps.Networking.ok2Curl)

    implementation(Deps.Serialization.retrofitKotlinxSerializationConverter)
    implementation(Deps.Serialization.kotlinxSerialization)

    implementation(Deps.Coroutines.coroutines)

    implementation(Deps.Timber.timber)

}