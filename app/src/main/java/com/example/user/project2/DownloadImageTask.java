package com.example.user.project2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by user on 2018-01-01.
 */

public class DownloadImageTask extends AsyncTask<String, Activity, Bitmap> {

    private ImageView bmImage;
    private Activity act;

    public DownloadImageTask(ImageView bmImage, Activity act) {
        this.bmImage = bmImage;
        this.act = act;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];

        if(urls[0].equals("")){
            return BitmapFactory.decodeResource(act.getResources(),R.drawable.einstein);
        }
        Bitmap mIcon11 = decodeBase64(urls[0]);
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        //set image of your imageview
        bmImage.setImageBitmap(result);
        //close
    }

    public Bitmap decodeBase64(String input)
    {
        Log.e("LENGTH",Integer.toString(input.length()) + input);
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
