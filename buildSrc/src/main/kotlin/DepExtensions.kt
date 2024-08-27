import com.google.gson.JsonParser
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

fun DependencyHandler.neoForge(dep: Any) = add("neoForge", dep)
fun DependencyHandler.forge(dep: Any) = add("forge", dep)
fun DependencyHandler.forgeRuntimeLibrary(dep: Any) = add("forgeRuntimeLibrary", dep)

fun Dependency?.stripAw(project: Project) {
    val configuration = project.configurations.detachedConfiguration(this)
    configuration.resolve().forEach { file ->
        val tempFile = File(file.parent, file.name + ".tmp")
        JarFile(file).use { jar ->
            JarOutputStream(FileOutputStream(tempFile)).use { jos ->
                jar.entries().asSequence().forEach { entry ->
                    if (!entry.name.endsWith(".accesswidener")) {
                        jos.putNextEntry(ZipEntry(entry.name))
                        if (entry.name.endsWith("fabric.mod.json")) {
                            val jsonContent = jar.getInputStream(entry).bufferedReader().use { it.readText() }
                            val modifiedJson = removeAccessWidenerEntry(jsonContent)
                            jos.write(modifiedJson.toByteArray())
                        } else {
                            jar.getInputStream(entry).use { input ->
                                input.copyTo(jos)
                            }
                        }
                        jos.closeEntry()
                    }
                }
            }
        }
        Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}

fun removeAccessWidenerEntry(jsonContent: String): String {
    val jsonElement = JsonParser.parseString(jsonContent)
    if (jsonElement.isJsonObject) {
        val jsonObject = jsonElement.asJsonObject
        jsonObject.remove("accessWidener")
        return jsonObject.toString()
    }
    return jsonContent
}