package com.mobile.tictactoe.ui.state

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

// -------- CONST VALUES ---------------------------------------------------------------------------
const val playerX = "X"
const val playerO = "O"
const val emptySymbol = ""
const val baseSize = 3


// -------- VIEW MODEL -----------------------------------------------------------------------------
class GameViewModel(private var gameSize: Int = baseSize) : ViewModel() {
    private val _boardFields = mutableStateListOf<String>()
    val boardFields: List<String> = _boardFields

    private var winRequired = getWinCond(gameSize)
    var gameInfoState by mutableStateOf(GameInfoState())
        private set

    init {
        repeat(times = gameSize*gameSize) {
            _boardFields.add(emptySymbol)
        }
    }

    fun onRestart(gameSize: Int) {
        this.gameSize = gameSize
        this.winRequired = getWinCond(gameSize)

        _boardFields.clear()
        repeat(this.gameSize * this.gameSize) {
            _boardFields.add(emptySymbol)
        }

        gameInfoState = gameInfoState.copy(
            hintText = "Player O turn",
            currSymbol = playerO,
            gameEnded = false
        )
    }

    fun onFieldClick(fieldID: Int) {
        if (gameInfoState.gameEnded){ return }
        if (boardFields[fieldID] != emptySymbol) return

        val playerSymbol = gameInfoState.currSymbol
        val opponentSymbol = getOpponentSymbol(playerSymbol)

        _boardFields[fieldID] = playerSymbol
        if (checkForVictory()) {
            var countO = gameInfoState.countO
            var countX = gameInfoState.countX

            if (playerSymbol == playerO) countO++
            if (playerSymbol == playerX) countX++

            gameInfoState = gameInfoState.copy(
                hintText = "Player $playerSymbol Won",
                countO = countO,
                countX = countX,
                currSymbol = emptySymbol,
                gameEnded = true
            )
        }
        else if (isBoardFull()) {
            gameInfoState = gameInfoState.copy(
                hintText = "Game Draw",
                drawCount = gameInfoState.drawCount + 1
            )
        }
        else {
            gameInfoState = gameInfoState.copy(
                hintText = "Player $opponentSymbol turn",
                currSymbol = opponentSymbol
            )
        }
    }

    private fun getOpponentSymbol(playerSymbol: String) : String {
        if (playerSymbol == playerX) return playerO
        if (playerSymbol == playerO) return playerX
        return emptySymbol
    }

    private fun checkForVictory() : Boolean {
        var count: Int
        var prevSymbol: String
        var currSymbol: String
        var _row: Int; var _col:Int

        // check horizontal
        for(row in 0 until gameSize) {
            count = 1; prevSymbol = emptySymbol
            for (col in 0 until gameSize) {
                currSymbol = boardFields[linearPos(row, col)]
                if (currSymbol == emptySymbol) { count = 0; continue }

                if (currSymbol == prevSymbol) { count ++ }
                else { count = 1 }

                if (count >= winRequired) return true
                prevSymbol = currSymbol
            }
        }

        // check vertical
        for(col in 0 until gameSize) {
            count = 1; prevSymbol = emptySymbol
            for (row in 0 until gameSize) {
                currSymbol = boardFields[linearPos(row, col)]
                if (currSymbol == emptySymbol) { count = 0; continue }

                if (currSymbol == prevSymbol) { count ++ }
                else { count = 1 }

                if (count >= winRequired) return true
                prevSymbol = currSymbol
            }
        }

        // check diagonal-right-bottom
        for (rowOffset in 0 .. (gameSize - winRequired)) {
            count = 1; prevSymbol = emptySymbol
            for (i in 0 until (gameSize - rowOffset)) {
                _row = i + rowOffset; _col = i
                currSymbol = boardFields[linearPos(_row, _col)]
                if (currSymbol == emptySymbol) { count = 0; continue }

                if (currSymbol == prevSymbol) { count++ }
                else { count = 1 }

                if (count >= winRequired) return true
                prevSymbol = currSymbol
            }
        }
        // check diagonal-right-top
        for (colOffset in 1 .. (gameSize - winRequired)) {
            count = 1; prevSymbol = emptySymbol
            for (i in 0 until (gameSize - colOffset)) {
                _row = i; _col = i + colOffset
                currSymbol = boardFields[linearPos(_row, _col)]
                if (currSymbol == emptySymbol) { count = 0; continue }

                if (currSymbol == prevSymbol) { count++ }
                else { count = 1 }

                if (count >= winRequired) return true
                prevSymbol = currSymbol
            }
        }

        // check diagonal-left-bottom
        for (rowOffset in (gameSize-1)downTo(winRequired-1)) {
            count = 1; prevSymbol = emptySymbol
            for (i in  0 ..rowOffset) {
                _row = rowOffset - i; _col = i
                currSymbol = boardFields[linearPos(_row, _col)]
                if (currSymbol == emptySymbol) { count = 0; continue }

                if (currSymbol == prevSymbol) { count++ }
                else { count = 1 }

                if (count >= winRequired) return true
                prevSymbol = currSymbol
            }
        }
        // check diagonal-left-top
        for (colOffset in 0 .. (gameSize - winRequired)) {
            count = 1; prevSymbol = emptySymbol
            for (i in (gameSize-1) downTo  colOffset) {
                _row = i; _col = colOffset + ((gameSize-1) - i)
                currSymbol = boardFields[linearPos(_row, _col)]
                if (currSymbol == emptySymbol) { count = 0; continue }

                if (currSymbol == prevSymbol) { count++ }
                else { count = 1 }

                if (count >= winRequired) return true
                prevSymbol = currSymbol
            }
        }

        return false // no victory found
    }

    private fun isBoardFull() : Boolean {
        return !boardFields.contains(emptySymbol)
    }

    private fun getWinCond(gameSize: Int) : Int {
        val extra: Int = gameSize / baseSize - 1
        return baseSize + extra
    }

    private fun linearPos(row: Int, col: Int) : Int {
        return row * gameSize + col
    }

}
