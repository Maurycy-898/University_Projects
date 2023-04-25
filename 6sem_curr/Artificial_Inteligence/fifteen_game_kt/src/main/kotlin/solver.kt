import algorithms.*
import kotlin.system.measureNanoTime

fun main() {
    val initialState = arrayOf(2, 15, 7, 9, 1, 12, 11, 4, 0, 14, 6, 10, 3, 13, 8, 5).toIntArray()
//    val initialState = genRandomState()
    aStar(initialState, ::linearConflictHeuristic).printResult()
    aStar(initialState, ::manhattanHeuristic).printResult()
//    aStar(initialState, ::inversionDistHeuristic).printResult()


//    for (i in 1..39) {
//        val initialState = genRandomState(250)
//        var res1: Result
//        var res2: Result
//        var time1 = measureNanoTime {
//            res1 = aStar(initialState, ::linearConflictHeuristic)
//        }
//        var time2 = measureNanoTime {
//            res2 = aStar(initialState, ::manhattanHeuristic)
//        }
//        time1 /= 1000000; time2 /= 1000000 // in milliseconds
//        println("$time1;$time2;${res1.visitedStates};${res2.visitedStates};${res1.movesAmount};${res2.movesAmount};")
//    }
}
