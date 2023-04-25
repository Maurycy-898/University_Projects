package algorithms

import java.util.LinkedList

val SOLVED = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0)
val HORIZONTAL_ORDER = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0)
val VERTICAL_ORDER = intArrayOf(0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15)

const val GAME_SIZE = 4
const val GAME_FIELDS = 16
const val EMPTY_FIELD = 0


class GameState(val state: IntArray) : Comparable<GameState> {
    val stateID: String = state.joinToString(" ") { "$it" }
    var hScore: Int=0; var gScore: Int=0; var fScore: Int=0
    var parent: GameState? = null

    override fun compareTo(other: GameState): Int {
        return fScore.compareTo(other.fScore)
    }

    fun computeScores(heuristic: (IntArray) -> Int) {
        gScore = if (parent != null) {
            parent!!.gScore + 1
        } else 0
        hScore = heuristic(state)
        fScore = hScore + gScore
    }

    fun expand() : List<GameState> {
        val outStates = LinkedList<GameState>()
        val swappedState = { a: Int, b: Int ->
            val swapped = this.state.copyOf()
            swapped[a] = swapped[b].also { swapped[b] = swapped[a] }
            GameState(swapped)
        }

        val emptyField = this.state.indexOf(EMPTY_FIELD)
        if (emptyField % GAME_SIZE != GAME_SIZE - 1) {
            outStates.add(swappedState(emptyField, emptyField + 1))
        }
        if (emptyField % GAME_SIZE != 0) {
            outStates.add(swappedState(emptyField, emptyField - 1))
        }
        if (emptyField - GAME_SIZE >= 0) {
            outStates.add(swappedState(emptyField, emptyField - GAME_SIZE))
        }
        if (emptyField + GAME_SIZE < GAME_FIELDS) {
            outStates.add(swappedState(emptyField, emptyField + GAME_SIZE))
        }

        return outStates
    }
}


class Result(finalState: GameState, val visitedStates: Int) {
    private val statesPath = LinkedList<GameState>()
    var movesAmount: Int

    init {
        statesPath.add(finalState)
        var currState = finalState
        while (currState.parent != null) {
            currState = currState.parent!!
            statesPath.add(currState)
        }
        statesPath.reverse()
        movesAmount = statesPath.size
    }

    fun printResult() {
        println("Visited States : $visitedStates, Moves Made: $movesAmount")
        println("Solving process :")
        statesPath.forEach { it.stateID.split(" ").forEachIndexed { idx, symbol ->
            val symbolVal = symbol.toInt()
            if (idx % GAME_SIZE == 0) println()
            if (symbolVal < 10) print(" $symbol ") else print("$symbol ")
        }
            println()
        }
    }

    fun printFormatted(time: Long) {
        println("$visitedStates;$movesAmount;$time")
    }
}
