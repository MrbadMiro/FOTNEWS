plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Firebase BoM using version catalog
    implementation(platform(libs.firebase.bom))

    // Firebase services (versions managed by BoM)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
