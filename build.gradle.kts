plugins {
    java
    application
}

version = "0.1.0"

val mainPackageName = "org.bitbucket.mlmoses.qencode"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
    mainClassName = "$mainPackageName.Main"
}

repositories {
    jcenter()
}

dependencies {
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

        val vendor = "Matthew L. Moses"
        val sectionName = "${mainPackageName.replace(".", "/")}/"
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
            sectionName
        )
    }
}
