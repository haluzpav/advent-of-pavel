package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay

fun createDayJvm(day: Int): AocDay = when (day) {
    19 -> Day19(mainConfig.copy(inputName = "Day19"))
    24 -> Day24(mainConfig.copy(inputName = "Day24"))
    else -> createDayCommon(day)
}
