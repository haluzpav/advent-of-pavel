package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay

data class Year2025Config(
    override val inputName: String? = null,
    override val log: Boolean = true,
    override val verboseLog: Boolean = true,
    val day08Part1PairCount: Int? = null,
) : AocDay.Config {
    override val yearName: String = "year2025"
}
