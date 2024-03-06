plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"
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

    implementation("org.eclipse.jgit:org.eclipse.jgit:6.9.0.202403050737-r")

    implementation("com.structurizr:structurizr-core:2.1.1")
    implementation("com.structurizr:structurizr-dsl:2.1.1")
    implementation("com.structurizr:structurizr-export:2.1.1")

    implementation("net.sourceforge.plantuml:plantuml:1.2024.3")

    implementation("com.vladsch.flexmark:flexmark-all:0.64.8")
    implementation("org.asciidoctor:asciidoctorj:2.5.12")
    implementation("org.jsoup:jsoup:1.17.2")

    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    implementation("org.eclipse.jetty:jetty-server:12.0.7")
    implementation("org.eclipse.jetty.websocket:jetty-websocket-jetty-server:12.0.7")

    runtimeOnly("org.slf4j:slf4j-simple:2.0.12")
    runtimeOnly("org.jetbrains.kotlin:kotlin-scripting-jsr223:1.9.23")
    runtimeOnly("org.codehaus.groovy:groovy-jsr223:3.0.21")
    runtimeOnly("org.jruby:jruby-core:9.4.6.0")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.28.0")
}

application {
    mainClass.set("nl.avisi.structurizr.site.generatr.AppKt")
}

kotlin {
    jvmToolchain(21)
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

    withType<Tar> {
        archiveExtension.set("tar.gz")
        compression = Compression.GZIP
    }
}
