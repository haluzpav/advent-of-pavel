package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay

fun createDayCommon(day: Int): AocDay = when (day) {
    1 -> Day01(mainConfig.copy(inputName = "Day01"))
    2 -> Day02(mainConfig.copy(inputName = "Day02"))
    3 -> Day03(mainConfig.copy(inputName = "Day03"))
    4 -> Day04(mainConfig.copy(inputName = "Day04"))
    5 -> Day05(mainConfig.copy(inputName = "Day05"))
    6 -> Day06(mainConfig.copy(inputName = "Day06"))
    7 -> Day07(mainConfig.copy(inputName = "Day07"))
    8 -> Day08(mainConfig.copy(inputName = "Day08"))
    9 -> Day09(mainConfig.copy(inputName = "Day09"))
    else -> error("undefined day $day")
}

internal val mainConfig = Year2025Config(
    log = true,
    verboseLog = false,
    day08Part1PairCount = 1000,
)
