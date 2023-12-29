plugins {
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.serialization") version "1.9.22"
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

    implementation("org.eclipse.jgit:org.eclipse.jgit:6.8.0.202311291450-r")

    implementation("com.structurizr:structurizr-core:1.29.0")
    implementation("com.structurizr:structurizr-dsl:1.35.0")
    implementation("com.structurizr:structurizr-export:1.18.0")

    implementation("net.sourceforge.plantuml:plantuml:1.2023.13")

    implementation("com.vladsch.flexmark:flexmark-all:0.64.8")
    implementation("org.asciidoctor:asciidoctorj:2.5.11")
    implementation("org.jsoup:jsoup:1.17.2")

    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    implementation("org.eclipse.jetty:jetty-server:12.0.5")
    implementation("org.eclipse.jetty.websocket:jetty-websocket-jetty-server:12.0.5")

    runtimeOnly("org.slf4j:slf4j-simple:2.0.10")
    runtimeOnly("org.jetbrains.kotlin:kotlin-scripting-jsr223:1.9.22")
    runtimeOnly("org.codehaus.groovy:groovy-jsr223:3.0.20")
    runtimeOnly("org.jruby:jruby-core:9.4.5.0")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.28.0")
}

application {
    mainClass.set("nl.avisi.structurizr.site.generatr.AppKt")
}

kotlin {
    jvmToolchain(19)
}

tasks {
    copy {
        from("package.json")
        into("src/main/resources")
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
