package cz.veleto.aoc.year2023

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
    10 -> Day10(mainConfig.copy(inputName = "Day10"))
    11 -> Day11(mainConfig.copy(inputName = "Day11"))
    12 -> Day12(mainConfig.copy(inputName = "Day12"))
    16 -> Day16(mainConfig.copy(inputName = "Day16"))
    18 -> Day18(mainConfig.copy(inputName = "Day18"))
    else -> error("undefined day $day")
}

internal val mainConfig = Year2023Config(
    log = false,
    verboseLog = false,
    day11part2expandFactor = 1_000_000,
)
