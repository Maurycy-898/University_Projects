package zadanie2a

import kotlin.collections.ArrayList
import kotlin.math.floor
import kotlin.math.pow
import kotlin.random.Random


class CSMA {
    private var cable : ArrayList<ArrayList<Transmission>>
    private val transmitters = ArrayList<Transmitter>()

    init {
        val size = 50
        cable = ArrayList(size)
        for (i in 0 until size) {
            cable.add(ArrayList())
        }
    }

    fun addNode(id: Char, position: Int, probability: Double) {
        var t = 0
        while (Random.nextDouble() > probability) {
            t++
        }
        transmitters.add(Transmitter(id, position, probability, false, false, t, 1))
    }

    fun step() {
        val newCable = ArrayList<ArrayList<Transmission>>()

        for (i in cable.size downTo 1) {
            newCable.add(ArrayList())
        }

        for (i in 0 until cable.size) {
            for (t in cable[i]) {
                when (t.direction) {
                    -1 -> {
                        if (i != 0) {
                            newCable[i - 1].add(Transmission(t.id, -1))
                        }
                    }
                    0 -> {
                        if (i != 0) {
                            newCable[i - 1].add(Transmission(t.id, -1))
                        }
                        if (i != cable.size- 1) {
                            newCable[i + 1].add(Transmission(t.id, 1))
                        }
                    }
                    1 -> {
                        if (i != cable.size - 1) {
                            newCable[i + 1].add(Transmission(t.id, 1))
                        }
                    }
                }
            }
        }

        cable = newCable

        for (node in transmitters) {
            if (!node.emits && node.timeout == 0) {
                if (cable[node.position].size > 0) {
                    node.timeout += cable.size / 2
                } else {
                    node.timeout = 2 * cable.size + 1
                    node.emits = true
                }
            }

            if (node.emits) {
                cable[node.position].add(Transmission(node.id, 0))
                if (!node.collision && cable[node.position].size > 1) {
                    node.collision = true
                }
            }
            node.timeout--

            if (node.emits && node.timeout == 0) {
                node.emits = false

                if (node.collision) {
                    val timeouts = ArrayList<Int>()
                    for (i in 0..node.mult) {
                        timeouts.add(2.0.pow(i.toDouble()).toInt())
                    }
                    node.timeout = cable.size * timeouts[floor(Random.nextDouble() * timeouts.size).toInt()]
                    node.mult++
                    node.collision = false
                }
                else {
                    node.mult = 1
                    while (Random.nextDouble() > node.probability) {
                        node.timeout++
                    }
                }
            }
        }
    }

    fun printCSMA() {
        println("\u001Bc")
        print("[")
        for (i in 0 until cable.size) {
            when (cable[i].size) {
                1 -> print(cable[i][0].id + " ")
                0 -> print("  ")
                else -> print("# ")
            }
        }
        print("]")
        println()
    }
}