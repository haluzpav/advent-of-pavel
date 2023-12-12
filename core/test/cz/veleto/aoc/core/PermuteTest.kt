package cz.veleto.aoc.core

import kotlin.test.Test
import kotlin.test.assertEquals

class PermuteTest {

    @Test
    fun permuteTest() {
        val permutations = permute(1, 2, 3).toList()

        assertEquals(8, permutations.size)
        assertEquals(8, permutations.toSet().size)

        permutations.forEach { permutation ->
            assertEquals(3, permutation.size)
            permutation.all { it == 1 || it == 2 }
        }
    }
}
