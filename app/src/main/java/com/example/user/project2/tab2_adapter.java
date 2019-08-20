package com.example.user.project2;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by user on 2018-01-03.
 */

public class tab2_adapter extends BaseAdapter {

    private ArrayList<gallery_item> mItems = new ArrayList<>();

    public static ArrayList<String> links = new ArrayList<String>();

    @Override
    public int getCount(){
        return mItems.size();
    }

    @Override
    public gallery_item getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Context context = parent.getContext();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gridview_element, parent, false);
        }
        ImageView iv_img = (ImageView) convertView.findViewById(R.id.image) ;
        gallery_item myItem = getItem(position);
        iv_img.setImageBitmap(myItem.getBm());
        return convertView;
    }

    public void addItem(Bitmap bm){
        gallery_item mItem = new gallery_item();

        mItem.setBm(bm);

        mItems.add(mItem);

    }
}


