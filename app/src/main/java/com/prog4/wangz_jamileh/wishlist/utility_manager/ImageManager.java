package com.prog4.wangz_jamileh.wishlist.utility_manager;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;

import com.prog4.wangz_jamileh.wishlist.magic.Ajax;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;


public class ImageManager {

    private Activity a;

    public ImageManager(Activity a) {
        if (this.a == null) this.a = a;
    }

    public Map<String, Object> uploadImage(Uri selectedImage) {
        InputStream image = uriToFile(selectedImage);
        Ajax a = new Ajax();
        String fileField = "image",
                mimeType = "image/jpeg",
                fileName = "avatar";
        a.post("/file", null, image, fileField, mimeType, fileName);
        return a.response();
    }


    //TODO calculate the weight and height, to generate the smaller pic with original ratio
    public InputStream uriToFile(Uri uri) {
        InputStream inp = null;
        try {
            inp = a.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inp;
    }


    public Bitmap compressImage(Uri selectedImage, int h, int w, int q) {
        InputStream imageStream = uriToFile(selectedImage);
        Bitmap bmp = BitmapFactory.decodeStream(imageStream);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, q, stream);
        Bitmap resized = resize(w, h, bmp);
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


    public Bitmap compressImage(Bitmap image, int h, int w, int q) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, q, stream);
        Bitmap resized = resize(w, h, image);
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
        Bitmap resized = resize(768, 1024, image);
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

    public Bitmap compressImage(Uri selectedImage) {
        InputStream imageStream = uriToFile(selectedImage);
        Bitmap bmp = BitmapFactory.decodeStream(imageStream);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        Bitmap resized = resize(768, 1024, bmp);
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

    public InputStream Bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    public Drawable bitmapToDrawable(Resources res, Bitmap img){
        return new BitmapDrawable(res, img);
    }

    private Bitmap resize(int tw, int th, Bitmap img){
        int w = img.getWidth(), h = img.getHeight();
        float r = (float) w / (float) h;
        int finalWidth = tw;
        int finalHeight = th;
        if (r < 1) {
            finalWidth = (int) ((float) tw * r);
        } else {
            finalHeight = (int) ((float) th / r);
        }
        return Bitmap.createScaledBitmap(img, finalWidth, finalHeight, true);
    }
}
