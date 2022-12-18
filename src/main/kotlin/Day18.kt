import kotlin.math.abs

class Day18(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)

    fun part1(): Int {
        val cubes = parseCubes()
        return countExposedSides(cubes)
    }

    fun part2(): Int {
        val cubes = parseCubes()
        val volume = Volume(cubes)
        val candidates = findPocketCandidates(volume, cubes)
        val pockets = demoteCandidatesBySpreadingAir(volume, cubes, candidates)
        return countExposedSides(cubes + pockets)
    }

    private fun parseCubes(): List<Triple<Int, Int, Int>> = input.map { line ->
        line.split(",")
            .map { it.toInt() }
            .let { (x, y, z) -> Triple(x, y, z) }
    }.toList()

    private fun countExposedSides(cubes: List<Triple<Int, Int, Int>>): Int {
        var exposedSides = 0
        for (i in 0..cubes.lastIndex) {
            exposedSides += 6
            val (x1, y1, z1) = cubes[i]
            for (j in i..cubes.lastIndex) {
                val (x2, y2, z2) = cubes[j]
                if (abs(x1 - x2) + abs(y1 - y2) + abs(z1 - z2) == 1) exposedSides -= 2
            }
        }
        return exposedSides
    }

    private fun findPocketCandidates(volume: Volume, cubes: List<Triple<Int, Int, Int>>) = buildList {
        for (x in volume.xRange) {
            for (y in volume.yRange) {
                for (z in volume.zRange) {
                    val pos = Triple(x, y, z)
                    if (pos in cubes) continue
                    val rays = raysFromPos(volume, x, y, z)
                    val isCandidate = rays.all { ray ->
                        ray.any { it in cubes }
                    }
                    if (isCandidate) this += pos
                }
            }
        }
    }

    private fun demoteCandidatesBySpreadingAir(
        volume: Volume,
        cubes: List<Triple<Int, Int, Int>>,
        candidates: List<Triple<Int, Int, Int>>,
    ): List<Triple<Int, Int, Int>> {
        var anyCandidateDemoted = true
        var keptCandidates = candidates
        while (anyCandidateDemoted) {
            anyCandidateDemoted = false
            for (x in volume.xRange) {
                for (y in volume.yRange) {
                    for (z in volume.zRange) {
                        val pos = Triple(x, y, z)
                        if (pos !in keptCandidates) continue
                        val rays = raysFromPos(volume, x, y, z)
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

    @Suppress("ReplaceRangeToWithUntil")
    private fun raysFromPos(volume: Volume, x: Int, y: Int, z: Int): Sequence<Sequence<Triple<Int, Int, Int>>> = sequenceOf(
        sequence { for (rx in (volume.xRange.first..x - 1).reversed()) yield(Triple(rx, y, z)) },
        sequence { for (rx in x + 1..volume.xRange.last) yield(Triple(rx, y, z)) },
        sequence { for (ry in (volume.yRange.first..y - 1).reversed()) yield(Triple(x, ry, z)) },
        sequence { for (ry in y + 1..volume.yRange.last) yield(Triple(x, ry, z)) },
        sequence { for (rz in (volume.zRange.first..z - 1).reversed()) yield(Triple(x, y, rz)) },
        sequence { for (rz in z + 1..volume.zRange.last) yield(Triple(x, y, rz)) },
    )

    private class Volume(cubes: List<Triple<Int, Int, Int>>) {
        val xRange = cubes.minOf { it.first }..cubes.maxOf { it.first }
        val yRange = cubes.minOf { it.second }..cubes.maxOf { it.second }
        val zRange = cubes.minOf { it.third }..cubes.maxOf { it.third }
    }
}

fun main() {
    val task = Day18("Day18")
    println(task.part1())
    println(task.part2())
}
