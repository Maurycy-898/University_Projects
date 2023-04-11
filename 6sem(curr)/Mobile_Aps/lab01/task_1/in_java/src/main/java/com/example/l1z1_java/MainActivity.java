package com.example.l1z1_java;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int score = 0;
    private int val1 = 0;
    private int val2 = 0;
    private Random generator = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        roll();
    }

    private void roll() {
        TextView scoreInfo = (TextView) findViewById(R.id.points);
        scoreInfo.setText("Punkty: " + score);
        val1 = generator.nextInt(100);
        val2 = generator.nextInt(100);
        TextView leftBtn = (TextView) findViewById(R.id.leftButton);
        TextView rightBtn = (TextView) findViewById(R.id.rightButton);
        leftBtn.setText("" + val1);
        rightBtn.setText("" + val2);
    }

    public void rightClick(View view) {
        if (val1 <= val2) {
            score++;
            Toast.makeText(this, "Dobrze!!!", Toast.LENGTH_SHORT).show();
        }
        else {
            score--;
            Toast.makeText(this, "Źle!!!", Toast.LENGTH_SHORT).show();
        }
        roll();
    }

    public void leftClick(View view) {
        if (val1 >= val2) {
            score++;
            Toast.makeText(this, "Dobrze!!!", Toast.LENGTH_SHORT).show();
        }
        else {
            score--;
            Toast.makeText(this, "Źle!!!", Toast.LENGTH_SHORT).show();
        }
        roll();
    }
}