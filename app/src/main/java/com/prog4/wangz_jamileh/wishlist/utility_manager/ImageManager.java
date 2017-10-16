package com.prog4.wangz_jamileh.wishlist.utility_manager;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class ImageManager {


    public InputStream uriToFile(@NonNull Uri uri, Activity a){
        InputStream inp = null;
        try {
            inp =  a.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inp;
    }

    public Bitmap listToBitmap(ArrayList<? extends Number> list){
        byte[] ary = new byte[list.size()];

        for(int i =0, len = list.size(); i < len; i++){
            ary[i] = (byte)(int)list.get(i).doubleValue();
        }

        return BitmapFactory.decodeByteArray(ary , 0, ary.length);
    }
}
