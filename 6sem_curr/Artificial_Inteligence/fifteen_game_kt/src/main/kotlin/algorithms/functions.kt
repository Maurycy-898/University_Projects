package algorithms

import java.util.*
import kotlin.math.abs
import kotlin.math.max

fun aStar(initialState: IntArray, heuristic: (IntArray) -> Int) : Result {
    val openSet = PriorityQueue<GameState>()
    val closeSet = HashMap<String, Int>()
    var visitedStates = 1

    openSet.add(GameState(initialState).apply { this.computeScores(heuristic) })
    while (!openSet.isEmpty()) {
        val currState = openSet.poll()!!
        if (currState.hScore == 0) {
            return Result(currState, closeSet.keys.size)
        }
        val currID = currState.stateID
        if (currID in closeSet) {
            continue
        }
        closeSet[currID] = currState.fScore
        currState.expand().forEach {
            it.apply {
                parent = currState
                hScore = heuristic(it.state)
                gScore = currState.gScore + 1
                fScore = hScore + gScore
            }
            openSet.add(it)
        }
        visitedStates += 1
    }
    throw Exception("Impossible to solve")
}

fun manhattanHeuristic(state: IntArray) : Int {
    var totalDistance = 0
    state.forEachIndexed { idx, element ->
        if (element != EMPTY_FIELD) {
            val currRow: Int = idx / GAME_SIZE; val currCol: Int = idx % GAME_SIZE
            val finalRow: Int = (element - 1) / GAME_SIZE; val finalCol: Int = (element - 1) % GAME_SIZE
            val manhattanDist: Int = abs(currRow - finalRow) + abs(currCol - finalCol)
            totalDistance += manhattanDist
        }
    }
    return totalDistance
}

fun linearConflictHeuristic(state: IntArray) : Int {
    var conflicts = 0
    for (row0 in 0 until GAME_SIZE) {
        for (col0 in 0 until GAME_SIZE) {
            val lin0: Int = row0 * GAME_SIZE + col0
            if (state[lin0] == EMPTY_FIELD) {
                continue
            }
            val finalRow0: Int = (state[lin0] - 1) / GAME_SIZE
            val finalCol0: Int = (state[lin0] - 1) % GAME_SIZE

            if (row0 == finalRow0) {
                for (col1 in (col0 + 1) until GAME_SIZE) {
                    val lin1: Int = row0 * GAME_SIZE + col1
                    if (state[lin1] == EMPTY_FIELD) continue

                    val finalRow1: Int = (state[lin1] - 1) / GAME_SIZE
                    if (finalRow1 == finalRow0) {
                        val finalCol1: Int = (state[lin1] - 1) % GAME_SIZE
                        if (finalCol1 < finalCol0) conflicts += 1
                    }
                }
            }
            if (col0 == finalCol0) {
                for (row1 in (row0 + 1) until GAME_SIZE) {
                    val lin1: Int = row1 * GAME_SIZE + col0
                    if (state[lin1] == EMPTY_FIELD) continue

                    val finalCol1: Int = (state[lin1] - 1) % GAME_SIZE
                    if (finalCol1 == finalCol0) {
                        val finalRow1: Int = (state[lin1] - 1) / GAME_SIZE
                        if (finalRow1 < finalRow0) conflicts += 1
                    }
                }
            }

        }
    }
    return manhattanHeuristic(state) + (2 * conflicts)
}

fun inversionDistHeuristic(state: IntArray) : Int {
    var invCount = 0
    for (i in 0 until (GAME_FIELDS - 1)) {
        for (j in i + 1 until GAME_FIELDS) {
            if (state[j] != EMPTY_FIELD && state[i] != EMPTY_FIELD && state[i] > state[j]) {
                invCount += 1
            }
        }
    }
    val horizontal = invCount / 3

    invCount = 0
    for (i in 0 until (GAME_FIELDS - 1)) {
        for (j in i + 1 until GAME_FIELDS) {
            val iIdx = VERTICAL_ORDER[i]
            val jIdx = VERTICAL_ORDER[j]
            if (state[jIdx] != EMPTY_FIELD && state[iIdx] != EMPTY_FIELD && state[iIdx] > state[jIdx]) {
                invCount += 1
            }
        }
    }
    val vertical = invCount / 3

    return vertical + horizontal
}

fun combinedHeuristic(state: IntArray) : Int {
    return max(linearConflictHeuristic(state), inversionDistHeuristic(state))
}

fun isSolvable(state: IntArray) : Boolean {
    val invCount = getInvCount(state)
    return if (GAME_SIZE % 2 == 1) {
        (invCount % 2 == 0)
    } else {
        val emptyFieldRow = state.indexOf(EMPTY_FIELD) / GAME_SIZE
        (emptyFieldRow + invCount) % 2 == 1
    }
}

fun getInvCount(state: IntArray) : Int {
    var invCount = 0
    for (i in 0 until (GAME_FIELDS - 1)) {
        for (j in i + 1 until GAME_FIELDS) {
            if (state[j] != EMPTY_FIELD && state[i] != EMPTY_FIELD && state[i] > state[j]) {
                invCount += 1
            }
        }
    }
    return invCount
}

fun genRandomState(movesToMake: Int) : IntArray {
    val initialState = IntArray(GAME_FIELDS) { (it + 1) % GAME_FIELDS }
    var currGameState = GameState(initialState)

    var movesMade = 0
    while (movesMade < movesToMake) {
        movesMade++
        val moves = currGameState.expand()
        currGameState = moves.shuffled().first()
    }

    return currGameState.state
}

fun genRandomState() : IntArray {
    var resultState: IntArray
    do {
        resultState = IntArray(GAME_FIELDS) { it }
        resultState.shuffle()
    } while (!isSolvable(resultState))

    resultState.forEach { print("$it, ") }
    println()

    return resultState
}
