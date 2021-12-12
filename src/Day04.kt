data class Board(val rows: List<Row>)
data class Row(val cells: List<Cell>)
data class Cell(val number: Int, var isMarked: Boolean = false)

fun main() {

    fun parseBoards(input: List<String>): MutableList<Board> {
        val boards = mutableListOf<Board>()
        var rows = mutableListOf<Row>()
        for (rowStr in input) {
            if (rowStr.isBlank()) {
                boards.add(Board(rows))
                rows = mutableListOf()
                continue
            }
            val cells = rowStr.split(" ").filter { it.isNotBlank() }.map { Cell(it.toInt()) }
            rows.add(Row(cells))
        }
        if (rows.isNotEmpty()) {
            boards.add(Board(rows))
        }
        return boards
    }

    fun markNumber(number: Int, boards: List<Board>) {
        for (board in boards) {
            for (row in board.rows) {
                for (cell in row.cells) {
                    if (cell.number == number) {
                        cell.isMarked = true
                    }
                }
            }
        }
    }

    fun isWinning(board: Board): Boolean {
        if (board.rows.any { row -> row.cells.all { it.isMarked } }) {
            return true
        }

        for ((columnIndex, _) in board.rows.first().cells.withIndex()) {
            if (board.rows.all { it.cells[columnIndex].isMarked }) {
                return true
            }
        }
        return false
    }

    fun calcScore(number: Int, board: Board): Int {
        return number * board.rows.sumOf { row -> row.cells.filter { !it.isMarked }.sumOf { it.number } }
    }

    fun part1(input: List<String>): Int {
        val drawnNumbers = input.first().split(",").map { it.toInt() }
        val boards = parseBoards(input.drop(2))

        for (number in drawnNumbers) {
            markNumber(number, boards)
            for (board in boards) {
                if (isWinning(board)) {
                    return calcScore(number, board)
                }
            }
        }

        return 0
    }

    fun part2(input: List<String>): Int {
        val drawnNumbers = input.first().split(",").map { it.toInt() }
        val boards = parseBoards(input.drop(2))

        for (number in drawnNumbers) {
            markNumber(number, boards)
            if (boards.any { !isWinning(it) }) {
                boards.removeIf { isWinning(it) }
                continue
            }
            return calcScore(number, boards.first())
        }

        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
