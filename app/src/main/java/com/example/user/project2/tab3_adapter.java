package com.example.user.project2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 2018-01-04.
 */

public class tab3_adapter extends BaseAdapter {

    private ArrayList<tab3_element> mItems = new ArrayList<tab3_element>();

    @Override
    public int getCount(){
        return mItems.size();
    }

    @Override
    public tab3_element getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent){

        tab3_adapter mAdapter = new tab3_adapter();

        Context context = parent.getContext();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tab3_listview_element, parent, false);
        }

        ImageView iv_img = (ImageView) convertView.findViewById(R.id.food) ;
        TextView store_name = (TextView) convertView.findViewById(R.id.name);
        TextView cash_only = (TextView) convertView.findViewById(R.id.cash);
        TextView number = (TextView)convertView.findViewById(R.id.number);


        if(getItem(position).gettype().equals("chicken")){
            iv_img.setImageResource(R.drawable.chicken);

            //iv_img.setImageDrawable(R.drawable.chicken);
        }
        else if (getItem(position).gettype().equals("pizza")){
            iv_img.setImageResource(R.drawable.pizza);
        }
        else{
            Drawable myDrawable = context.getResources().getDrawable(R.drawable.etc);
            iv_img.setImageDrawable(myDrawable);
        }

        store_name.setText(getItem(position).getname());

        if(getItem(position).getcash_only().equals("true")){
            cash_only.setText("현금만 사용이 가능합니다. 현금 결제를 부탁드립니다.");
        }
        else{
            cash_only.setText("현금, 카드 모두 사용 가능");
        }

        number.setText(getItem(position).getnumber());

        return convertView;
    }

    public void addItem(String type, String name, String number, String cash_only){

        tab3_element mItem = new tab3_element();

        mItem.settype(type);
        mItem.setname(name);
        mItem.setnumber(number);
        mItem.setcash_only(cash_only);

        mItems.add(mItem);

    }


}
