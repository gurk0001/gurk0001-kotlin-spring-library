plugins {
    id("java-library")
    id("maven-publish")
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
version = "0.0.0-SNAPSHOT"

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

publishing {
    repositories {
        maven {
            val ARCHIVA_URL = System.getenv("ARCHIVA_URL")
            url = uri("http://${ARCHIVA_URL}/repository/snapshots")
            isAllowInsecureProtocol = true
            credentials {
                username = System.getenv("ARCHIVA_USR")
                password = System.getenv("ARCHIVA_PSW")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

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