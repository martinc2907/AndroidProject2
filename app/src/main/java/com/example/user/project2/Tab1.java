package com.example.user.project2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by user on 2017-12-31.
 */

public class Tab1 extends Fragment {

    LoginButton loginButton;
    CallbackManager callbackManager;
    TextView textView;
    ListView listView;
    CustomListAdapter listAdapter;
    ArrayList<String> nameArray = new ArrayList<String>();
    ArrayList<String> urlArray = new ArrayList<String>();
    ArrayList<String> infoArray = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1, container, false);

        callbackManager  = CallbackManager.Factory.create();
        loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
        //textView = rootView.findViewById(R.id.email);
        listView = rootView.findViewById(R.id.listview);

        // If using in a fragment
        loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request =  GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback(){
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response){
                        JSONArray jsonArrayFriends = null;
                        String jsonTest = null;
                        String picURL = null;
                        try {
                            object = object.getJSONObject("taggable_friends");
                            //jsonTest = object.toString();
                            jsonArrayFriends = object.getJSONArray("data");
                            int length  = jsonArrayFriends.length();
                            for(int i = 0; i< length; i++){
                                object = jsonArrayFriends.getJSONObject(i);
                                nameArray.add(object.getString("name"));

                                object = object.getJSONObject("picture");
                                object = object.getJSONObject("data");
                                urlArray.add(object.getString("url"));

                                infoArray.add("No Phone Number");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //textView.setText(jsonTest);
                        //new DownloadImageTask(imageView).execute(picURL);
                        listAdapter = new CustomListAdapter(getActivity(),nameArray,infoArray,urlArray);
                        listView.setAdapter(listAdapter);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields","first_name, last_name,taggable_friends, friendlists" );
                request.setParameters(parameters);
                request.executeAsync();
            }


            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

/*
    private void myNewGraphReq(String friendlistId){
        final String graphPath = "/" + friendlistId + "/members/";
        AccessToken token = AccessToken.getCurrentAccessToken();
        GraphRequest request = new GraphRequest(token, graphPath, null, HttpMethod.GET, new GraphRequest.)

    }
*/
}

