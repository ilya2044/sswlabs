plugins {
    id("java")
    kotlin("jvm") version "1.8.10"
    application
}

application {
    mainClass.set("src/main/java/org/example/Main.java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

