plugins {
    `kotlin-dsl`
}
repositories {
    gradlePluginPortal()
    mavenCentral()
}
kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(22)
        vendor = JvmVendorSpec.ADOPTIUM
    }
}
dependencies {
    implementation(libs.kotlin.gradle)
}
