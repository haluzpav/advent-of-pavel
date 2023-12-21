package cz.veleto.aoc.year2023

actual fun <T> PriorityQueue(comparator: Comparator<in T>): PriorityQueue<T> = object : PriorityQueue<T> {
    val impl = java.util.PriorityQueue(comparator)

    override fun offer(element: T) {
        impl.offer(element)
    }

    override fun peek(): T = impl.peek()
    override fun poll(): T = impl.poll()
}
