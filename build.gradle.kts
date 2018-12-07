import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    application
    kotlin("jvm") version "1.3.11"
}

version = "0.1.0"

val mainPackageName = "com.salamanderlive.qencode"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
    mainClassName = "$mainPackageName.AppKt"
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.google.zxing:javase:3.3.3")
}

tasks.jar {
    manifest {
        attributes(mapOf(
            "Main-Class" to application.mainClassName,
            // Gradle's documentation says runtimeClasspath is an instance of Configuration, but it seems to actually
            // be an instance of NamedDomainObjectProvider<Configuration>. The latter is marked as incubating, so I'm
            // not sure what the mix-up is. Either way, the code below won't function without the get() to unwrap the
            // actual Configuration object we need.
            "Class-Path" to configurations.runtimeClasspath.get().joinToString(" ") { it.name }
        ))

        val vendor = "Salamander Technologies, LLC"
        attributes(
            mapOf(
                "Sealed" to true,
                "Specification-Title" to project.name,
                "Specification-Version" to project.version,
                "Specification-Vendor" to vendor,
                "Implementation-Title" to mainPackageName,
                "Implementation-Version" to project.version,
                "Implementation-Vendor" to vendor
            ),
            "${mainPackageName.replace(".", "/")}/"
        )
    }
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
