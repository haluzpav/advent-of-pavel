import kotlin.math.abs

class Day18(inputName: String) {
    private val input: Sequence<String> = readInput(inputName)

    fun part1(): Int {
        val cubes = parseCubes()
        return countExposedSides(cubes)
    }

    fun part2(): Int {
        val cubes = parseCubes()
        val grid = Grid(cubes)
        findPocketCandidates(grid)
        demoteCandidatesBySpreadingAir(grid)
        val pockets = getPockets(grid)
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

    private fun findPocketCandidates(grid: Grid) {
        for (x in grid.xRange) {
            for (y in grid.yRange) {
                for (z in grid.zRange) {
                    val pos = Triple(x, y, z)
                    if (grid[pos] != Block.Air) continue
                    val rays = raysFromPos(grid, x, y, z)
                    val isCandidate = rays.all { ray ->
                        ray.any { grid[it] == Block.Cube }
                    }
                    if (isCandidate) grid[pos] = Block.PocketCandidate
                }
            }
        }
    }

    private fun demoteCandidatesBySpreadingAir(grid: Grid) {
        var anyCandidateDemoted = true
        while (anyCandidateDemoted) {
            anyCandidateDemoted = false
            for (x in grid.xRange) {
                for (y in grid.yRange) {
                    for (z in grid.zRange) {
                        val pos = Triple(x, y, z)
                        if (grid[pos] != Block.PocketCandidate) continue
                        val rays = raysFromPos(grid, x, y, z)
                        val candidateDemoted = rays.any { ray ->
                            ray.takeWhile { grid[it] != Block.Cube }.any { grid[it] == Block.Air }
                        }
                        if (candidateDemoted) {
                            grid[pos] = Block.Air
                            anyCandidateDemoted = true
                        }
                    }
                }
            }
        }
    }

    private fun getPockets(grid: Grid): List<Triple<Int, Int, Int>> = buildList {
        for (x in grid.xRange) {
            for (y in grid.yRange) {
                for (z in grid.zRange) {
                    val pos = Triple(x, y, z)
                    if (grid[pos] == Block.PocketCandidate) this += pos
                }
            }
        }
    }

    @Suppress("ReplaceRangeToWithUntil")
    private fun raysFromPos(grid: Grid, x: Int, y: Int, z: Int): Sequence<Sequence<Triple<Int, Int, Int>>> = sequenceOf(
        sequence { for (rx in (grid.xRange.first..x - 1).reversed()) yield(Triple(rx, y, z)) },
        sequence { for (rx in x + 1..grid.xRange.last) yield(Triple(rx, y, z)) },
        sequence { for (ry in (grid.yRange.first..y - 1).reversed()) yield(Triple(x, ry, z)) },
        sequence { for (ry in y + 1..grid.yRange.last) yield(Triple(x, ry, z)) },
        sequence { for (rz in (grid.zRange.first..z - 1).reversed()) yield(Triple(x, y, rz)) },
        sequence { for (rz in z + 1..grid.zRange.last) yield(Triple(x, y, rz)) },
    )

    private class Grid(cubes: List<Triple<Int, Int, Int>>) {
        val xRange = cubes.minOf { it.first }..cubes.maxOf { it.first }
        val yRange = cubes.minOf { it.second }..cubes.maxOf { it.second }
        val zRange = cubes.minOf { it.third }..cubes.maxOf { it.third }
        private val grid: List<List<MutableList<Block>>> = xRange.map { x ->
            yRange.map { y ->
                zRange.map { z ->
                    if (Triple(x, y, z) in cubes) Block.Cube else Block.Air
                }.toMutableList()
            }
        }

        operator fun get(pos: Triple<Int, Int, Int>): Block =
            grid[pos.first - xRange.first][pos.second - yRange.first][pos.third - zRange.first]

        operator fun set(pos: Triple<Int, Int, Int>, value: Block) {
            grid[pos.first - xRange.first][pos.second - yRange.first][pos.third - zRange.first] = value
        }
    }

    private enum class Block {
        Air, Cube, PocketCandidate
    }
}

fun main() {
    val task = Day18("Day18")
    println(task.part1())
    println(task.part2())
}
