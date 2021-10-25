plugins {
    id("org.beryx.jlink") version "2.24.4"
    kotlin("jvm") version "1.5.31"
}

repositories {
    mavenCentral()
}

// Note that there are no toolchains defined here. Specifying a toolchain
// doesn't seme to work well on macos - at least not when you have an Apple
// with their M1 chip. Specifying the toolchain may not be necessary though,
// and I think we'll be OK with just using the Java version in which the build
// script is executing.

application {
    mainModule.set("com.salamanderlive.qencode")
    mainClass.set("com.salamanderlive.qencode.AppKt")
}

println(System.getProperty("os.name"))

jlink {
    jpackage {
        skipInstaller = true

        // FIXME This is not the most robust way to check the OS
        // Unfortunately, Gradle doesn't offer a built-in way to determine the
        // OS on which the project is being built. The internet offers various
        // solutions, but none of them are much better than this. There is one
        // solution which uses part of Gradle's public Ant API, which may be
        // worth exploring in the future.
        if (System.getProperty("os.name").startsWith("Windows")) {
            imageOptions.add("--win-console")
        }
    }
}

version = "1.0.0"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.google.zxing:javase:3.4.1")
}

