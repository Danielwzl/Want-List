package com.prog4.wangz_jamileh.wishlist.magic;

//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

public class HttpRequest {

    private final String USER_AGENT = "Mozilla/5.0";
//    https://auth0202-labile-psoriasis.cfapps.io/
//    http://10.0.2.2:3002
    private final String DEFAULT_URL = "https://auth0202-labile-psoriasis.cfapps.io";
    private CountDownLatch latch;
    public Map<String, Object> response;

//    public Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            Bundle data = msg.getData();
//            String res = data.getString("response");
//            Log.i("data", response);
//            response = res;
//        }
//    };

    public HttpRequest(CountDownLatch latch) {
        this.latch = latch;
    }


    public void post(final String link, final TreeMap<String, String> params, final InputStream filepath, final String filefield, final String fileMimeType, final String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                String body = generateParams(params);
                response = multipartPostRequest(link, params, filepath, filefield, fileMimeType, fileName);
                latch.countDown();
            }
        }).start();
    }

    public void post(final String link, final TreeMap<String, String> params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String body = generateParams(params);
                response = sendPost(link, body);
//                Message msg = new Message();
//                Bundle data = new Bundle();
//                data.putString("response", response);
//                msg.setData(data);
//                handler.sendMessage(msg);
                latch.countDown();
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
                response = sendGet(link, querystring);
                latch.countDown();
            }
        }).start();

    }

    // HTTP GET request
    private Map<String, Object> sendGet(String link, String querystring) {
        try {
            String url = DEFAULT_URL + link + "?" + querystring;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setConnectTimeout(3000);
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
    private Map<String, Object> sendPost(String link, String body) {
        try {
            String url = DEFAULT_URL + link;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setConnectTimeout(5000);
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

    private Map<String, Object> multipartPostRequest
            (String urlTo, TreeMap<String, String> params, InputStream file, String filefield, String fileMimeType, String fileName) {
        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024 * 1024;

//        String[] path = filepath.split("/");
//        int last = path.length - 1;

        try {
            //File file = new File(filepath);
            //FileInputStream fileInputStream = new FileInputStream(file);
            URL url = new URL(DEFAULT_URL + urlTo);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setConnectTimeout(10000);
            //upload file
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + fileName + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: " + fileMimeType + lineEnd);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
            outputStream.writeBytes(lineEnd);

            if(file != null) {
                bytesAvailable = file.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesRead = file.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    outputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = file.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = file.read(buffer, 0, bufferSize);
                }
                file.close();
            }
            outputStream.writeBytes(lineEnd);

            // Upload POST Data
            String key = null, value = null;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(value);
                outputStream.writeBytes(lineEnd);
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                return this.response = getResponse(connection);
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
        if(params.isEmpty()) return "";
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
    @SuppressWarnings("unchecked")
    private Map<String, Object> getResponse(HttpURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;

        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Gson gson = new Gson();
        Map<String, Object> map = new TreeMap<>();
        String jsonString = response.toString();
        if (!jsonString.equals("null"))
            map = (Map<String, Object>) gson.fromJson(jsonString, map.getClass());
        return map;
    }
}
