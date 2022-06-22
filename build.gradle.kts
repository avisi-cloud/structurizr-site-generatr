import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.0"
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.4")

    implementation("org.eclipse.jgit:org.eclipse.jgit:6.2.0.202206071550-r")

    implementation("com.structurizr:structurizr-core:1.12.2")
    implementation("com.structurizr:structurizr-dsl:1.19.1")
    implementation("com.structurizr:structurizr-export:1.5.0")

    @Suppress("GradlePackageUpdate")
    implementation("net.sourceforge.plantuml:plantuml:1.2022.6")

    implementation("com.vladsch.flexmark:flexmark-all:0.64.0")

    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.5")

    implementation("org.eclipse.jetty:jetty-server:11.0.10")

    runtimeOnly("org.slf4j:slf4j-simple:2.0.0-alpha7")
    runtimeOnly("org.jetbrains.kotlin:kotlin-scripting-jsr223:1.7.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClass.set("nl.avisi.structurizr.site.generatr.AppKt")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
        }
    }

    withType<Jar> {
        manifest {
            attributes["Implementation-Title"] = project.description
            attributes["Implementation-Version"] = project.version
        }
    }
}
