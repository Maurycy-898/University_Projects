package com.example.l1z1_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var score: Int = 0
    private var val1: Int  = 0
    private var val2: Int  = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        roll()
    }

    private fun roll() {
        findViewById<TextView>(R.id.points).text = "Punkty: $score"
        val1 = Random.nextInt(100)
        val2 = Random.nextInt(100)
        findViewById<TextView>(R.id.leftButton).text  = "$val1"
        findViewById<TextView>(R.id.rightButton).text = "$val2"
    }

    fun leftClick(view: View) {
        if (val1 >= val2) {
            score++
            Toast.makeText(this, "Dobrze!!!", Toast.LENGTH_SHORT).show()
        }
        else {
            score--
            Toast.makeText(this, "Źle!!!", Toast.LENGTH_SHORT).show()
        }
        roll()
    }

    fun rightClick(view: View) {
        if (val1 <= val2) {
            score++
            Toast.makeText(this, "Dobrze!!!", Toast.LENGTH_SHORT).show()
        }
        else {
            score--
            Toast.makeText(this, "Źle!!!", Toast.LENGTH_SHORT).show()
        }
        roll()
    }
}
