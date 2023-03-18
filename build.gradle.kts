plugins {
    kotlin("jvm") version "1.8.10"
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
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")

    implementation("org.eclipse.jgit:org.eclipse.jgit:6.5.0.202303070854-r")

    implementation("com.structurizr:structurizr-core:1.23.1")
    implementation("com.structurizr:structurizr-dsl:1.29.0")
    implementation("com.structurizr:structurizr-export:1.13.0")

    implementation("net.sourceforge.plantuml:plantuml:1.2023.4")

    implementation("com.vladsch.flexmark:flexmark-all:0.64.0")
    implementation("org.jsoup:jsoup:1.15.4")

    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.8.0")

    implementation("org.eclipse.jetty:jetty-server:11.0.14")
    implementation("org.eclipse.jetty:jetty-servlet:11.0.14")
    implementation("org.eclipse.jetty.websocket:websocket-jetty-server:11.0.14")

    runtimeOnly("org.slf4j:slf4j-simple:2.0.7")
    runtimeOnly("org.jetbrains.kotlin:kotlin-scripting-jsr223:1.8.10")
    runtimeOnly("org.codehaus.groovy:groovy-jsr223:3.0.16")
    runtimeOnly("org.jruby:jruby-core:9.4.2.0")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
}

application {
    mainClass.set("nl.avisi.structurizr.site.generatr.AppKt")
}

kotlin {
    jvmToolchain(19)
}

tasks {
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
