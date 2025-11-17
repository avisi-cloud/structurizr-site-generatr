import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.serialization") version "2.2.21"
    application
}

description = "Structurizr Site Generatr"
group = "cloud.avisi"

version = project.properties["projectVersion"] ?: "0.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.6")

    implementation("org.eclipse.jgit:org.eclipse.jgit:7.4.0.202509020913-r")

    implementation("com.structurizr:structurizr-core:4.1.0")
    implementation("com.structurizr:structurizr-dsl:4.1.0")
    implementation("com.structurizr:structurizr-export:4.1.0")

    implementation("net.sourceforge.plantuml:plantuml:1.2025.10")

    implementation("com.vladsch.flexmark:flexmark-all:0.64.8")
    implementation("org.asciidoctor:asciidoctorj:3.0.1")
    implementation("org.jsoup:jsoup:1.21.2")

    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    implementation("org.eclipse.jetty:jetty-server:12.1.3")
    implementation("org.eclipse.jetty.websocket:jetty-websocket-jetty-server:12.1.3")

    runtimeOnly("org.slf4j:slf4j-simple:2.0.17")

    // Support for Structurizr scripting languages
    runtimeOnly("org.jetbrains.kotlin:kotlin-scripting-jsr223:2.2.21")
    runtimeOnly("org.codehaus.groovy:groovy-jsr223:3.0.25")
    runtimeOnly("org.jruby:jruby-core:9.4.14.0")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.28.1")

    testImplementation("com.microsoft.playwright:playwright:1.55.0")
}

application {
    mainClass.set("nl.avisi.structurizr.site.generatr.AppKt")
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

java {
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    processResources {
        from("package.json")
    }

    test {
        useJUnitPlatform()
    }

    jar {
        manifest {
            attributes["Implementation-Title"] = project.description
            attributes["Implementation-Version"] = project.version
        }
    }

    startScripts {
        // This is a workaround for an issue with the generated .bat file,
        // reported in https://github.com/avisi-cloud/structurizr-site-generatr/issues/463.
        // Instead of listing each and every .jar file in the lib directory, we just use a
        // wildcard to include everything in the lib directory in the classpath.
        classpath = files("lib/*")
    }

    withType<Tar> {
        archiveExtension.set("tar.gz")
        compression = Compression.GZIP
    }
}
