package com.example.user.project2;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 2018-01-01.
 */

public class CustomListAdapter extends ArrayAdapter {
    int[] colors =new int[50];
    int start = 0xffff6b6b;
    //{0xffff6b6b, 0xffef6b6b, 0xffcf6b6b, 0xffaf6b6b, 0xff8f6b6b, 0xff6f6b6b, 0xff4f6b6b};
    int[] colors2 = {0xffC62828, 0xffD32F2F, 0xffE53935, 0xffF44336, 0xffEF5350, 0xffE57373, 0xffEF9A9A};
    private final Activity context;
    private final ArrayList<String> urlArray;   //array of URLs later.
    private final ArrayList<String> nameArray;
    private final ArrayList<String> phoneArray;

    public CustomListAdapter(Activity context, ArrayList<String> nameArrayParam, ArrayList<String> phoneArrayParam, ArrayList<String> urlArray){

        super(context,R.layout.listview_row , nameArrayParam);

        for(int i = 0; i < 50; i ++){
            colors[i] = start;
            start -= 0x00200000;
        }
        this.context=context;
        this.urlArray = urlArray;
        this.nameArray = nameArrayParam;
        this.phoneArray = phoneArrayParam;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = (TextView) rowView.findViewById(R.id.name);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.info);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.custom_dp);

        //this code sets the values of the objects to values from the arrays
        nameTextField.setText(nameArray.get(position));
        infoTextField.setText(phoneArray.get(position));
        //new DownloadImageTask(imageView).execute(urlArray.get(position));


        rowView.setBackgroundColor(colors[position]);


        return rowView;
    };

}