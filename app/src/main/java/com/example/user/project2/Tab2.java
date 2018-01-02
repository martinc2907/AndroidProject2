package com.example.user.project2;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.*;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by user on 2017-12-31.
 */

public class Tab2 extends Fragment {

    private static int PICK_IMAGE_REQUEST = 1;

    public String data_link;

    public ArrayList<String> image_list = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.tab2, container, false);
        final Button load_image = (Button)rootView.findViewById(R.id.load_image);
        Button  upload = (Button)rootView.findViewById(R.id.upload);
        load_image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //need loading
                new GETTask().execute();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImagefromGallery(rootView);
            }
        });
        return rootView;
    }

    public void loadImagefromGallery(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        try{
            if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data){
                Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
                bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/3,bitmap.getHeight()/3,true);
                String encodedImage = load_getStringFromBitmap(bitmap);
                Log.d("encodedImage" , encodedImage);
                data_link = encodedImage;
                new JSONTask().execute(data_link);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public class JSONTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... link){
            try {
                if(link == null){
                    return null;
                }
                Log.d("linknknknknknknknk", link[0]);
                URL url = new URL("http://52.78.103.222:52273/upload");
//                Bitmap bm = BitmapFactory.decodeFile(link[0]);
//                String encodedImage = getStringFromBitmap(bm);

                String encodedImage = link[0];

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "application/json");

                conn.setReadTimeout(35000);
                conn.setConnectTimeout(35000);

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", "TEST");
                jsonObject.accumulate("image", encodedImage);


                OutputStream os = conn.getOutputStream();

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
                Log.d("stringed object ", jsonObject.toString());
                Log.d("conn ", conn.toString());
                writer.write(jsonObject.toString());
                writer.flush();
                writer.close();
                System.out.println("Response Code: " + conn.getResponseCode());

                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = responseStreamReader.readLine()) != null)
                    stringBuilder.append(line).append("\n");
                responseStreamReader.close();

                String response = stringBuilder.toString();
                System.out.println(response);

                conn.disconnect();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

        }
        private String getStringFromBitmap(Bitmap bitmapPicture) {
            String encodedImage;
            ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
            bitmapPicture.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);
            byte[] b = byteArrayBitmapStream.toByteArray();
            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            return encodedImage;
        }
    }

    private String load_getStringFromBitmap(Bitmap bitmapPicture) {
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, 100, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }


    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);

    }

    public class GETTask extends AsyncTask<String, String ,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", "androidTest");
                jsonObject.accumulate("name", "yun");

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    URL url = new URL("http://52.78.103.222:52273/upload");
                    con = (HttpURLConnection) url.openConnection();
                    con.connect();

                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while((line = reader.readLine()) != null){
                        Log.d("line is this", line);
                        buffer.append(line);
                        if(line.contains("image")){
                            image_list.add(line);
                        }
                    }
                    return buffer.toString();

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("get data", result);
            for(int i= 0; i<image_list.size(); i++){
                Log.d("element well? ", image_list.get(i));
            }
        }

    }
}
