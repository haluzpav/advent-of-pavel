package cz.veleto.aoc.core

abstract class AocDay(protected open val config: Config) {

    interface Config {
        val yearName: String
        val inputName: String?
        val log: Boolean
        val verboseLog: Boolean
    }

    protected val input: Sequence<String> by lazy(LazyThreadSafetyMode.NONE) {
        readInput(
            yearName = config.yearName,
            inputName = requireNotNull(config.inputName) { "inputName should be non-null before running a Day" },
        )
    }

    // prefer to use the Sequence above only
    protected val cachedInput: List<String> by lazy(LazyThreadSafetyMode.NONE) {
        input.toList()
    }

    abstract fun part1(): String
    abstract fun part2(): String
}
