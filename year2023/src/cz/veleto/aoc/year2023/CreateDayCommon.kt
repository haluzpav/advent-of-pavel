package cz.veleto.aoc.year2023

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
    else -> error("undefined day $day")
}
