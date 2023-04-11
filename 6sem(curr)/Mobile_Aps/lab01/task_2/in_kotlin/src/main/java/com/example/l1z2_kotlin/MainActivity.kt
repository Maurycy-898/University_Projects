package com.example.l1z2_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private var number = 0
    private var guesses = 0
    private var gameON = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }

    private fun setup() {
        guesses = 0
        gameON = true
        number = Random.nextInt(1000)
        findViewById<TextView>(R.id.gesses_counter).text = "Guesses: 0"
        findViewById<TextView>(R.id.game_info).text = "Guess the random number. \nThe number is in range: \nfrom 1 to 1000."
        findViewById<EditText>(R.id.editTextNumber).text.clear()
    }

    fun checkInput(view: View) {
        if (gameON) {
            val input = findViewById<EditText>(R.id.editTextNumber)
            val guess: Int
            try {
                    guess = input.text.toString().toInt()
                    input.text.clear()
            } catch (e: java.lang.NumberFormatException) {
                Toast.makeText(this, "Wrong input!", Toast.LENGTH_SHORT).show()
                input.text.clear()
                return
            }
            guesses++
            findViewById<TextView>(R.id.gesses_counter).text = "Guesses: $guesses"

            if (guess == number) {
                findViewById<TextView>(R.id.game_info).text = "Congratulations!!! \nYou guessed correctly."
                gameON = false
            }
            else if (guess < number) {
                findViewById<TextView>(R.id.game_info).text = "Wrong! \nThe number is higher."
            }
            else {
                findViewById<TextView>(R.id.game_info).text = "Wrong! \nThe number is lower."
            }
        }
    }

    fun restartGame(view: View) {
        setup()
    }
}
