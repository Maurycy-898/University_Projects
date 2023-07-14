package com.mobile.tictactoe.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobile.tictactoe.ui.state.playerX
import com.mobile.tictactoe.ui.theme.boardColor
import com.mobile.tictactoe.ui.theme.circleColor
import com.mobile.tictactoe.ui.theme.crossColor


@Composable
fun GameBoard(gameSize: Int, board: List<String>, onClick: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(boardColor)
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var counterID = 0
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
        ) {

        }
        repeat(gameSize) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat (gameSize) {
                    val fieldID = counterID
                    DrawField(
                        gameSize = gameSize,
                        symbol = board[fieldID],
                        modifier = Modifier
                            .weight(.8f)
                            .aspectRatio(1f)
                            .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(20.dp))
                    ) { onClick(fieldID) }

                    counterID ++
                }
            }
        }
    }
}

@Composable
fun DrawField(modifier: Modifier, gameSize: Int, symbol: String, onClick: () -> Unit){
    val fieldFontSize = 150 / gameSize
    val fieldColor = if (symbol == playerX) crossColor else circleColor
    Box(
        modifier = modifier.clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            fontSize = fieldFontSize.sp,
            color = fieldColor
        )
    }
}
