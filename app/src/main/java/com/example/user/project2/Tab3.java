package com.example.user.project2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by user on 2017-12-31.
 */

public class Tab3 extends Fragment implements View.OnClickListener {

    public ListView listViews;

    public TextView textView;

    public ArrayList<String> food_list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3, container, false);

        listViews = (ListView)rootView.findViewById(R.id.delivery);

        Button all_menu = (Button)rootView.findViewById(R.id.all_food);
        Button chicken = (Button)rootView.findViewById(R.id.chicken);
        Button pizza = (Button)rootView.findViewById(R.id.pizza);
        Button etc = (Button)rootView.findViewById(R.id.etc);

        all_menu.setOnClickListener(this);
        chicken.setOnClickListener(this);
        pizza.setOnClickListener(this);
        etc.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        // default method for handling onClick Events..
        switch (v.getId()){
            case R.id.all_food:
                new GETTask().execute("all_food");
                break;
            case R.id.chicken:
                new GETTask().execute("chicken");
                break;
            case R.id.pizza:
                new GETTask().execute("pizza");
                break;
            case R.id.etc:
                new GETTask().execute("etc");
                break;
        }
    }

    public class GETTask extends AsyncTask<String, Integer ,JSONArray> {

        @Override
        protected JSONArray doInBackground(String... strings) {
            try {
                HttpURLConnection con = null;
                BufferedReader reader = null;
                try{
                    URL url = new URL("http://52.78.103.222:52273/" + strings[0]);
                    con = (HttpURLConnection) url.openConnection();
                    con.connect();

                    InputStream stream = con.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                    StringBuilder responseStrBuilder = new StringBuilder();
                    String inputStr;
                    while ((inputStr = streamReader.readLine()) != null)
                        responseStrBuilder.append(inputStr);

                    JSONArray jsonObject = new JSONArray(responseStrBuilder.toString());
                    return jsonObject;

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

        //String type, String name, String number, String cash_only

        @Override
        protected void onPostExecute(JSONArray result) {

            tab3_adapter mAdapter = new tab3_adapter();

            super.onPostExecute(result);
            Log.d("json object?",result.toString());

            for(int i = 0; i < result.length(); i++){
                try {
                    JSONObject jsonObject = result.getJSONObject(i);
                    String type = jsonObject.getString("type");
                    String name = jsonObject.getString("name");
                    String number = jsonObject.getString("number");
                    String only_cash = jsonObject.getString("only_cash");
                    mAdapter.addItem(type,name,number,only_cash);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            listViews.setAdapter(mAdapter);

        }
    }

}
