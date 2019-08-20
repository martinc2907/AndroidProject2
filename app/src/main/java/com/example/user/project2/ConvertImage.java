package com.example.user.project2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by user on 2018-01-03.
 */

public class ConvertImage extends AsyncTask<String, Void, Bitmap> {
    private ArrayList<String> array;
    public ConvertImage(ArrayList<String> array) {
        this.array = array;
    }

    protected Bitmap doInBackground(String... urls) {
        int position = Integer.parseInt(urls[0]);

        String urldisplay = urls[1];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
            array.add(position,encodeToBase64(mIcon11));
        } catch (Exception e) {
            Log.e("Error", "image download error");
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }


    protected void onPostExecute(Bitmap result) {
        //set image of your imageview
        //bmImage.setImageBitmap(result);
        //close
    }

    private static String encodeToBase64(Bitmap image){
        Bitmap imagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagex.compress(Bitmap.CompressFormat.PNG,15,baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        imageEncoded = imageEncoded.replaceAll("\n","");
        Log.e("TEST5",imageEncoded);
        return imageEncoded;
    }

}
