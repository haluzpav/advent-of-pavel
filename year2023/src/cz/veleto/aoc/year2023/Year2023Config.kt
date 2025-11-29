package cz.veleto.aoc.year2023

import cz.veleto.aoc.core.AocDay

data class Year2023Config(
    override val inputName: String? = null,
    override val log: Boolean = true,
    override val verboseLog: Boolean = true,
    val day11part2expandFactor: Int = 2,
) : AocDay.Config {
    override val yearName: String = "year2023"
}
