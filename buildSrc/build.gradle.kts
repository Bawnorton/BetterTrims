plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    maven("https://maven.kikugie.dev/releases")
}

dependencies {
    implementation("net.fabricmc:fabric-loader:0.15.11")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("dev.kikugie:stonecutter:0.4")
}