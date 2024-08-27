import org.gradle.api.Project

class LoaderData(private val project: Project, private val name: String) {
    val isFabric = name == "fabric"
    val isNeoForge = name == "neoforge"

    fun getVersion() : String = if (isNeoForge) {
        project.property("loader_neoforge").toString()
    } else {
        project.property("fabric_loader").toString()
    }

    override fun toString(): String {
        return name
    }
}