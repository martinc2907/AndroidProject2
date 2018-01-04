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
    int[] colors2 = {0xff19547b,0xff2c5f7e,0xff3f6a80,0xff527583,0xff668086,0xff798b88,0xff8c968b,0xff9fa18e,0xffb2ac90,0xffc5b793,0xffd9c296,0xffeccd98,0xffffd89b};

    private final Activity context;
    private final ArrayList<String> urlArray;   //array of URLs later.
    private final ArrayList<String> nameArray;
    private final ArrayList<String> phoneArray;

    public CustomListAdapter(Activity context, ArrayList<String> nameArrayParam, ArrayList<String> phoneArrayParam, ArrayList<String> urlArray){

        super(context,R.layout.listview_row , nameArrayParam);
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

        rowView.setBackgroundColor(colors2[position]);

        return rowView;
    };

}