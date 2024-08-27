import dev.kikugie.stonecutter.StonecutterBuild

class MinecraftVersionData(private val stonecutter: StonecutterBuild) {
    private val name = stonecutter.current.version.substringBeforeLast("-")

    fun equalTo(other: String) : Boolean = stonecutter.compare(name, other.lowercase()) == 0
    fun greaterThan(other: String) : Boolean = stonecutter.compare(name, other.lowercase()) > 0
    fun lessThan(other: String) : Boolean = stonecutter.compare(name, other.lowercase()) < 0

    override fun toString(): String {
        return name
    }

    fun javaVersion(): Int = if (greaterThan("1.20.5")) 21 else 17
}