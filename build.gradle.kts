plugins {
    java
    `java-library`
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("edu.sc.seis.launch4j") version "2.5.3"
}

group = "fr.mardiH"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}


dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")

    implementation("org.apache.logging.log4j:log4j-core:2.17.1")
    implementation("org.apache.logging.log4j:log4j-api:2.17.1")

    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.2")
    implementation("org.springframework:spring-core:5.3.16")

    implementation("uk.co.electronstudio.sdl2gdx:sdl2gdx:1.0.4")
    implementation("com.github.WilliamAHartman:Jamepad:1.4.0")

    implementation("commons-io:commons-io:2.11.0")

}

application {
    mainClass.set("fr.mardiH.Main")
    mainClassName = "fr.mardiH.Main"
}

tasks.jar {
    manifest {
        attributes(
            "Multi-Release" to true,
            "Main-Class" to "fr.mardiH.Main"
        )
    }
}

launch4j {
    mainClassName = "fr.mardiH.Main"
    icon = "${projectDir}/icons/logo.ico"
    bundledJrePath = "%JAVA_HOME%"
    jreMinVersion = "11"
    jarTask = project.tasks.shadowJar.get()
    copyright = "Tiny Tanks"
    windowTitle = "Tiny Tanks"
}



tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    compileTestJava {
        options.encoding = "UTF-8"
    }

    shadowJar {
        archiveClassifier.set("")
    }
}


tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

