package com.prog4.wangz_jamileh.wishlist.magic;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

public class Ajax {

    private CountDownLatch latch;
    private HttpRequest http;
    public Ajax(){
        latch = new CountDownLatch(1);
        http = new HttpRequest(latch);
    }

    public void get(String link){
        http.get(link);
        try{
            latch.await();
        }
        catch(InterruptedException e){}
    }

    public void get(String link, TreeMap<String, String> params){
        http.get(link, params);
        try{
            latch.await();
        }
        catch(InterruptedException e){}
    }

    public void post(String link, TreeMap<String,String> params, InputStream filepath, String filefield, String fileMimeType, String fileName){
        http.post(link, params, filepath, filefield, fileMimeType, fileName);
        try{
            latch.await();
        }
        catch(InterruptedException e){}
    }

    public void post(String link, TreeMap<String, String> params){
        http.post(link, params);
        try{
            latch.await();
        }
        catch(InterruptedException e){}
    }

    public Map<String, Object> response(){
        return http.response;
    }
}
