@file:Suppress("UnstableApiUsage")

plugins {
    `maven-publish`
    kotlin("jvm") version "1.9.22"
    id("dev.architectury.loom") version "1.7-SNAPSHOT"
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("me.modmuss50.mod-publish-plugin") version "0.5.+"
}

class CompatMixins {
    private var common : List<String> = listOf()
    private var fabric : List<String> = listOf()
    private var neoforge : List<String> = listOf()

    fun getMixins() : Map<String, String> {
        val mixins = common + if(loader.isFabric) fabric else neoforge
        return mapOf(
            "compat_mixins" to "[\n${mixins.joinToString(",\n") { "\"$it\"" }}\n]"
        )
    }
}

val mod = ModData(project)
val loader = LoaderData(project, loom.platform.get().name.lowercase())
val minecraftVersion = MinecraftVersionData(stonecutter)
val awName = "${mod.id}.accesswidener"

version = "${mod.version}-$loader+$minecraftVersion"
group = mod.group
base.archivesName.set(mod.name)

repositories {
    mavenCentral()
    maven("https://maven.neoforged.net/releases/")
    maven("https://maven.bawnorton.com/releases/")
    maven("https://maven.shedaniel.me")
    maven("https://maven.su5ed.dev/releases")
    maven("https://jitpack.io")
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")

    modImplementation("com.bawnorton.allthetrims:allthetrims-$loader:${property("allthetrims")}") { isTransitive = false }
}

loom {
    accessWidenerPath.set(rootProject.file("src/main/resources/$awName"))

    runConfigs.all {
        ideConfigGenerated(true)
        runDir = "../../run"
    }

    runConfigs["client"].apply {
        vmArgs("-Dmixin.debug.export=true")
        programArgs("--username=Bawnorton")
    }
}

tasks {
    withType<JavaCompile> {
        options.release = 21
    }

    processResources {
        val compatMixins = CompatMixins().getMixins()
        inputs.properties(compatMixins)
        filesMatching("${mod.id}-compat.mixins.json") { expand(compatMixins) }
    }
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

val buildAndCollect = tasks.register<Copy>("buildAndCollect") {
    group = "build"
    from(tasks.remapJar.get().archiveFile)
    into(rootProject.layout.buildDirectory.file("libs/${mod.version}"))
    dependsOn("build")
}

if (stonecutter.current.isActive) {
    rootProject.tasks.register("buildActive") {
        group = "project"
        dependsOn(buildAndCollect)
    }
}

if(loader.isFabric) {
    fabricApi {
        configureDataGeneration {
            outputDirectory = rootProject.rootDir.resolve("src/main/generated")
        }
    }

    dependencies {
        mappings("net.fabricmc:yarn:$minecraftVersion+build.${property("yarn_build")}:v2")
        modImplementation("net.fabricmc:fabric-loader:${loader.getVersion()}")

        modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api")}+$minecraftVersion")

        include(implementation(annotationProcessor("io.github.llamalad7:mixinextras-fabric:0.5.0-beta.2")!!)!!)

        modCompileOnly(fileTree("libs") {
            include("*.jar")
        }).stripAw(project)
    }

    tasks {
        processResources {
            val modMetadata = mapOf(
                "mod_id" to mod.id,
                "mod_name" to mod.name,
                "description" to mod.description,
                "version" to mod.version,
                "minecraft_dependency" to mod.minecraftDependency
            )

            inputs.properties(modMetadata)
            filesMatching("fabric.mod.json") { expand(modMetadata) }
        }
    }
}

if (loader.isNeoForge) {
    sourceSets {
        main {
            resources.srcDir(rootProject.rootDir.resolve("src/main/generated"))
        }
    }

    dependencies {
        mappings(loom.layered {
            mappings("net.fabricmc:yarn:$minecraftVersion+build.${property("yarn_build")}:v2")
            mappings("dev.architectury:yarn-mappings-patch-neoforge:1.21+build.4")
            mappings(file("mappings/fix.tiny"))
        })
        neoForge("net.neoforged:neoforge:${loader.getVersion()}")
        modImplementation("org.sinytra.forgified-fabric-api:forgified-fabric-api:${property("fabric_api")}+${property("forgified_fabric_api")}+$minecraftVersion")

        compileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.0-beta.2")!!)
        implementation(include("io.github.llamalad7:mixinextras-neoforge:0.5.0-beta.2")!!)

    }

    tasks {
        processResources {
            val modMetadata = mapOf(
                "mod_id" to mod.id,
                "mod_name" to mod.name,
                "description" to mod.description,
                "version" to mod.version,
                "minecraft_dependency" to mod.minecraftDependency,
                "loader_version" to loader.getVersion()
            )

            inputs.properties(modMetadata)
            filesMatching("META-INF/neoforge.mods.toml") { expand(modMetadata) }
        }

        remapJar {
            atAccessWideners.add(awName)
        }
    }
}

extensions.configure<PublishingExtension> {
    repositories {
        maven {
            name = "bawnorton"
            url = uri("https://maven.bawnorton.com/releases")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "${mod.group}.${mod.id}"
            artifactId = "${mod.id}-$loader"
            version = "${mod.version}+$minecraftVersion"

            from(components["java"])
        }
    }
}

publishMods {
    file = tasks.remapJar.get().archiveFile
    val tag = "$loader-${mod.version}+$minecraftVersion"
    val branch = "main"
    changelog = "[Changelog](https://github.com/Bawnorton/${mod.name}/blob/$branch/CHANGELOG.md)"
    displayName = "${mod.name} ${loader.toString().replaceFirstChar { it.uppercase() }} ${mod.version} for $minecraftVersion"
    type = STABLE
    modLoaders.add(loader.toString())

    github {
        accessToken = providers.gradleProperty("GITHUB_TOKEN")
        repository = "Bawnorton/${mod.name}"
        commitish = branch
        changelog = getRootProject().file("CHANGELOG.md").readLines().joinToString("\n")
        tagName = tag
    }

    modrinth {
        accessToken = providers.gradleProperty("MODRINTH_TOKEN")
        projectId = mod.modrinthProjId
        minecraftVersions.addAll(mod.supportedVersions)
    }

    curseforge {
        accessToken = providers.gradleProperty("CURSEFORGE_TOKEN")
        projectId = mod.curseforgeProjId
        changelog = """
            <markdown>
        """
        minecraftVersions.addAll(mod.supportedVersions)
    }
}
