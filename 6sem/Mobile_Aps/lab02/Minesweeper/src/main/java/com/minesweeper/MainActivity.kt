package com.minesweeper

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


const val TIMER_LENGTH = 999000L
const val BOMB_COUNT = 9
const val GRID_SIZE = 9


class MainActivity : AppCompatActivity(), OnCellClickListener {
    private var timerStarted = false
    private var secondsElapsed = 0

    private lateinit var mineGridRecyclerAdapter: GridRecyclerAdapter
    private lateinit var mineGrid: RecyclerView
    private lateinit var replayFace: TextView
    private lateinit var timer: TextView
    private lateinit var flagSwitch: TextView
    private lateinit var flagsLeft: TextView
    private lateinit var mineSweeperGame: MineSweeperGame
    private lateinit var countDownTimer: CountDownTimer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ----------------- GAME VARS SETUP -------------------------------------------------------
        mineSweeperGame = MineSweeperGame(GRID_SIZE, BOMB_COUNT)

        mineGrid = findViewById(R.id.game_grid)
        mineGrid.layoutManager = GridLayoutManager(this, GRID_SIZE)
        mineGridRecyclerAdapter = GridRecyclerAdapter(mineSweeperGame.mineGrid.cells, this)
        mineGrid.adapter = mineGridRecyclerAdapter

        flagsLeft = findViewById(R.id.flags_counter)
        flagsLeft.text = String.format("%03d", mineSweeperGame.numberBombs - mineSweeperGame.flagCount)

        timer = findViewById(R.id.game_timer)
        replayFace = findViewById(R.id.replay_face)
        flagSwitch = findViewById(R.id.flag_switch)

        // ----------------- TIMER SETUP -----------------------------------------------------------
        timerStarted = false
        countDownTimer = object: CountDownTimer(TIMER_LENGTH, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                secondsElapsed += 1
                timer.text = String.format("%03d", secondsElapsed)
            }
            override fun onFinish() {
                mineSweeperGame.timeExpired
                Toast.makeText(applicationContext, "Game Over: Timer Expired", Toast.LENGTH_SHORT).show()
                mineSweeperGame.mineGrid.revealAllBombs()
                mineGridRecyclerAdapter.refreshCells(mineSweeperGame.mineGrid.cells)
            }
        }

        // ----------------- REPLAY FACE -----------------------------------------------------------
        replayFace.setOnClickListener {
            mineSweeperGame = MineSweeperGame(GRID_SIZE, BOMB_COUNT)
            mineGridRecyclerAdapter.refreshCells(mineSweeperGame.mineGrid.cells)
            timerStarted = false
            secondsElapsed = 0
            countDownTimer.cancel()
            timer.setText(R.string.count)
            flagSwitch.text = getString(R.string.bomb)
            flagsLeft.text = String.format("%03d", mineSweeperGame.numberBombs - mineSweeperGame.flagCount)
            replayFace.text = getString(R.string.smile)
        }

        // ----------------- FLAG SWITCH -----------------------------------------------------------
        flagSwitch.setOnClickListener {
            mineSweeperGame.toggleMode()
            flagSwitch.text = if (mineSweeperGame.flagMode) getString(R.string.button_flag)
                else getString(R.string.button_bomb)
        }
    }

    // --------------------- CLICK HANDLING ---------------------------------------------------------
    override fun onCellClick(cell: Cell) {
        mineSweeperGame.handleCellClick(cell)
        flagsLeft.text = String.format("%03d", mineSweeperGame.numberBombs - mineSweeperGame.flagCount)

        if (!timerStarted) {
            replayFace.text = getString(R.string.neutral)
            countDownTimer.start()
            timerStarted = true
        }
        if (mineSweeperGame.gameOver) {
            replayFace.text = getString(R.string.sad)
            countDownTimer.cancel()
            mineSweeperGame.mineGrid.revealAllBombs()
            Toast.makeText(applicationContext, "Game Over", Toast.LENGTH_SHORT).show()
        }
        if (mineSweeperGame.isGameWon()) {
            replayFace.text = getString(R.string.smile)
            countDownTimer.cancel()
            mineSweeperGame.mineGrid.revealAllBombs()
            Toast.makeText(applicationContext, "Game Won", Toast.LENGTH_SHORT).show()
        }

        mineGridRecyclerAdapter.refreshCells(mineSweeperGame.mineGrid.cells)
    }
}
