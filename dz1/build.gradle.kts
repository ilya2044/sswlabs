plugins {
    kotlin("jvm") version "1.8.10" // замените на нужную версию Kotlin
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    // JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testRuntimeOnly ("org.junit.platform:junit-platform-launcher:1.8.2")
}

tasks.test {
    useJUnitPlatform() // Указывает Gradle использовать платформу JUnit
}

application {
    mainClass.set("src/main/java/org/example/FileFilter.java")  // Замените на путь к вашему основному классу
}