package com.mobile.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mobile.tictactoe.ui.elements.GameScreen
import com.mobile.tictactoe.ui.state.GameViewModel
import com.mobile.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                GameScreen(
                    viewModel = GameViewModel()
                )
            }
        }
    }
}
