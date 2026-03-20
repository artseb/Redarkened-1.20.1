pluginManagement {
	repositories {
		maven {
			name = "Fabric"
			url = uri("https://maven.fabricmc.net/")
		}
		mavenCentral()
		gradlePluginPortal()
	}

	plugins {
		id("net.fabricmc.fabric-loom-remap") version providers.gradleProperty("loom_version")
	}
}

includeBuild("../artlib-1.20.1") {
	dependencySubstitution {
		substitute(module("github.artseb:artlib")).using(project(":"))
	}
}
