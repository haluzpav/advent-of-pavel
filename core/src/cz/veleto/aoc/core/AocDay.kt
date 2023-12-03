package cz.veleto.aoc.core

abstract class AocDay(protected val config: Config) {

    data class Config(
        val inputName: String,
        val log: Boolean = true,
        // TODO make Config extendable somehow ??
        val year2022day15part1row: Int = -1,
        val year2022day15part2max: Int = -1,
    )

    protected val input: Sequence<String> = readInput(config.inputName)

    // prefer to use the Sequence above only
    protected val cachedInput: List<String> by lazy(LazyThreadSafetyMode.NONE) {
        input.toList()
    }

    abstract fun part1(): String
    abstract fun part2(): String
}
