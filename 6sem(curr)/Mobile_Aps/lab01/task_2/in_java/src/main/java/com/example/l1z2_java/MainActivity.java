package com.example.l1z2_java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int number = 0;
    private int guesses = 0;
    private boolean gameON = true;
    private final Random g = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
    }

    private void setup() {
        this.guesses = 0;
        this.gameON = true;
        this.number = g.nextInt(1000);
        TextView guesses = (TextView) findViewById(R.id.gesses_counter);
        TextView gameInfo = (TextView) findViewById(R.id.game_info);
        EditText input = (EditText) findViewById(R.id.editTextNumber);
        guesses.setText("Guesses: 0");
        gameInfo.setText("Guess the random number. \nThe number is in range: \nfrom 1 to 1000.");
        input.getText().clear();
    }

    public void checkInput(View view) {
        if (gameON) {
            EditText input = (EditText) findViewById(R.id.editTextNumber);
                    int guess;
            try {
                guess = Integer.parseInt(input.getText().toString());
                input.getText().clear();
            } catch (NumberFormatException ne) {
                Toast.makeText(this, "Wrong input!", Toast.LENGTH_SHORT).show();
                input.getText().clear();
                return;
            }
            guesses++;
            TextView guesses = (TextView) findViewById(R.id.gesses_counter);
            guesses.setText("Guesses: " + this.guesses);

            TextView gameInfo = (TextView) findViewById(R.id.game_info);
            if (guess == number) {
                gameInfo.setText("Congratulations!!! \nYou guessed correctly.");
                gameON = false;
            }

            else if (guess < number) {
                gameInfo.setText("Wrong! \nThe number is higher.");
            }
            else {
                gameInfo.setText("Wrong! \nThe number is lower.");
            }
        }
    }

    public void restartGame(View view) {
        setup();
    }
}
