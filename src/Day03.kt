fun main() {
    fun calcFrequency(input: List<String>): Map<Int, Int> {
        // if frequency > 0, then 1 is more common, else 0 is more common.
        val frequencies = mutableMapOf<Int, Int>() // index : frequency

        for (str in input) {
            val binary = str.trim()
            for ((index, char) in binary.withIndex()) {
                val currentCount = frequencies[index] ?: 0
                if (char == '1') {
                    frequencies[index] = currentCount + 1
                } else {
                    frequencies[index] = currentCount - 1
                }
            }
        }
        return frequencies
    }

    fun part1(input: List<String>): Int {
        val frequencies = calcFrequency(input)

        var gammaBinary = ""
        var epsilonBinary = ""
        for (index in frequencies.keys.sorted()) {
            if (frequencies.getValue(index) > 0) {
                gammaBinary += "1"
                epsilonBinary += "0"
            } else {
                gammaBinary += "0"
                epsilonBinary += "1"
            }
        }

        val gamma = gammaBinary.toInt(2)
        val epsilon = epsilonBinary.toInt(2)
        return gamma * epsilon
    }

    fun filterNumbers(input: List<String>, currentIndex: Int, mostCommon: Boolean): String {
        val frequencies = calcFrequency(input)
        val bitCriteria = if (mostCommon) {
            if (frequencies.getValue(currentIndex) >= 0) '1' else '0'
        } else {
            if (frequencies.getValue(currentIndex) >= 0) '0' else '1'
        }
        val remainingInput = input.filter { it[currentIndex] == bitCriteria }
        if (remainingInput.size == 1) {
            return remainingInput.single()
        }
        return filterNumbers(remainingInput, currentIndex + 1, mostCommon)
    }

    fun part2(input: List<String>): Int {
        // if frequency > 0, then 1 is more common, else 0 is more common.
        val oxygenStr = filterNumbers(input, 0, mostCommon = true)
        val co2Str = filterNumbers(input, 0, mostCommon = false)

        val oxygen = oxygenStr.toInt(2)
        val co2 = co2Str.toInt(2)
        return oxygen * co2
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
