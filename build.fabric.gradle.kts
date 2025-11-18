@file:Suppress("UnstableApiUsage")

import bettertrims.utils.ReplacementType
import bettertrims.utils.applyMixinDebugSettings
import bettertrims.utils.deps
import bettertrims.utils.getReplacements
import bettertrims.utils.getSwaps
import bettertrims.utils.mod
import dev.kikugie.fletching_table.annotation.MixinEnvironment

plugins {
  kotlin("jvm")
  `maven-publish`
  id("bettertrims.common")
  id("fabric-loom")
  id("me.modmuss50.mod-publish-plugin")
  id("com.google.devtools.ksp") version "2.2.0-2.0.2"
  id("dev.kikugie.fletching-table.fabric") version "0.1.0-alpha.18"
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
  minecraft("com.mojang:minecraft:$minecraft")
  mappings(loom.layered {
    officialMojangMappings()
    deps("parchment") {
      parchment("org.parchmentmc.data:parchment-$it@zip")
    }
  })

  modImplementation("net.fabricmc:fabric-loader:0.17.2")
  modImplementation("net.fabricmc.fabric-api:fabric-api:${deps("fabric_api")}")

  deps("configurable") {
    modImplementation(annotationProcessor("com.bawnorton.configurable:configurable-$loader:$it")!!)
  }
}

java {
  withSourcesJar()
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

loom {
  accessWidenerPath.set(rootProject.file("src/main/resources/$minecraft.accesswidener"))

  fabricApi {
    configureDataGeneration {
      createRunConfiguration = true
      client = true
      modId = mod("id")!!
    }

    configureTests {
      enableGameTests = false
      eula = true
      clearRunDirectory = false
    }
  }

  runConfigs.all {
    ideConfigGenerated(true)
    runDir = "../../run"
    appendProjectPathToConfigName = false
  }

  runConfigs["client"].apply {
    programArgs("--username=Bawnorton", "--uuid=17c06cab-bf05-4ade-a8d6-ed14aaf70545")
    name = "Fabric Client $minecraft"
  }

  runConfigs["server"].apply {
    name = "Fabric Server $minecraft"
  }

  runConfigs["clientGameTest"].apply {
    name = "Fabric Client Game Test $minecraft"
  }

  runConfigs["datagen"].apply {
    name = "Fabric Data Generation $minecraft"
  }

  afterEvaluate {
    runConfigs.configureEach {
      applyMixinDebugSettings(::vmArg, ::property)
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

  fabric {
    entrypointMappings.put("fabric-datagen", "net.fabricmc.fabric.api.datagen.v1.FabricDataGeneratorEntrypoint")
  }
}

stonecutter {
  replacements {
    for (replacement in getReplacements()) {
      when (replacement.type) {
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

tasks {
  register<Copy>("buildAndCollect") {
    group = "build"
    from(remapJar.map { it.archiveFile })
    into(rootProject.layout.buildDirectory.file("libs/${mod("version")}"))
    dependsOn("build")
  }

  remapJar {
    dependsOn("runDatagen")
  }

  named<Jar>("sourcesJar") {
    dependsOn("runDatagen")
  }

  named("runDatagen") {
    dependsOn("runClientGameTest")
  }

  processResources {
    exclude("META-INF/neoforge.mods.toml")
    exclude { it.name.endsWith("-accesstransformer.cfg") }
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
  file = tasks.remapJar.map { it.archiveFile.get() }
  additionalFiles.from(tasks.remapSourcesJar.map { it.archiveFile.get() })

  displayName = "${mod("name")} Fabric ${mod("version")} for $minecraft"
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