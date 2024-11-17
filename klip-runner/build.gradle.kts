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
