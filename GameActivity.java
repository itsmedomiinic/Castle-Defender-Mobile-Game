package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //set flags

        Point point = new Point(); //new point
        getWindowManager().getDefaultDisplay().getSize(point); //new point is the size

        gameView = new GameView(this, point.x, point.y); //new gameview

        setContentView(gameView); //content view in the game view
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause(); //pause
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume(); //resume
    }
}


