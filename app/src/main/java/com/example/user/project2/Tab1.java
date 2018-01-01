package com.example.user.project2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * Created by user on 2017-12-31.
 */

public class Tab1 extends Fragment {

    LoginButton loginButton;
    CallbackManager callbackManager;
    TextView textView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1, container, false);



        callbackManager  = CallbackManager.Factory.create();
        loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
        // If using in a fragment
        loginButton.setFragment(this);


        textView = rootView.findViewById(R.id.email);


        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback(){
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response){
                        String jsonTest = null;
                        JSONArray jsonArrayFriends = null;
                        StringBuilder names = new StringBuilder();

                        try {
                            object = object.getJSONObject("taggable_friends");
                            jsonArrayFriends = object.getJSONArray("data");
                            int length  = jsonArrayFriends.length();
                            for(int i = 0; i< length; i++){
                                names.append(jsonArrayFriends.getJSONObject(i).getString("name"));
                                names.append("\n\n");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        textView.setText(names.toString());
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields","first_name, last_name,taggable_friends" );
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

