@file:Suppress("UnstableApiUsage")

plugins {
    `maven-publish`
    java
    kotlin("jvm") version "1.9.22"
    id("dev.architectury.loom") version "1.7-SNAPSHOT"
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("me.modmuss50.mod-publish-plugin") version "0.5.+"
    id("dev.kikugie.j52j") version "1.0.2"
}

val mod = ModData(project)
val loader = LoaderData(project, loom.platform.get().name.lowercase())
val mcVersion = MinecraftVersionData(stonecutter)

version = "${mod.version}-$loader+$mcVersion"
group = mod.group
base.archivesName.set(mod.name)

StonecutterSwapper(stonecutter)
    .register("trim_getter", "1.21",
        "ArmorTrim.getTrim(world.getRegistryManager(),stack).orElse(null);",
        "stack.get(DataComponentTypes.TRIM);")
    .register("attribute_shadow", "1.21",
        "@Shadow public abstract double getAttributeValue(EntityAttribute attribute);",
        "@Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);")
    .apply(mcVersion.toString())

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://maven.neoforged.net/releases/")
    maven("https://maven.bawnorton.com/releases/")
    maven("https://maven.shedaniel.me")
    maven("https://maven.su5ed.dev/releases")
    maven("https://jitpack.io")
    maven("https://api.modrinth.com/maven")
}

dependencies {
    minecraft("com.mojang:minecraft:$mcVersion")

    modCompileOnly("maven.modrinth:sodium-dynamic-lights:${property("sodium_dynamic_lights")}")
    modImplementation("dev.isxander:yet-another-config-lib:${property("yacl")}-$loader")
    annotationProcessor(modImplementation("com.bawnorton.configurable:configurable-$loader-yarn:${property("configurable")}+$mcVersion") { isTransitive = false })
}

loom {
    accessWidenerPath.set(rootProject.file("src/main/resources/$mcVersion.accesswidener"))

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
        options.release = mcVersion.javaVersion()
    }

    processResources {
        val modMetadata = mapOf(
            "description" to mod.description,
            "version" to mod.version,
            "minecraft_dependency" to mod.minecraftDependency,
            "minecraft_version" to mcVersion.toString(),
            "loader_version" to loader.getVersion()
        )

        inputs.properties(modMetadata)
        filesMatching("fabric.mod.json") { expand(modMetadata) }
        filesMatching("META-INF/neoforge.mods.toml") { expand(modMetadata) }
    }

    processResources {
        filesMatching("bettertrims.mixins.json5") {
            filter {
                it.replace("\${refmap}", "${mod.name}-$mcVersion-$loader-refmap.json")
            }
        }
    }
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.toVersion(mcVersion.javaVersion())
    targetCompatibility = JavaVersion.toVersion(mcVersion.javaVersion())
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
            createRunConfiguration = false
        }
    }

    tasks {
        register<Copy>("copyDatagen") {
            from("src/main/generated")
            into("${layout.buildDirectory.get()}/resources/main")
            dependsOn("runDatagen")
        }

        jar {
            dependsOn("copyDatagen")
        }

        withType<AbstractCopyTask> {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
    }

    sourceSets {
        main {
            resources {
                srcDir("src/main/generated")
            }
        }
    }

    dependencies {
        mappings("net.fabricmc:yarn:$mcVersion+build.${property("yarn_build")}:v2")
        modImplementation("net.fabricmc:fabric-loader:${loader.getVersion()}")

        modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api")}+$mcVersion")

        include(implementation(annotationProcessor("io.github.llamalad7:mixinextras-fabric:0.4.1")!!)!!)

        if (mcVersion.lessThan("1.21")) {
//            modRuntimeOnly("maven.modrinth:allthetrims:${property("allthetrims")}")

            modCompileOnly("maven.modrinth:lambdynamiclights:2.3.2+1.20.1")
        } else {
            modRuntimeOnly("com.bawnorton.allthetrims:allthetrims-fabric:${property("allthetrims")}")

            modCompileOnly(fileTree("libs") {
                include("*.jar")
            })
        }
    }
}

if (loader.isNeoForge) {
    dependencies {
        mappings(loom.layered {
            mappings("net.fabricmc:yarn:$mcVersion+build.${property("yarn_build")}:v2")
            mappings("dev.architectury:yarn-mappings-patch-neoforge:1.21+build.4")
            mappings(file("mappings/fix.tiny"))
        })
        neoForge("net.neoforged:neoforge:${loader.getVersion()}")

        modImplementation("org.sinytra.forgified-fabric-api:forgified-fabric-api:${property("fabric_api")}+${property("forgified_fabric_api")}+$mcVersion")

        forgeRuntimeLibrary(runtimeOnly("org.quiltmc.parsers:json:${property("quilt_parsers")}")!!)
        forgeRuntimeLibrary(runtimeOnly("org.quiltmc.parsers:gson:${property("quilt_parsers")}")!!)

        compileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:0.4.1")!!)
        implementation(include("io.github.llamalad7:mixinextras-neoforge:0.4.1")!!)
    }

    tasks {
        remapJar {
            atAccessWideners.add("$mcVersion.accesswidener")
        }

        register<Copy>("copyDatagen") {
            from(rootProject.file("versions/${mcVersion}-fabric/src/main/generated"))
            into("${layout.buildDirectory.get()}/resources/main")
        }

        jar {
            dependsOn("copyDatagen")
        }

        withType<AbstractCopyTask> {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
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
            version = "${mod.version}+$mcVersion"

            from(components["java"])
        }
    }
}

publishMods {
    file = tasks.remapJar.get().archiveFile
    val tag = "$loader-${mod.version}+$mcVersion"
    val branch = "main"
    changelog = "[Changelog](https://github.com/Bawnorton/${mod.name}/blob/$branch/CHANGELOG.md)"
    displayName = "${mod.name} ${loader.toString().replaceFirstChar { it.uppercase() }} ${mod.version} for $mcVersion"
    type = STABLE
    modLoaders.add(loader.toString())

    github {
        accessToken = providers.gradleProperty("GITHUB_TOKEN")
        repository = "Bawnorton/${mod.name}"
        commitish = branch
        tagName = tag
    }

    modrinth {
        accessToken = providers.gradleProperty("MODRINTH_TOKEN")
        projectId = mod.modrinthProjId
        minecraftVersions.addAll(mod.supportedVersions.split(", "))
    }

    curseforge {
        accessToken = providers.gradleProperty("CURSEFORGE_TOKEN")
        projectId = mod.curseforgeProjId
        minecraftVersions.addAll(mod.supportedVersions.split(", "))
    }
}
