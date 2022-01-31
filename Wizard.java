package com.example.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.myapplication.GameView.screenRatioX;
import static com.example.myapplication.GameView.screenRatioY;

public class Wizard {

    int toShoot = 0;
    boolean isGoingUp = false;
    int x, y, width, height, runCounter = 1, shootCounter = 1;
    Bitmap wizard1, wizard2, wizard3, wizard4, wizard5, wizard6, shoot1, shoot2, shoot3, shoot4, shoot5, dead;
    private GameView gameView;

    /**
     *
     * @param gameView the gameView
     * @param screenY size of the y
     * @param res resources
     */
    Wizard(GameView gameView, int screenY, Resources res) {

        this.gameView = gameView;

        wizard1 = BitmapFactory.decodeResource(res, R.drawable.wizard1);
        wizard2 = BitmapFactory.decodeResource(res, R.drawable.wizard2);
        wizard3 = BitmapFactory.decodeResource(res, R.drawable.wizard3);
        wizard4 = BitmapFactory.decodeResource(res, R.drawable.wizard4);
        wizard5 = BitmapFactory.decodeResource(res, R.drawable.wizard5);
        wizard6 = BitmapFactory.decodeResource(res, R.drawable.wizard6); //six frames of the wizards

        width = wizard1.getWidth();
        height = wizard1.getHeight(); //height and width of the wizard

        width /= 4;
        height /= 4;

        width = (int) (width * screenRatioX *5);
        height = (int) (height * screenRatioY *5); //height and width with the ratios

        wizard1 = Bitmap.createScaledBitmap(wizard1, width, height, false);
        wizard2 = Bitmap.createScaledBitmap(wizard2, width, height, false);
        wizard3 = Bitmap.createScaledBitmap(wizard3, width, height, false);
        wizard4 = Bitmap.createScaledBitmap(wizard4, width, height, false);
        wizard5 = Bitmap.createScaledBitmap(wizard5, width, height, false);
        wizard6 = Bitmap.createScaledBitmap(wizard6, width, height, false); //scale the wizard images

        shoot1 = BitmapFactory.decodeResource(res, R.drawable.shoot1);
        shoot2 = BitmapFactory.decodeResource(res, R.drawable.shoot2);
        shoot3 = BitmapFactory.decodeResource(res, R.drawable.shoot3);
        shoot4 = BitmapFactory.decodeResource(res, R.drawable.shoot4);
        shoot5 = BitmapFactory.decodeResource(res, R.drawable.shoot5); //five frames for attacking

        shoot1 = Bitmap.createScaledBitmap(shoot1, width, height, false);
        shoot2 = Bitmap.createScaledBitmap(shoot2, width, height, false);
        shoot3 = Bitmap.createScaledBitmap(shoot3, width, height, false);
        shoot4 = Bitmap.createScaledBitmap(shoot4, width, height, false);
        shoot5 = Bitmap.createScaledBitmap(shoot5, width, height, false); //scale attacking frams

        dead = BitmapFactory.decodeResource(res, R.drawable.dead); //dead image
        dead = Bitmap.createScaledBitmap(dead, width, height, false); //scale dead image

        y = screenY / 2;
        x = (int) (64 * screenRatioX);

    }

    /**
     *
     * @return movement frames and shooting frames
     */
    Bitmap getMovement() {

        if (toShoot != 0) {

            if (shootCounter == 1) {
                shootCounter++;
                return shoot1;
            }//shoot first frame

            if (shootCounter == 2) {
                shootCounter++;
                return shoot2;
            }//shoot second frame

            if (shootCounter == 3) {
                shootCounter++;
                return shoot3;
            }//shoot thrid frame

            if (shootCounter == 4) {
                shootCounter++;
                return shoot4;
            }//shoot fourth frame

            shootCounter = 1; //reset counter
            toShoot--;
            gameView.newBullet();

            return shoot5; //shoot fifth frame
        }

        if (runCounter == 1) {
            runCounter++;
            return wizard1;
        } //move first frame
        if (runCounter == 2) {
            runCounter++;
            return wizard2;
        }//move second frame
        if (runCounter == 3) {
            runCounter++;
            return wizard3;
        }//move third frame
        if (runCounter == 4) {
            runCounter++;
            return wizard4;
        }//move fourth frame
        if (runCounter == 5) {
            runCounter++;
            return wizard5;
        }//move fifth frame
        runCounter = 1; //reset counter

        return wizard6;//move sixth frame
    }

    /**
     *
     * @return collision shape
     */
    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    } //collision shape

    /**
     *
     * @return dead
     */
    Bitmap getDead () {
        return dead;
    } //if dead return dead

}
