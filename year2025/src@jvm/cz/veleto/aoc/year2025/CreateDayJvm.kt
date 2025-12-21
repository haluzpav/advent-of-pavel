package cz.veleto.aoc.year2025

import cz.veleto.aoc.core.AocDay

fun createDayJvm(day: Int): AocDay = when (day) {
    // 10 -> Day10Jvm(mainConfig.copy(inputName = "Day10"))
    else -> createDayCommon(day)
}
