import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.3.2.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
	kotlin("plugin.jpa") version "1.3.72"
	id(	"org.flywaydb.flyway") version "6.5.1"
}

group = "com.kg"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	jcenter()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")}
	implementation("org.apache.logging.log4j:log4j-api:2.13.3")
	implementation("org.apache.logging.log4j:log4j-core:2.13.3")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.+")
	implementation ("com.google.code.gson:gson:2.8.5")
	//implementation ("com.squareup.retrofit2:retrofit:2.4.0")
	//implementation ("com.squareup.retrofit2:converter-gson:2.4.0")
	implementation ("com.github.kittinunf.fuel:fuel:2.2.3")
	implementation ("org.apache.commons:commons-lang3:3.11")
	implementation ("org.springframework.security:spring-security-core")
	implementation ("org.mockito:mockito-core:3.6.28")
	testImplementation ("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

flyway {
	url = "jdbc:postgresql://localhost:5432/pets"
	user = "postgres"
	password = "goosegoose"
}
