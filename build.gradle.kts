plugins {
    kotlin("jvm") version "1.4.21"
}

group = "pal.fiction"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))


    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
    testRuntimeOnly( "org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
