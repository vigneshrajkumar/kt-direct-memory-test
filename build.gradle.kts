import org.gradle.jvm.tasks.Jar

plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "org.rajkumarv"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {

    // https://mvnrepository.com/artifact/io.projectreactor/reactor-core
    implementation("io.projectreactor:reactor-core:3.5.11")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}


val fatJar = task("fatJar", type = Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Main-Class"] = "MainKt" // fully qualified class name of default main class
    }

    from(configurations.compileClasspath.get().map { if (it.isDirectory()) it else zipTree(it) })
    with(tasks["jar"] as CopySpec)
}

tasks {
    "build" {
        dependsOn(fatJar)
    }
}