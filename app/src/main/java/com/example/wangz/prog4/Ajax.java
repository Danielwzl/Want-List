package com.example.wangz.prog4;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

//package com.example.wangz.prog4;
//
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.TreeMap;
//import java.util.Map;
//import java.net.HttpURLConnection;
////import javax.net.ssl.HttpsURLConnection;
//
//import android.os.AsyncTask;
//import android.util.Log;
//
public class Ajax {

    private final String USER_AGENT = "Mozilla/5.0";
//    public void post(String link, TreeMap<String, String> params) {
//        String[] setsOfInfo = new String[3];
//        setsOfInfo[0] = link;
//        setsOfInfo[1] = params != null ? generateParams(params) : "";
//        setsOfInfo[2] = "POST";
//        doInBackground(setsOfInfo);
//    }
//
//    public void get(String link, TreeMap<String, String> params) {
//
//        String[] setsOfInfo = new String[3];
//        setsOfInfo[0] = link;
//        setsOfInfo[1] = params != null ? generateParams(params) : "";
//        setsOfInfo[2] = "GET";
//        doInBackground(setsOfInfo);
//    }
//
//    @Override
//    protected Void doInBackground(String... params) {
//        try {
//
//            URL url = new URL(params[0]);
//            String urlParameters = params[1];
//            URLConnection connection = url.openConnection();
//            HttpURLConnection httpsConn = (HttpURLConnection) connection;
//            httpsConn.setAllowUserInteraction(false);
//            httpsConn.setInstanceFollowRedirects(true);
//            httpsConn.setRequestMethod(params[2]);
//            Log.i("test", "12321312312");
//            httpsConn.connect();
//            int resCode = httpsConn.getResponseCode();
//
//
////            connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
////            connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
////            connection.setDoOutput(true);
//
////            DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
////            dStream.writeBytes(urlParameters);
////            dStream.flush();
////
////            dStream.close();
////            int responseCode = connection.getResponseCode();
////            Log.i("test", responseCode + " ");
////            final StringBuilder output = new StringBuilder("Request URL " + url);
////            output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
////            output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
////            Object res = connection.getInputStream();
////            Log.i("test", res.toString());
////            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
////            String line = "";
////
////            StringBuilder responseOutput = new StringBuilder();
////            while ((line = br.readLine()) != null) {
////                responseOutput.append(line);
////            }
////            br.close();
//
////            output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
//
//        } catch (MalformedURLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private String generateParams(TreeMap<String, String> params) {
//        String paramString = "",
//                key = "",
//                value = "";
//
//        for (Map.Entry<String, String> entry : params.entrySet()) {
//            key = entry.getKey();
//            value = entry.getValue();
//            paramString += key + "=" + value + "&";
//        }
//
//        return paramString.substring(0, paramString.length() - 1);
//    }
//}

    public static void main(String[] args) throws Exception {

        Ajax http = new Ajax();

        System.out.println("Testing 1 - Send Http GET request");
        http.sendGet();

//        System.out.println("\nTesting 2 - Send Http POST request");
//        http.sendPost();

    }

    // HTTP GET request
    public void sendGet() {

        try {
            String url = "http://10.0.2.2:3002/test?id=1&name=daniel";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();

            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;

            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    // HTTP POST request
    private void sendPost() throws Exception {

        String url = "https://selfsolve.apple.com/wcResults.do";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }

}
