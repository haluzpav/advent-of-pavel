package cz.veleto.aoc.year2022

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
    13 -> Day13(mainConfig.copy(inputName = "Day13"))
    14 -> Day14(mainConfig.copy(inputName = "Day14"))
    15 -> Day15(mainConfig.copy(inputName = "Day15"))
    16 -> Day16(mainConfig.copy(inputName = "Day16"))
    17 -> Day17(mainConfig.copy(inputName = "Day17"))
    18 -> Day18(mainConfig.copy(inputName = "Day18"))
    20 -> Day20(mainConfig.copy(inputName = "Day20"))
    21 -> Day21(mainConfig.copy(inputName = "Day21"))
    22 -> Day22(mainConfig.copy(inputName = "Day22"))
    23 -> Day23(mainConfig.copy(inputName = "Day23"))
    25 -> Day25(mainConfig.copy(inputName = "Day25"))
    else -> error("undefined day $day")
}

internal val mainConfig = Year2022Config(
    log = false,
    verboseLog = false,
    day15part1row = 2_000_000,
    day15part2max = 4_000_000,
)
