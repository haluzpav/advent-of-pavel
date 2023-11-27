package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.baseSeriousConfig

fun createDayCommon(day: Int): AocDay = when (day) {
    1 -> Day01(baseSeriousConfig.copy(inputName = "Day01"))
    2 -> Day02(baseSeriousConfig.copy(inputName = "Day02"))
    3 -> Day03(baseSeriousConfig.copy(inputName = "Day03"))
    4 -> Day04(baseSeriousConfig.copy(inputName = "Day04"))
    5 -> Day05(baseSeriousConfig.copy(inputName = "Day05"))
    6 -> Day06(baseSeriousConfig.copy(inputName = "Day06"))
    7 -> Day07(baseSeriousConfig.copy(inputName = "Day07"))
    8 -> Day08(baseSeriousConfig.copy(inputName = "Day08"))
    9 -> Day09(baseSeriousConfig.copy(inputName = "Day09"))
    10 -> Day10(baseSeriousConfig.copy(inputName = "Day10"))
    11 -> Day11(baseSeriousConfig.copy(inputName = "Day11"))
    12 -> Day12(baseSeriousConfig.copy(inputName = "Day12"))
    13 -> Day13(baseSeriousConfig.copy(inputName = "Day13"))
    14 -> Day14(baseSeriousConfig.copy(inputName = "Day14"))
    15 -> Day15(baseSeriousConfig.copy(inputName = "Day15"))
    16 -> Day16(baseSeriousConfig.copy(inputName = "Day16"))
    17 -> Day17(baseSeriousConfig.copy(inputName = "Day17"))
    18 -> Day18(baseSeriousConfig.copy(inputName = "Day18"))
    20 -> Day20(baseSeriousConfig.copy(inputName = "Day20"))
    21 -> Day21(baseSeriousConfig.copy(inputName = "Day21"))
    22 -> Day22(baseSeriousConfig.copy(inputName = "Day22"))
    23 -> Day23(baseSeriousConfig.copy(inputName = "Day23"))
    25 -> Day25(baseSeriousConfig.copy(inputName = "Day25"))
    else -> error("undefined day $day")
}
