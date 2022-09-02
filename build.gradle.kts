plugins {
    id("org.beryx.jlink") version "2.25.0"
    kotlin("jvm") version "1.7.10"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainModule.set("com.salamanderlive.qencode")
    mainClass.set("com.salamanderlive.qencode.AppKt")
}

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
    implementation("com.google.zxing:javase:3.5.0")
}

