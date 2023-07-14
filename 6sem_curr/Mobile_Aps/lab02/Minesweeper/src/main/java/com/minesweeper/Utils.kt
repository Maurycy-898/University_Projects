package com.minesweeper

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


//---------------------- CONST VALUES --------------------------------------------------------------
const val BOMB: Int = -1
const val BLANK: Int = 0

//---------------------- RECYCLER ADAPTER ----------------------------------------------------------
class GridRecyclerAdapter(
    private var cells: List<Cell>,
    private val listener: OnCellClickListener
) : RecyclerView.Adapter<GridRecyclerAdapter.MineTileViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MineTileViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_cell, parent, false)
        return MineTileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MineTileViewHolder, position: Int) {
        holder.bind(cells[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return cells.size
    }

    fun refreshCells(newCells: List<Cell>) {
        this.cells = newCells
        notifyDataSetChanged()
    }

    inner class MineTileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val valueTextView: TextView = itemView.findViewById(R.id.item_cell_value)

        fun bind(cell: Cell) {
            itemView.setBackgroundResource(R.color.covered)
            itemView.setOnClickListener { listener.onCellClick(cell) }

            if (cell.isRevealed) {
                when (cell.value) {
                    BOMB -> {
                        valueTextView.setText(R.string.bomb)
                        itemView.setBackgroundResource(R.color.bomb)
                    }
                    BLANK -> {
                        valueTextView.text = ""
                        itemView.setBackgroundResource(R.color.uncovered)
                    }
                    else -> {
                        valueTextView.text = cell.value.toString()
                        itemView.setBackgroundResource(R.color.info)
                        when (cell.value) {
                            1 -> { valueTextView.setTextColor(Color.CYAN) }
                            2 -> { valueTextView.setTextColor(Color.GREEN) }
                            3 -> { valueTextView.setTextColor(Color.YELLOW) }
                            else -> {
                                valueTextView.setTextColor(Color.RED)
                            }
                        }
                    }
                }
            } else if (cell.isFlagged) {
                valueTextView.setText(R.string.field_flag)
            }
        }
    }
}

//---------------------- GAME CELL -----------------------------------------------------------------
class Cell(var value: Int) {
    var isFlagged: Boolean = false
    var isRevealed: Boolean = false
}

//---------------------- CELL GRID -----------------------------------------------------------------
class MineGrid(private val size: Int) {
    val cells = ArrayList<Cell>(List(size * size) { Cell(BLANK) })

    fun generateBombs(totalBombs: Int) {
        val bombs = List(size * size) { it }.shuffled().subList(0, totalBombs)
        bombs.forEach { bombIdx -> cells[bombIdx].value = BOMB }

        cells.forEachIndexed { idx, cell ->
            val xy = toXY(idx)
            if (cellAt(xy.first, xy.second)!!.value != BOMB) {
                val adjacentCells = adjacentCells(xy.first, xy.second)
                var bombsCounter = 0
                adjacentCells.forEach { c -> if (c.value == BOMB) { bombsCounter++ } }

                if (bombsCounter > 0) {
                    cell.value = bombsCounter
                }
            }
        }
    }

    fun revealAllBombs() {
        cells.forEach { cell -> if (cell.value == BOMB) cell.isRevealed = true }
    }

    fun adjacentCells(x: Int, y: Int): ArrayList<Cell> {
        val adjacentCellsList: ArrayList<Cell> = ArrayList()
        cellAt((x - 1), y)?.let { adjacentCellsList.add(it) }
        cellAt((x + 1), y)?.let { adjacentCellsList.add(it) }
        cellAt((x - 1), (y - 1))?.let { adjacentCellsList.add(it) }
        cellAt(x, (y - 1))?.let { adjacentCellsList.add(it) }
        cellAt((x + 1), (y - 1))?.let { adjacentCellsList.add(it) }
        cellAt((x - 1), (y + 1))?.let { adjacentCellsList.add(it) }
        cellAt(x, (y + 1))?.let { adjacentCellsList.add(it) }
        cellAt((x + 1), (y + 1))?.let { adjacentCellsList.add(it) }
        return adjacentCellsList
    }

    fun toXY(index: Int) : Pair<Int, Int> {
        val y = index / size
        val x = index - (y * size)
        return Pair(x, y)
    }

    private fun cellAt(x: Int, y: Int): Cell? {
        return if ((x < 0) || (x >= size) || (y < 0) || (y >= size)) null else cells[x + y * size]
    }
}

//---------------------- GAME CLASS ----------------------------------------------------------------
class MineSweeperGame(size: Int, val numberBombs: Int) {
    val mineGrid = MineGrid(size)
    var timeExpired = false
    var gameOver = false
    var flagMode = false
    var clearMode = true
    var flagCount = 0

    init { mineGrid.generateBombs(numberBombs) }

    fun handleCellClick(cell: Cell) {
        if (!gameOver && !timeExpired && !cell.isRevealed && !isGameWon()) {
            if (clearMode) { clear(cell) } else if (flagMode) { flag(cell) }
        }
    }

    private fun clear(cell: Cell) {
        val index = mineGrid.cells.indexOf(cell)
        mineGrid.cells[index].isRevealed = true

        if (cell.value == BOMB) {
            gameOver = true
        }
        else if (cell.value == BLANK) {
            val toClear: ArrayList<Cell> = ArrayList()
            val toCheckAdjacents: ArrayList<Cell> = ArrayList()

            toCheckAdjacents.add(cell)
            while (toCheckAdjacents.size > 0) {
                val curr = toCheckAdjacents[0]
                val cellIndex = mineGrid.cells.indexOf(curr)
                val cellXY = mineGrid.toXY(cellIndex)

                for (adjacent in mineGrid.adjacentCells(cellXY.first, cellXY.second)) {
                    if (adjacent.value == BLANK) {
                        if (!toClear.contains(adjacent)) {
                            if (!toCheckAdjacents.contains(adjacent)) {
                                toCheckAdjacents.add(adjacent)
                            }
                        }
                    } else {
                        if (!toClear.contains(adjacent)) {
                            toClear.add(adjacent)
                        }
                    }
                }
                toCheckAdjacents.remove(curr)
                toClear.add(curr)
            }
            toClear.forEach { it.isRevealed = true }
        }
    }

    private fun flag(cell: Cell) {
        if (cell.isFlagged) flagCount-- else flagCount++
        cell.isFlagged = !cell.isFlagged
    }

    fun isGameWon(): Boolean {
        mineGrid.cells.forEach { cell ->
            if (cell.value != BOMB && cell.value != BLANK && !cell.isRevealed) {
                return false
            }
        }
        return true
    }

    fun toggleMode() {
        clearMode = !clearMode
        flagMode = !flagMode
    }
}

//---------------------- CELL CLICK ----------------------------------------------------------------
interface OnCellClickListener {
    fun onCellClick(cell: Cell)
}
