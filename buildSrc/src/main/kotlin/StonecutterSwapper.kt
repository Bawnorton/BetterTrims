import dev.kikugie.stonecutter.StonecutterBuild
import net.fabricmc.loader.api.Version

class StonecutterSwapper(private val stonecutter: StonecutterBuild) {
    private val swaps = HashMap<String, Swap>()

    fun register(key: String, version: String, below: String, aboveOrEqual: String) : StonecutterSwapper {
        return register(key, Version.parse(version), below, aboveOrEqual)
    }

    private fun register(key: String, version: Version, below: String, aboveOrEqual: String) : StonecutterSwapper {
        swaps[key] = Swap(version, Pair(below, aboveOrEqual))
        return this
    }

    fun apply(version: String) {
        for ((key, swap) in swaps) {
            stonecutter.swap(key, if (swap.version > Version.parse(version)) swap.first() else swap.second())
        }
    }

    private data class Swap(val version: Version, val swap: Pair<String, String>) {
        fun first() = swap.first
        fun second() = swap.second
    }
}