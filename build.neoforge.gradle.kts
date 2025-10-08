import bettertrims.utils.*
import dev.kikugie.fletching_table.annotation.MixinEnvironment

plugins {
    kotlin("jvm")
    `maven-publish`
    id("net.neoforged.moddev")
    id("bettertrims.common")
    id("me.modmuss50.mod-publish-plugin")
    id("com.google.devtools.ksp") version "2.2.0-2.0.2"
    id("dev.kikugie.fletching-table.neoforge") version "0.1.0-alpha.18"
}

repositories {
    mavenCentral()
    maven("https://maven.parchmentmc.org")
    maven("https://maven.bawnorton.com/releases/")
}

val minecraft: String by project
val loader: String by project
base.archivesName = "${mod("id")}-${mod("version")}+$minecraft-$loader"

dependencies {
    deps("configurable") {
        implementation(annotationProcessor("com.bawnorton.configurable:configurable-$loader:$it")!!)
    }
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

neoForge {
    version = deps("neoforge")

    validateAccessTransformers = true
    accessTransformers.from(rootProject.file("src/main/resources/$minecraft-accesstransformer.cfg"))

    mods {
        register("bettertrims") {
            sourceSet(sourceSets["main"])
        }
    }

    deps("parchment") {
        parchment {
            val (mc, version) = it.split(':')
            mappingsVersion = version
            minecraftVersion = mc
        }
    }

    runs {
        all {
            gameDirectory = rootProject.file("run")
        }

        register("client") {
            ideName = "NeoForge Client $minecraft"
            client()

            programArgument("--username=Bawnorton")
            programArgument("--uuid=17c06cab-bf05-4ade-a8d6-ed14aaf70545")
        }

        register("server") {
            ideName = "NeoForge Server $minecraft"
            server()
        }

        register("data") {
            ideName = "NeoForge Datagen $minecraft"
            if (stonecutter.eval(minecraft, ">1.21.1")) {
                serverData()
            } else {
                data()
            }
            programArguments.addAll(
                "--mod", "${mod("id")}",
                "--output", project.file("src/main/generated").toString()
            )
        }
    }

    afterEvaluate {
        runs.configureEach {
            applyMixinDebugSettings(::jvmArgument, ::systemProperty)
        }
    }
}

fletchingTable {
    mixins.register("main") {
        mixin("default", "bettertrims.mixins.json")
        mixin("client", "bettertrims.client.mixins.json") {
            environment = MixinEnvironment.Env.CLIENT
        }
    }
}

stonecutter {
    replacements {
        for (replacement in getReplacements()) {
            when(replacement.type) {
                ReplacementType.STRING -> string(eval(current.version, replacement.predicate)) {
                    replace(replacement.from, replacement.to)
                    if (replacement.id != null) id = replacement.id
                }
                ReplacementType.REGEX -> regex(eval(current.version, replacement.predicate)) {
                    replace(replacement.from, replacement.to)
                    reverse(replacement.to, replacement.from)
                    if (replacement.id != null) id = replacement.id
                }
            }
        }
    }

    for (swap in getSwaps()) {
        swaps[swap.id] = when {
            eval(current.version, swap.predicate) -> swap.to
            else -> swap.from
        }
    }
}

sourceSets.main {
    resources.srcDir(project.file("src/main/generated"))
    resources.exclude(".cache")
}

tasks {
    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(jar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }

    build {
        dependsOn("runData")
    }

    processResources {
        exclude("fabric.mod.json", "bettertrims.fabric.mixins.json")
        exclude { it.name.endsWith(".accesswidener") }
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
            groupId = "${mod("group")}.${mod("id")}"
            artifactId = "${mod("id")}-$loader"
            version = "${mod("version")}+$minecraft"

            from(components["java"])
        }
    }
}


publishMods {
    val mrToken = providers.gradleProperty("MODRINTH_TOKEN")
    val cfToken = providers.gradleProperty("CURSEFORGE_TOKEN")

    type = BETA
    file = tasks.jar.map { it.archiveFile.get() }
    additionalFiles.from(tasks.named<Jar>("sourcesJar").map { it.archiveFile.get() })

    displayName = "${mod("name")} Neoforge ${mod("version")} for $minecraft"
    version = mod("version")
    changelog = provider { rootProject.file("CHANGELOG.md").readText() }
    modLoaders.add(loader)

    modrinth {
        projectId = property("publishing.modrinth") as String
        accessToken = mrToken
        minecraftVersions.add(minecraft)
        requires("fabric-api", "configurable")
    }

    curseforge {
        projectId = property("publishing.curseforge") as String
        accessToken = cfToken
        minecraftVersions.add(minecraft)
        requires("fabric-api", "configurable")
    }
}