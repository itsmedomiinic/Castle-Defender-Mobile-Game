package com.example.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.myapplication.GameView.screenRatioX;
import static com.example.myapplication.GameView.screenRatioY; //takes the ratios

public class Bandit {

    public int speed = 1;
    public boolean wasShot = true;
    int x = 0, y, width, height, banditCounter = 1; //variables
    Bitmap bandit1, bandit2, bandit3, bandit4; //bitmaps of the bandit

    /**
     *
     * @param res resources
     */
    Bandit(Resources res) {

        bandit1 = BitmapFactory.decodeResource(res, R.drawable.bandit1);
        bandit2 = BitmapFactory.decodeResource(res, R.drawable.bandit2);
        bandit3 = BitmapFactory.decodeResource(res, R.drawable.bandit3);
        bandit4 = BitmapFactory.decodeResource(res, R.drawable.bandit4); //4 frames of the bandits

        width = bandit1.getWidth();
        height = bandit1.getHeight(); //size of the bandits

        width /= 6;
        height /= 6;

        width = (int) (width * screenRatioX *15);
        height = (int) (height * screenRatioY *15); //size of the bandits in the same ratio

        bandit1 = Bitmap.createScaledBitmap(bandit1, width, height, false);
        bandit2 = Bitmap.createScaledBitmap(bandit2, width, height, false);
        bandit3 = Bitmap.createScaledBitmap(bandit3, width, height, false);
        bandit4 = Bitmap.createScaledBitmap(bandit4, width, height, false); //scale all the bandits

        y = -height;
    }

    /**
     *
     * @return bandit movement frames
     */
    Bitmap getBandit() {

        if (banditCounter == 1) {
            banditCounter++;
            return bandit1;
        } //first frame

        if (banditCounter == 2) {
            banditCounter++;
            return bandit2;
        }//second frame

        if (banditCounter == 3) {
            banditCounter++;
            return bandit3;
        }//thrid frame

        banditCounter = 1;//reset counter

        return bandit4; //fourth frames
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    } //the limit to touch collision shape

}
