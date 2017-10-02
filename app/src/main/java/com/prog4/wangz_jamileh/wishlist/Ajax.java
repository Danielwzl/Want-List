package com.prog4.wangz_jamileh.wishlist;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

public class Ajax {

    private final String USER_AGENT = "Mozilla/5.0";
    private final String DEFAULT_URL = "http://10.0.2.2:3002";
    private CountDownLatch latch;
    public String response;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String res = data.getString("response");
            Log.i("data", response);
            response = res;
        }
    };

    public Ajax(CountDownLatch latch){
        this.latch = latch;
    }

    public void post(final String link, final TreeMap<String, String> params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String body = generateParams(params);
                String response = sendPost(link, body);
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("response", response);
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }).start();
    }


    public void get(final String link) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                response = sendGet(link, "");
                latch.countDown();
            }
        }).start();
    }

    public void get(final String link, final TreeMap<String, String> params) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String querystring = generateParams(params);
                sendGet(link, querystring);
            }
        }).start();

    }

    // HTTP GET request
    private String sendGet(String link, String querystring) {
            try {
                String url = DEFAULT_URL + link + "?" + querystring;

                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                // optional default is GET
                con.setRequestMethod("GET");

                //add request header
                con.setRequestProperty("User-Agent", USER_AGENT);

                int responseCode = con.getResponseCode();

                System.out.println("\nSending 'GET' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);

                if (responseCode == 200) {
                    return this.response = getResponse(con);
                } else {
                    Log.i("err", "request failed due to 301, 404 or 500");
                    return this.response = null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return this.response = null;
    }

    // HTTP POST request
    private String sendPost(String link, String body) {
            try {
                String url = DEFAULT_URL + link;
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                // Send post request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(body);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + body);
                System.out.println("Response Code : " + responseCode);
                if (responseCode == 200) {
                    return this.response = getResponse(con);
                } else {
                    Log.i("err", "request failed due to 301, 404 or 500");
                    return this.response = null;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return this.response = null;
    }

    /**
     * generate the query string or post request body
     *
     * @param params
     * @return
     */
    private String generateParams(TreeMap<String, String> params) {
        String paramString = "",
                key = "",
                value = "";

        for (Map.Entry<String, String> entry : params.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            paramString += key + "=" + value + "&";
        }

        return paramString.substring(0, paramString.length() - 1);
    }

    private String getResponse(HttpURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;

        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}
