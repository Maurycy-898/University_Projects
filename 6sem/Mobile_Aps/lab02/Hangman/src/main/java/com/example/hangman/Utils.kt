package com.example.hangman

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


//--------------------- LETTER GRID ADAPTER  -------------------------------------------------------
class GridRecyclerAdapter(
    private var letterCells: List<LetterCell>,
    private val myListener: OnItemClickListener,
    context: Context
) : RecyclerView.Adapter<GridRecyclerAdapter.ViewHolder>()
{
    private val mInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = mInflater.inflate(R.layout.recycler_item_cell, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(letterCells[position])
    }

    override fun getItemCount(): Int {
        return letterCells.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val letterTextView: TextView = itemView.findViewById(R.id.item_cell_value)

        fun bind(letterCell: LetterCell) {
            itemView.setOnClickListener { myListener.onItemClick(letterCell) }
            itemView.setBackgroundResource(R.color.letter_bg)
            letterTextView.text = letterCell.letter.toString()
            letterTextView.setTextColor(Color.WHITE)
            if (letterCell.wasGuessed) {
                letterTextView.text = ""
                itemView.setBackgroundResource(R.color.guessed_letter_bg)
            }
        }
    }
}


//--------------------- LETTER CELL CLASS ----------------------------------------------------------
class LetterCell(val letter: Char, val index: Int) {
    var wasGuessed = false
}


//--------------------- MAIN GAME CLASS ------------------------------------------------------------
class HangmanGame(private var wordToGuess: String) {
    val letters = ArrayList<LetterCell>()
    private var guessedL: String = " "
    private var guesses: Int = 0
    private var correctG: Int = 0
    private var wrongG: Int = 0

    init {
        "AĄBCĆDEĘFGHIJKLŁMNŃOÓPQRSŚTUVWXYZŹŻ".forEachIndexed { idx, letter ->
            letters.add(LetterCell(letter, idx))
        }
    }

    fun makeGuess(letter: Char) {
        guesses++
        if (letter in wordToGuess) correctG++ else wrongG++
        guessedL += letter
    }

    fun restartGame(newWord: String) {
        letters.forEach { it.wasGuessed = false }
        wordToGuess = newWord
        guesses = 0; correctG = 0; wrongG = 0; guessedL = " "
    }

    fun isGameOver() : Boolean {
        return (wrongG > MAX_GUESSES || wordGuessed())
    }

    fun wordGuessed() : Boolean {
        wordToGuess.forEach { letter ->
            if (letter !in guessedL) return false
        }
        return true
    }

    fun getCurrWordString(): String {
        var wordString = ""
        wordToGuess.forEach { l ->
            wordString += if (l in guessedL) "$l " else "_ "
        }
        return wordString
    }

    fun getGameStage() : Int {
        return wrongG
    }

    fun getCurrGuessesString() : String {
        return "TOTAL: $guesses"
    }

    fun getCurrCorrectGString() : String {
        return "CORRECT: $correctG"
    }

    fun getCurrWrongGString() : String {
        return "WRONG: $wrongG"
    }
}


//--------------------- HANDLE LETTER CLICK --------------------------------------------------------
interface OnItemClickListener {
    fun onItemClick(letterCell: LetterCell)
}
