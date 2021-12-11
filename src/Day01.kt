fun main() {
    fun part1(input: List<String>): Int {
        val depths = input.map { it.toInt() }
        var increaseCount = 0
        for ((index, depth) in depths.withIndex()) {
            if (index > 0 && depth > depths[index - 1]) {
                increaseCount++
            }
        }
        return increaseCount
    }

    fun part2(input: List<String>): Int {
        val depths = input.map { it.toInt() }
        var increaseCount = 0
        for ((index, _) in depths.withIndex()) {
            if (index > 2 && depths[index] > depths[index - 3]) {
                increaseCount++
            }
        }
        return increaseCount
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
