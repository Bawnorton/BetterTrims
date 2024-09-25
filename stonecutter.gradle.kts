import org.gradle.configurationcache.extensions.capitalized

plugins {
    id("dev.kikugie.stonecutter")
}
stonecutter active "1.21.1-fabric" /* [SC] DO NOT EDIT */

fun chiseledTask(task : String, group : String) {
    val name = "chiseled${task.capitalized()}"

    stonecutter registerChiseled tasks.register(name, stonecutter.chiseled) {
        versions = stonecutter.versions.filter { it.project.endsWith("neoforge") }
        this.group = group
        ofTask(task)
        dependsOn("Pre${name.capitalized()}")
    }

    stonecutter registerChiseled tasks.register("Pre${name.capitalized()}", stonecutter.chiseled) {
        versions = stonecutter.versions.filter { it.project.endsWith("fabric") }
        this.group = group
        ofTask(task)
    }
}

chiseledTask("buildAndCollect", "project")
chiseledTask("publishMods", "publishing")
chiseledTask("publishMavenPublicationToMavenLocal", "publishing")
chiseledTask("publishMavenPublicationToBawnortonRepository", "publishing")

stonecutter configureEach {
    val current = project.property("loom.platform")
    val platforms = listOf("fabric", "neoforge").map { it to (it == current) }
    consts(platforms)
}
