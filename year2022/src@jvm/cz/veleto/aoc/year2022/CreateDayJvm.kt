package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.baseSeriousConfig

fun createDayJvm(day: Int): AocDay = when (day) {
    19 -> Day19(baseSeriousConfig.copy(inputName = "Day19"))
    24 -> Day24(baseSeriousConfig.copy(inputName = "Day24"))
    else -> createDayCommon(day)
}
