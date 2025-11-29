package cz.veleto.aoc.year2024

import cz.veleto.aoc.core.AocDay

data class Year2024Config(
    override val inputName: String? = null,
    override val log: Boolean = true,
    override val verboseLog: Boolean = true,
) : AocDay.Config {
    override val yearName: String = "year2024"
}
