import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java`
    `java-library`
    `maven-publish`
   // signing
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.spring") version "1.6.0"
}

val springVersion = "2.6.0"
val kotlinVersion = "1.6.0"
val lombokVersion = "1.18.22"
val reactorKotlinVersion = "1.1.5"
val kotlinxCoroutinesReactor = "1.5.2"
group = "com.gurk0001"
version = "0.0.5"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${springVersion}")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:${reactorKotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${kotlinxCoroutinesReactor}")
    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")

    implementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}



publishing {
    repositories {
        maven {

            /* change URLs to point to your repos */
            val ARCHIVA_URL = System.getenv("ARCHIVA_URL")
            val releasesRepoUrl = uri("http://$ARCHIVA_URL/repository/releases")
            val snapshotsRepoUrl = uri("http://$ARCHIVA_URL/repository/snapshots")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            isAllowInsecureProtocol = true
            credentials {
                username = System.getenv("ARCHIVA_USR")
                password = System.getenv("ARCHIVA_PSW")
            }
        }
    }

    publications {
        create<MavenPublication>("mavenJava") {
            //groupId = project.group.toString()
            //version = project.version.toString()
            afterEvaluate {
                artifactId = tasks.jar.get().archiveBaseName.get()
            }

            //artifact bootJar
            from(components["java"])

            pom {
                name.set(project.name)
                description.set("This is to showcase that we can publish java project into archiva repo")
                url.set("www.popye.in")
                developers {
                    developer {
                        id.set("gurk0001")
                        name.set("Guru")
                        email.set("gurk001@test.com")
                    }
                }
            }
        }
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

//signing {
//    sign(publishing.publications["mavenJava"])
//}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}