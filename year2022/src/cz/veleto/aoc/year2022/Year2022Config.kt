package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay

data class Year2022Config(
    override val inputName: String? = null,
    override val log: Boolean = true,
    override val verboseLog: Boolean = true,
    val day15part1row: Int = -1,
    val day15part2max: Int = -1,
) : AocDay.Config {
    override val yearName: String = "year2022"
}
