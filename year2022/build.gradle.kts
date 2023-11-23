plugins {
    kotlin("jvm") version "1.9.20"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}
