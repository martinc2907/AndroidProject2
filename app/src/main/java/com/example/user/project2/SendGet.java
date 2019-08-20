package com.example.user.project2;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 2018-01-02.
 */

public class SendGet extends AsyncTask<String, Void,String> {
    String data = "";

    @Override
    protected String doInBackground(String... params){
        //String data = "";

        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();

            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type","application/json");

            InputStream in = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(in);

            int inputStreamData = inputStreamReader.read();
            while(inputStreamData != -1){
                char current = (char) inputStreamData;
                inputStreamData = inputStreamReader.read();
                data += current;
            }
            in.close();
            inputStreamReader.close();
            Log.e("CHECK","Finishing Get..." + data);

        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
        }

        Log.e("CHECK", "Finished get");
        return data;
    }

    public String getData(){
        Log.e("CHECK","CHECKING getData");
        return data;
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }
}