package com.example.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {

    int x = 0, y = 0;
    Bitmap background; //bitmap

    /**
     *
     * @param screenX size of the x
     * @param screenY size of the y
     * @param res resources
     */
    Background (int screenX, int screenY, Resources res) {

        background = BitmapFactory.decodeResource(res, R.drawable.background); //background image
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false); //scale

    }

}
