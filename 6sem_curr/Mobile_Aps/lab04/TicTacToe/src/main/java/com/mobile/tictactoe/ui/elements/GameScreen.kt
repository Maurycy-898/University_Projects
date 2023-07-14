package com.mobile.tictactoe.ui.elements

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobile.tictactoe.ui.state.GameViewModel
import com.mobile.tictactoe.ui.state.baseSize
import com.mobile.tictactoe.ui.theme.backgroundColor
import com.mobile.tictactoe.ui.theme.boardColor
import com.mobile.tictactoe.ui.theme.textColor
import com.mobile.tictactoe.ui.theme.titleColor


@Composable
fun GameScreen(viewModel: GameViewModel) {
    val context = LocalContext.current
    val gameInfoState = viewModel.gameInfoState

    var gameSizeInput by remember { mutableStateOf("$baseSize") }
    var gameSize by remember { mutableStateOf(baseSize) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            InfoText(text = "Player O : ${gameInfoState.countO}", fontSize = 17.sp)
            InfoText(text = "Draws : ${gameInfoState.drawCount}", fontSize = 17.sp)
            InfoText(text = "Player X : ${gameInfoState.countX}", fontSize = 17.sp)
        }
        Text(
                text = "Tic Tac Toe",
        fontSize = 50.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.SansSerif,
        color = titleColor
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            InfoText(
                text = "Game Size: ",
                fontSize = 17.sp,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            TextField(
                value = gameSizeInput,
                shape = RoundedCornerShape(10.dp),
                onValueChange = { gameSizeInput = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(color = textColor, fontSize = 17.sp, textAlign = TextAlign.Center),
                modifier = Modifier.width(170.dp).padding(horizontal = 10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = textColor,
                    focusedIndicatorColor = textColor,
                    backgroundColor = boardColor
                )
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(20.dp)
                )
                .clip(RoundedCornerShape(20.dp))
                .background(boardColor),
            contentAlignment = Alignment.Center
        ) {
            if (isValid(gameSize)) {
                GameBoard(
                    gameSize = gameSize,
                    board = viewModel.boardFields,
                    onClick = viewModel::onFieldClick
                )
            } else {
                Toast.makeText(context,
                    "Game size must be > 3 and < 10",
                    Toast.LENGTH_SHORT).show()
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            InfoText(
                text = gameInfoState.hintText,
                fontSize = 21.sp,
                color = titleColor
            )
            Button(
                modifier = Modifier.padding(horizontal = 25.dp),
                onClick = {
                    try {
                        gameSize = gameSizeInput.toInt()
                        viewModel.onRestart(gameSize)
                    } catch (_: java.lang.NumberFormatException) {
                        Toast.makeText(context,
                            "Game size must be > 3 and < 10",
                            Toast.LENGTH_SHORT).show()
                    }
                },
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.elevation(5.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = titleColor,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Restart Game",
                    color = backgroundColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        }
    }
}

@Composable
fun InfoText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit,
    color:Color = textColor
) {
    Text(
        modifier = modifier
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .background(boardColor)
            .padding(10.dp),
        text = text,
        fontSize = fontSize,
        color = color
    )
}

fun isValid(input: Int) : Boolean {
    if (input < 3) return false
    if (input > 10) return false
    return true
}
