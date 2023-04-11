//package lab04.tests
//
//import lab05.utils.Stats
//import java.io.File
//import kotlin.random.Random
//
//fun randKeys(n: Int): IntArray {
//    return IntArray(n) { Random.nextInt((2 * n)) }
//}
//
//fun ascKeys(n: Int): IntArray {
//    return IntArray(n) { x -> x }
//}
//
//fun testTrees(start: Int, end: Int, inc: Int, randKeys: Boolean) {
//    val CMPs = File("src\\main\\java\\lab04\\tests\\results\\csvs\\CMPs${if (randKeys) "_rand" else "_asc"}.csv")
//    val MEMs = File("src\\main\\java\\lab04\\tests\\results\\csvs\\MEMs${if (randKeys) "_rand" else "_asc"}.csv")
//    val ins_HGTs = File("src\\main\\java\\lab04\\tests\\results\\csvs\\insHGTs${if (randKeys) "_rand" else "_asc"}.csv")
//    val del_HGTs = File("src\\main\\java\\lab04\\tests\\results\\csvs\\delHGTs${if (randKeys) "_rand" else "_asc"}.csv")
//
//    var bst: TestBST; var rb: TestRB; var spl: TestSPL
//    var bstStats: Stats; var rbStats: Stats; var splStats: Stats
//    val insBstHgts = IntArray(end); val insRbHgts = IntArray(end); val insSplHgts = IntArray(end)
//    val delBstHgts  = IntArray(end); val delRbHgts = IntArray(end); val delSplHgts = IntArray(end)
//    var keys: IntArray
//
//    for (i in start .. end step inc) {
//        bstStats = Stats(); rbStats = Stats(); splStats = Stats()
//
//        for (j in 1..20) {
//            bst = TestBST(); rb = TestRB(); spl = TestSPL()
//
//            keys = if (randKeys) randKeys(i) else ascKeys(i)
//            keys.forEachIndexed { idx, key ->
//                bst.insert(key); rb.insert(key); spl.insert(key)
//                if (i == end) {
//                    insBstHgts[idx] += bst.calcHeight()
//                    insRbHgts[idx] += rb.calcHeight()
//                    insSplHgts[idx] += spl.calcHeight()
//
//                    if (j == 20)
//                    ins_HGTs.appendText(
//                        "$i;${i - idx - 1};${idx + 1};${insBstHgts[idx] / 20};${insRbHgts[idx] / 20};${insSplHgts[idx] / 20};\n"
//                    )
//                }
//            }
//
//            keys.shuffle()
//            keys.forEachIndexed { idx, key ->
//                bst.delete(key); rb.delete(key); spl.delete(key)
//                if (i == end) {
//                    delBstHgts[idx] += bst.calcHeight()
//                    delRbHgts[idx] += rb.calcHeight()
//                    delSplHgts[idx] += spl.calcHeight()
//
//                    if (j == 20) {
//                        del_HGTs.appendText(
//                            "$i;${i - idx - 1};${idx + 1};${delBstHgts[idx] / 20};${delRbHgts[idx] / 20};${delSplHgts[idx] / 20};\n"
//                        )
//                    }
//                }
//            }
//
//            bstStats.addStats(bst.stats); rbStats.addStats(rb.stats); splStats.addStats(spl.stats)
//            print("$j, ")
//        }
//
//        bstStats.divStats(20); rbStats.divStats(20); splStats.divStats(20)
////        MEMs.appendText("$i;${bstStats.mem};${rbStats.mem};${splStats.mem};\n")
////        CMPs.appendText("$i;${bstStats.cmp};${rbStats.cmp};${splStats.cmp};\n")
//
//        println("\n$i")
//    }
//}
//
//fun main() {
//    testTrees(5000, 100000, 5000, true)
//    //testTrees(25000, 25000, 5000, false)
//}
