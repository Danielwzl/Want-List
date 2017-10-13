package com.prog4.wangz_jamileh.wishlist.magic;

//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;

import android.util.Log;

import com.google.gson.Gson;

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

public class HttpRequest {

    private final String USER_AGENT = "Mozilla/5.0";
    private final String DEFAULT_URL = "http://10.0.2.2:3002";
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


    public void post(final String link, final TreeMap<String,String> params, final String filepath, final String filefield, final String fileMimeType){
        new Thread(new Runnable() {
            @Override
            public void run() {
//                String body = generateParams(params);
                response = multipartPostRequest(link, params, filepath, filefield, fileMimeType);
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

    private Map<String, Object> multipartPostRequest(String urlTo, TreeMap<String, String> params, String filepath, String filefield, String fileMimeType){
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;

        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        String result = "";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        String[] q = filepath.split("/");
        int idx = q.length - 1;

        try {
            File file = new File(filepath);
            FileInputStream fileInputStream = new FileInputStream(file);

            URL url = new URL(urlTo);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            //Send file
            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + q[idx] + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: " + fileMimeType + lineEnd);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);

            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);

            // Upload POST Data
            Iterator<String> keys = params.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = params.get(key);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(value);
                outputStream.writeBytes(lineEnd);
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


            if (200 != connection.getResponseCode()) {
                throw new CustomException("Failed to upload code:" + connection.getResponseCode() + " " + connection.getResponseMessage());
            }

            inputStream = connection.getInputStream();

            result = this.convertStreamToString(inputStream);

            fileInputStream.close();
            inputStream.close();
            outputStream.flush();
            outputStream.close();

            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
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
