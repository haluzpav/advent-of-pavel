package cz.veleto.aoc.year2022

import cz.veleto.aoc.core.AocDay
import cz.veleto.aoc.core.Pos3
import cz.veleto.aoc.core.manhattanTo

class Day18(config: Config) : AocDay(config) {

    override fun part1(): String {
        val cubes = parseCubes()
        return countExposedSides(cubes).toString()
    }

    override fun part2(): String {
        val cubes = parseCubes()
        val volume = Volume(cubes)
        val candidates = findPocketCandidates(volume, cubes)
        val pockets = demoteCandidatesBySpreadingAir(volume, cubes, candidates)
        return countExposedSides(cubes + pockets).toString()
    }

    private fun parseCubes(): List<Pos3> = input.map { line ->
        line.split(",")
            .map { it.toInt() }
            .let { (x, y, z) -> Pos3(x, y, z) }
    }.toList()

    private fun countExposedSides(cubes: List<Pos3>): Int {
        var exposedSides = 0
        for (i in 0..cubes.lastIndex) {
            exposedSides += 6
            for (j in i..cubes.lastIndex) {
                if (cubes[i].manhattanTo(cubes[j]) == 1) exposedSides -= 2
            }
        }
        return exposedSides
    }

    private fun findPocketCandidates(volume: Volume, cubes: List<Pos3>) = buildList {
        for (x in volume.xRange) {
            for (y in volume.yRange) {
                for (z in volume.zRange) {
                    val pos = Pos3(x, y, z)
                    if (pos in cubes) continue
                    val rays = raysFromPos(pos, volume)
                    val isCandidate = rays.all { ray ->
                        ray.any { it in cubes }
                    }
                    if (isCandidate) this += pos
                }
            }
        }
    }

    private fun demoteCandidatesBySpreadingAir(volume: Volume, cubes: List<Pos3>, candidates: List<Pos3>): List<Pos3> {
        var anyCandidateDemoted = true
        var keptCandidates = candidates
        while (anyCandidateDemoted) {
            anyCandidateDemoted = false
            for (x in volume.xRange) {
                for (y in volume.yRange) {
                    for (z in volume.zRange) {
                        val pos = Pos3(x, y, z)
                        if (pos !in keptCandidates) continue
                        val rays = raysFromPos(pos, volume)
                        val candidateDemoted = rays.any { ray ->
                            ray.takeWhile { it !in cubes }.any { it !in keptCandidates }
                        }
                        if (candidateDemoted) {
                            keptCandidates = keptCandidates - pos
                            anyCandidateDemoted = true
                        }
                    }
                }
            }
        }
        return keptCandidates
    }

    private fun raysFromPos(pos: Pos3, volume: Volume): Sequence<Sequence<Pos3>> {
        val (x, y, z) = pos
        return sequenceOf(
            sequence { for (rx in (volume.xRange.first..<x).reversed()) yield(Pos3(rx, y, z)) },
            sequence { for (rx in x + 1..volume.xRange.last) yield(Pos3(rx, y, z)) },
            sequence { for (ry in (volume.yRange.first..<y).reversed()) yield(Pos3(x, ry, z)) },
            sequence { for (ry in y + 1..volume.yRange.last) yield(Pos3(x, ry, z)) },
            sequence { for (rz in (volume.zRange.first..<z).reversed()) yield(Pos3(x, y, rz)) },
            sequence { for (rz in z + 1..volume.zRange.last) yield(Pos3(x, y, rz)) },
        )
    }

    private class Volume(cubes: List<Pos3>) {
        val xRange = cubes.minOf { it.first }..cubes.maxOf { it.first }
        val yRange = cubes.minOf { it.second }..cubes.maxOf { it.second }
        val zRange = cubes.minOf { it.third }..cubes.maxOf { it.third }
    }
}
