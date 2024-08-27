import org.gradle.api.Project

class ModData(project: Project) {
    val id = project.property("mod_id").toString()
    val name = project.property("mod_name").toString()
    val version = project.property("mod_version").toString()
    val group = project.property("mod_group").toString()
    val minecraftDependency = project.property("minecraft_dependency").toString()
    val minSupportedVersion = project.property("mod_min_supported_version").toString()
    val maxSupportedVersion = project.property("mod_max_supported_version").toString()
}