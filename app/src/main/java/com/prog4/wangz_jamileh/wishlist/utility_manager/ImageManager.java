package com.prog4.wangz_jamileh.wishlist.utility_manager;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ImageManager {


    //TODO calculate the weight and height, to generate the smaller pic with original ratio
    public InputStream uriToFile(@NonNull Uri uri, Activity a) {
        InputStream inp = null;
        try {
            inp = a.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inp;
    }


    public Bitmap compressImage(InputStream imageStream, int h, int w) {
        Bitmap bmp = BitmapFactory.decodeStream(imageStream);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        Bitmap resized = Bitmap.createScaledBitmap(bmp, h, w, false);
//        byte[] byteArray = stream.toByteArray();
//        System.out.println("a: " + byteArray.length);
        try {
            stream.close();
            stream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resized;
    }


    public Bitmap compressImage(Bitmap image, int h, int w) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        Bitmap resized = Bitmap.createScaledBitmap(image, h, w, false);
//        byte[] byteArray = stream.toByteArray();
//        System.out.println("a: " + byteArray.length);
        try {
            stream.close();
            stream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resized;
    }

    public Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        Bitmap resized = Bitmap.createScaledBitmap(image, 125, 125, false);
//        byte[] byteArray = stream.toByteArray();
//        System.out.println("a: " + byteArray.length);
        try {
            stream.close();
            stream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resized;
    }

    public Bitmap compressImage(InputStream imageStream) {
        Bitmap bmp = BitmapFactory.decodeStream(imageStream);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        Bitmap resized = Bitmap.createScaledBitmap(bmp, 125, 125, false);
//        byte[] byteArray = stream.toByteArray();
//        System.out.println("a: " + byteArray.length);
        try {
            stream.close();
            stream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resized;
    }

    public Bitmap listToBitmap(ArrayList<? extends Number> list) {
        byte[] ary = new byte[list.size()];

        for (int i = 0, len = list.size(); i < len; i++) {
            ary[i] = (byte) (int) list.get(i).doubleValue();
        }

        return BitmapFactory.decodeByteArray(ary, 0, ary.length);
    }
}
