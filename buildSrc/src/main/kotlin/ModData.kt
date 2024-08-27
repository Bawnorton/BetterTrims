import org.gradle.api.Project

class ModData(project: Project) {
    val id = project.property("mod_id").toString()
    val name = project.property("mod_name").toString()
    val description = project.property("mod_description").toString()
    val version = project.property("mod_version").toString()
    val group = project.property("mod_group").toString()
    val minecraftDependency = project.property("minecraft_dependency").toString()
    val supportedVersions = project.property("supported_versions").toString()
    val modrinthProjId = project.property("modrinth_project_id").toString()
    val curseforgeProjId = project.property("curseforge_project_id").toString()
}