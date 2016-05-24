package com.ldoublem.rate.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lumingmin on 16/5/24.
 */
public class BitmapUtil {


    public static Bitmap getImageFromAssetsFile(Context c, String fileName) {
        Bitmap image = null;
        AssetManager am = c.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }

}
