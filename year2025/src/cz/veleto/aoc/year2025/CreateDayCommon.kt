package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay

fun createDayCommon(day: Int): AocDay = when (day) {
    else -> error("undefined day $day")
}

internal val mainConfig = Year2025Config(
    log = false,
    verboseLog = false,
)
