package com.example.user.project2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import static com.example.user.project2.Tab2.bitmaps_list; ///

/**
 * Created by user on 2017-12-28.
 */

public class ItemClick extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagefrag);

        Intent i = getIntent();

        int link = (int) i.getExtras().get("id");

        Bitmap bm = bitmaps_list.get(link); //

        ImageView imageView = (ImageView)findViewById(R.id.image);
        imageView.setImageBitmap(bm);
    }

}
