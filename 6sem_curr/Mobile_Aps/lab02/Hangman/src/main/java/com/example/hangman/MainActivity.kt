package com.example.hangman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


//--------------------- GAME CONST VARS ------------------------------------------------------------
const val GRID_SIZE = 7
const val MAX_GUESSES = 6


//--------------------- MAIN GAME ACTIVITY ---------------------------------------------------------
class MainActivity : AppCompatActivity(), OnItemClickListener {
    //--------------------- INIT GAME VARS ---------------------------------------------------------
    private lateinit var guessedLettersIdx: ArrayList<Int>
    private lateinit var hangmanGame: HangmanGame
    private lateinit var wordToGuess: String

    private lateinit var hangmanImage: ImageView
    private lateinit var currentWordView: TextView
    private lateinit var guessesView: TextView
    private lateinit var correctGView: TextView
    private lateinit var wrongGView: TextView
    private lateinit var lettersGrid: RecyclerView
    private lateinit var gridRecyclerAdapter: GridRecyclerAdapter
    private lateinit var restartButton: Button

    //--------------------- GAME SETUP -------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wordToGuess = rollForWord()
        hangmanGame = HangmanGame(wordToGuess)
        guessedLettersIdx = ArrayList()

        lettersGrid = findViewById(R.id.guess_letters_grid)
        lettersGrid.layoutManager = GridLayoutManager(this, GRID_SIZE)
        gridRecyclerAdapter = GridRecyclerAdapter(hangmanGame.letters, (this), (this))
        lettersGrid.adapter = gridRecyclerAdapter

        hangmanImage = findViewById(R.id.hangman_state)
        currentWordView = findViewById(R.id.word_to_guess)
        guessesView = findViewById(R.id.guesses_no)
        correctGView = findViewById(R.id.correct_no)
        wrongGView = findViewById(R.id.wrong_no)
        restartButton = findViewById(R.id.restart_btn)

        restartButton.setOnClickListener { restartGame() }
        updateViews()
    }

    //--------------------- GUESS LETTERS ----------------------------------------------------------
    override fun onItemClick(letterCell: LetterCell) {
        if (!letterCell.wasGuessed && !hangmanGame.isGameOver()) {
            letterCell.wasGuessed = true
            hangmanGame.makeGuess(letterCell.letter)
            gridRecyclerAdapter.notifyItemChanged(letterCell.index)
            guessedLettersIdx += letterCell.index
            updateViews()
        }
        if (hangmanGame.isGameOver()) {
            val message = if (hangmanGame.wordGuessed()) "YOU WON !!! - TRY AGAIN"
                else "YOU LOST !!! - TRY AGAIN"
            currentWordView.text = if (hangmanGame.wordGuessed()) "Y O U   W O N"
                else "Y O U   L O S T"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    //--------------------- RESTART GAME -----------------------------------------------------------
    private fun restartGame() {
        guessedLettersIdx.forEach { gridRecyclerAdapter.notifyItemChanged(it) }
        guessedLettersIdx.clear()
        wordToGuess = rollForWord()
        hangmanGame.restartGame(wordToGuess)
        updateViews()
    }

    //--------------------- UPDATE VIEWS -----------------------------------------------------------
    private fun updateViews() {
        currentWordView.text = hangmanGame.getCurrWordString()
        guessesView.text = hangmanGame.getCurrGuessesString()
        correctGView.text = hangmanGame.getCurrCorrectGString()
        wrongGView.text = hangmanGame.getCurrWrongGString()
        hangmanImage.setImageResource(chooseHangmanImage(hangmanGame.getGameStage()))
    }

    //--------------------- FIND RIGHT IMAGE -------------------------------------------------------
    private fun chooseHangmanImage(stage: Int) : Int {
        return when(stage) {
            1 -> R.drawable.stage_1
            2 -> R.drawable.stage_2
            3 -> R.drawable.stage_3
            4 -> R.drawable.stage_4
            5 -> R.drawable.stage_5
            6 -> R.drawable.stage_6
            7 -> R.drawable.stage_7
            else -> R.drawable.stage_0
        }
    }

    //--------------------- CHOOSE THE WORD --------------------------------------------------------
    private fun rollForWord() : String {
        val array: Array<String> = resources.getStringArray(R.array.word_array)
        return array.random().uppercase()
    }
}
