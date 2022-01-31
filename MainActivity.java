package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //set flags

        setContentView(R.layout.activity_main); //layout setup in the main activity

        findViewById(R.id.playButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        }); //play button when clicked will start the game

        findViewById(R.id.instructions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Instructions.class));
            }
        });//instructions when clicked will display instructionstxt image with information

        TextView highScoreTxt = findViewById(R.id.highScoretxt); //the highscore of the user

        final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);//shared preferences

        highScoreTxt.setText("HighScore: " + prefs.getInt("highscore", 0));//keep highscores, default 0 until improved
    }
}
