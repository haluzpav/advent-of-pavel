package cz.veleto.aoc.year2024

import cz.veleto.aoc.core.AocDay

fun createDayCommon(day: Int): AocDay = when (day) {
    1 -> Day01(mainConfig.copy(inputName = "Day01"))
    2 -> Day02(mainConfig.copy(inputName = "Day02"))
    3 -> Day03(mainConfig.copy(inputName = "Day03"))
    else -> error("undefined day $day")
}

private val mainConfig = Year2024Config(
    log = false,
    verboseLog = false,
)
