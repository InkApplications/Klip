plugins {
    application
    kotlin("jvm")
}
kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(22)
        vendor = JvmVendorSpec.ADOPTIUM
    }
}
application {
    applicationName = "klip"
    mainClass.set("com.inkapplications.klip.runner.MainKt")
}

dependencies {
    implementation(libs.clikt)
    implementation(libs.kotlin.scripting.common)
    implementation(libs.kotlin.scripting.jvm.core)
    implementation(libs.kotlin.scripting.jvm.host)
    implementation(libs.kimchi.core)
    implementation(libs.mordant)
}
