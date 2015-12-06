package com.shopchat.consumer.network;

import android.support.v4.util.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sourin on 03/12/15.
 */
public class HttpPostClient {

    private String url;
    private String payload;
    private HashMap<String, String> headers = new HashMap<String, String>();

    private int responseCode;
    private String response;

    public HttpPostClient() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean sendPostRequest(){

        if(this.getUrl() == null){
            return false;
        }else{
            try{

                URL url = new URL(this.getUrl());
                HttpURLConnection mHttpURLConnection = (HttpURLConnection) url.openConnection();
                mHttpURLConnection.setDoOutput(true);
                mHttpURLConnection.setRequestMethod("POST");
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    mHttpURLConnection.setRequestProperty(key, value);
                }

                // Write data
                OutputStream outputStream = mHttpURLConnection.getOutputStream();
                outputStream.write(payload.getBytes());

                // Read response
                StringBuilder responseSB = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(mHttpURLConnection.getInputStream()));

                String line;
                while ( (line = bufferedReader.readLine()) != null)
                    responseSB.append(line);

                // Close streams
                bufferedReader.close();
                outputStream.close();

                this.responseCode = mHttpURLConnection.getResponseCode();
                this.response = responseSB.toString();

                return true;

            }catch (Exception e){
                e.printStackTrace();
                this.responseCode = 0;
                this.response = "Bad Request";
                return false;
            }

        }

    }

}
