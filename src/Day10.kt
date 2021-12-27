import BracketType.*
import ParseResult.CORRUPTED
import ParseResult.ILLEGAL_CHAR

enum class BracketType {
    ROUND,
    SQUARE,
    CURLY,
    ANGLE,
}

enum class Bracket(
    val char: Char,
    val type: BracketType,
    val isStart: Boolean,
    val errorScore: Int,
    val completeScore: Int
) {
    ROUND_START('(', ROUND, true, 3, 1),
    ROUND_END(')', ROUND, false, 3, 1),
    SQUARE_START('[', SQUARE, true, 57, 2),
    SQUARE_END(']', SQUARE, false, 57, 2),
    CURLY_START('{', CURLY, true, 1197, 3),
    CURLY_END('}', CURLY, false, 1197, 3),
    ANGLE_START('<', ANGLE, true, 25137, 4),
    ANGLE_END('>', ANGLE, false, 25137, 4);

    companion object {
        private val charToBracket = values().associateBy { it.char }
        private val matchingBrackets = mapOf(
            ROUND_START to ROUND_END,
            ROUND_END to ROUND_START,
            SQUARE_START to SQUARE_END,
            SQUARE_END to SQUARE_START,
            CURLY_START to CURLY_END,
            CURLY_END to CURLY_START,
            ANGLE_START to ANGLE_END,
            ANGLE_END to ANGLE_START
        )

        fun fromChar(s: Char?): Bracket? {
            return charToBracket[s]
        }

        fun matchingBracket(bracket: Bracket): Bracket {
            return matchingBrackets.getValue(bracket)
        }
    }
}

enum class ParseResult {
    CORRUPTED,
    ILLEGAL_CHAR
}

data class BracketParseResult(val result: ParseResult, val illegalChar: Bracket?, val missingBrackets: List<Bracket>)

fun main() {

    fun parseChunk(line: String, currentIndex: Int, currentLine: ArrayDeque<Bracket>): BracketParseResult {
        val currentChar = line[currentIndex]
        val currentBracket = Bracket.fromChar(currentChar)!!

        if (currentBracket.isStart) {
            currentLine.add(currentBracket)
        } else {
            if (currentLine.last().type == currentBracket.type) {
                currentLine.removeLast()
            } else {
                return BracketParseResult(ILLEGAL_CHAR, currentBracket, listOf())
            }
        }

        if (currentIndex < line.length - 1) {
            return parseChunk(line, currentIndex + 1, currentLine)
        }

        return BracketParseResult(CORRUPTED, currentBracket, currentLine.map { Bracket.matchingBracket(it) }.reversed())
    }

    fun parseLine(line: String): BracketParseResult {
        return parseChunk(line, 0, ArrayDeque())
    }

    fun calcCompletionScore(missingBrackets: List<Bracket>): Long {
        var score = 0L
        for (bracket in missingBrackets) {
            score *= 5
            score += bracket.completeScore
        }
        return score
    }

    fun part1(input: List<String>): Int {
        val lines = input.map { it.trim() }
        var totalScore = 0
        for (line in lines) {
            val result = parseLine(line)
            if (result.result == ILLEGAL_CHAR) {
                totalScore += result.illegalChar!!.errorScore
            }
        }
        return totalScore
    }

    fun part2(input: List<String>): Long {
        val lines = input.map { it.trim() }

        val scores = mutableListOf<Long>()
        for (line in lines) {
            val result = parseLine(line)
            if (result.result == CORRUPTED) {
                scores.add(calcCompletionScore(result.missingBrackets))
            }
        }
        return scores.sorted()[Math.floorDiv(scores.size, 2)]
    }

    val testInput = readInput("Day10_test")
    val input = readInput("Day10")

    val part1Test = part1(testInput)
    println("Test answer part1 = $part1Test")
    check(part1Test == 26397)
    println("Answer part1 = ${part1(input)}")

    val part2Test = part2(testInput)
    println("Test answer part2 = $part2Test")
    check(part2Test == 288957L)
    println("Answer part2 = ${part2(input)}")
}
