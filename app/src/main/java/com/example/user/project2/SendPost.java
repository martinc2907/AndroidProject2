package com.example.user.project2;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 2018-01-02.
 */

public class SendPost extends AsyncTask<String, Void,String> {
    @Override
    protected String doInBackground(String... params){
        HttpURLConnection  httpURLConnection = null;
        String data = "";
        try {
            httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            os.write(params[1].getBytes());
            //DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            //wr.writeBytes(params[1]);
            //wr.flush();
            //wr.close();
            os.flush();
            os.close();

            InputStream in = httpURLConnection.getInputStream();
            //InputStreamReader inputStreamReader = new InputStreamReader(in);
            Reader r1 = new BufferedReader(new InputStreamReader(in),2000);

            int inputStreamData = r1.read();
            while (inputStreamData != -1) {
                char current = (char) inputStreamData;
                inputStreamData = r1.read();
                data += current;
            }
            in.close();
            r1.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        //httpURLConnection.disconnect();
        return data;
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }
}