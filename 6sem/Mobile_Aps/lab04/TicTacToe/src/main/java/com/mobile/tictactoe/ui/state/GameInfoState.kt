package com.mobile.tictactoe.ui.state

data class GameInfoState(
    val countO: Int = 0,
    val countX: Int = 0,
    val drawCount: Int = 0,
    val gameEnded: Boolean = false,
    val currSymbol: String = playerO,
    val hintText: String = "Player O turn",
)