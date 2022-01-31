package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random; //imports used for the GameView

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying, isGameOver = false;
    private int screenX, screenY, score = 0;
    public static float screenRatioX, screenRatioY; //ratio of the screensize for other devices
    private Paint paint;
    private Bandit[] banditList; //list of bandits/enemies
    private SharedPreferences prefs;
    private Random random; //random for speed
    private SoundPool soundPool;
    private List<Bullet> bullets; //bullets
    private int sound;
    private Wizard wizard; //wizard/player
    private GameActivity activity;
    private Background background1, background2; //variables for dealing with the background

    private int level = 1, maxSpeed, minSpeed; //variable for dealing with the level

    /**
     *
     * @param activity setup the game view
     * @param screenX size of screenX
     * @param screenY size of screenY
     */
    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        sound = soundPool.load(activity, R.raw.shoot, 1);

        this.screenX = screenX;
        this.screenY = screenY; //gets the screensize
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY; //gets the screen ratio

        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources()); //two backgrounds - same image

        wizard = new Wizard(this, screenY, getResources()); //create wizard/player

        bullets = new ArrayList<>(); //create array to store bullets

        background2.x = screenX; //background 2 starts where background 1 ends

        paint = new Paint();
        paint.setTextSize(70);
        paint.setColor(Color.BLACK); //paint config

        banditList = new Bandit[4]; //spawns 4 bandits at a time

        for (int i = 0;i < 4;i++) {

            Bandit bandit = new Bandit(getResources());
            banditList[i] = bandit;

        } //add the bandits to the list

        random = new Random(); //random

    }


    @Override
    public void run() {

        while (isPlaying) {

            update ();
            draw ();
            improvementThread (); //update, draw, sleep (with multithreading)

        }
    }

    /**
     * used for multithreading -> make game run smoother
     */
    private void improvementThread(){
        Runnable runnable = new Runnable() { //allow to be run on a thread
            @Override
            public void run() {
                sleep(); //threads are seperated to increase framerate -> smoother

            }
        };
        Thread increaseThread = new Thread(runnable); //new thread
        increaseThread.start(); //start new thread
    }

    /**
     * update position of wizard, bullets and bandits
     */
    private void update () {

        background1.x -= 10 * screenRatioX;
        background2.x -= 10 * screenRatioX; //moves the backgrounds

        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenX;
        }
        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX;
        } //widths are not 0

        if (wizard.isGoingUp)
            wizard.y -= 30 * screenRatioY; //move the wizard upwards
        else
            wizard.y += 30 * screenRatioY; //move the wizard downwards

        if (wizard.y < 0)
            wizard.y = 0;

        if (wizard.y >= screenY - wizard.height)
            wizard.y = screenY - wizard.height; //does not let wizard exceed top

        List<Bullet> trash = new ArrayList<>(); //list of bullets

        for (Bullet bullet : bullets) {

            if (bullet.x > screenX)
                trash.add(bullet); //remove bullets

            bullet.x += 50 * screenRatioX; //move bullets on screen

            for (Bandit bandit : banditList) {

                if (Rect.intersects(bandit.getCollisionShape(),
                        bullet.getCollisionShape())) { //check the bandit has been hit

                    score++; //add to the score
                    bandit.x = -500;
                    bullet.x = screenX + 500;
                    bandit.wasShot = true; //dead bandit

                }

            }

        }

        for (Bullet bullet : trash)
            bullets.remove(bullet); //remove bullets

        for (Bandit bandit : banditList) {

            bandit.x -= bandit.speed;

            if (bandit.x + bandit.width < 0) {

                if (!bandit.wasShot) {
                    isGameOver = true;
                    return;
                } //ends the game when the bandit touches the end

                if (level == 1){
                    maxSpeed = 10;
                    minSpeed = 5; //speed of level one between min and max
                }

                if (level ==2){
                    maxSpeed = 20;
                    minSpeed = 15; //speed of level two between min and max
                }

                if (level ==3){
                    maxSpeed = 30;
                    minSpeed = 25; //speed of level three between min and max
                }

                int bound = (int) (maxSpeed * screenRatioX);
                bandit.speed = random.nextInt(bound); //random speed
                //bandit.speed = 1;

                if (bandit.speed < minSpeed * screenRatioX) //if speed is less than the min
                    bandit.speed = (int) (minSpeed * screenRatioX); //speed is min

                if (score == 15){
                    level = 2; //level up when score is 15
                }
                if (score == 25){
                    level =3; //level up when score is 25
                }

                bandit.x = screenX;
                bandit.y = random.nextInt(screenY - bandit.height); //position of bandit on random y
                //bandit.y = 700;

                bandit.wasShot = false;
            }

            if (Rect.intersects(bandit.getCollisionShape(), wizard.getCollisionShape())) {

                isGameOver = true;
                return; //ends game when bandit touches wizard
            }


        }

    }

    /**
     * draw background, bandits, wizard, and bullets
     */
    private void draw () {

        if (getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint); //both backgrounds


            for (Bandit bandit : banditList)
                canvas.drawBitmap(bandit.getBandit(), bandit.x, bandit.y, paint); //for all bandits, draw bandits

            canvas.drawText(score + "", screenX / 2f, 164, paint); //score text

            canvas.drawText("Level " + level, screenX /2f -80, 70, paint); //level text

            if (isGameOver) {
                isPlaying = false;
                canvas.drawBitmap(wizard.getDead(), wizard.x, wizard.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExiting ();
                return; //when the game is finished, pause, dead wizard, save score
            }

            canvas.drawBitmap(wizard.getMovement(), wizard.x, wizard.y, paint); //draw wizard

            for (Bullet bullet : bullets)
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y-100, paint); //draw bullets

            getHolder().unlockCanvasAndPost(canvas);

        }

    }

    /**
     *  sleep for a couple seconds before bringing back to menu
     */
    private void waitBeforeExiting() {

        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * save the users high score
     */
    private void saveIfHighScore() {

        if (prefs.getInt("highscore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }

    }

    /**
     * sleeps
     */
    private void sleep () {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * resume
     */
    public void resume () {

        isPlaying = true;
        thread = new Thread(this);
        thread.start();

    }

    /**
     * pause
     */
    public void pause () {

        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) { //when the user touches the screen

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX / 2) { //when the user touches the left side, move up
                    wizard.isGoingUp = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                wizard.isGoingUp = false;
                if (event.getX() > screenX / 2) //when the user touches the right side shoot
                    wizard.toShoot++;
                break;
        }

        return true;
    }

    /**
     * spawn bullet and sound
     */
    public void newBullet() {

        if (!prefs.getBoolean("isMute", false))
            soundPool.play(sound, 1, 1, 0, 0, 1); //sound of the bullet

        Bullet bullet = new Bullet(getResources());
        bullet.x = wizard.x + wizard.width;
        bullet.y = wizard.y + (wizard.height / 2); //spawn bullet relative to wizard position
        bullets.add(bullet);

    }
}
