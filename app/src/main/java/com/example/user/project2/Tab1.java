package com.example.user.project2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * Created by user on 2017-12-31.
 */

public class Tab1 extends Fragment{

    LoginButton loginButton;
    CallbackManager callbackManager;
    TextView textView;
    ListView listView;
    CustomListAdapter listAdapter;
    ArrayList<String> nameArray = new ArrayList<String>();
    ArrayList<String> urlArray = new ArrayList<String>();
    ArrayList<String> picArray = new ArrayList<String>();
    ArrayList<String> idArray = new ArrayList<String>();
    ArrayList<String> phoneArray = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1, container, false);

/*
            JSONObject json;
            String a = JSONObject.quote("한글+a<.../>kk12_{}");
            JSONObject
            json = new JSONObject();
            Log.e("TAG", "TESTING JSON"+json.toString());
*/


        callbackManager  = CallbackManager.Factory.create();
        loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
        textView = rootView.findViewById(R.id.email);
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


                        textView.setText("Tap Friend to Retrieve Data From Server");

                        try {
                            object = object.getJSONObject("taggable_friends");
                            jsonTest = object.toString();
                            jsonArrayFriends = object.getJSONArray("data");
                            int length  = jsonArrayFriends.length();
                            for(int i = 0; i< length; i++){

                                object = jsonArrayFriends.getJSONObject(i);
                                nameArray.add(object.getString("name"));
                                idArray.add(object.getString("id"));
                                phoneArray.add("Not Updated");
                                //picArray.add("null");   later add bitmap's string

                                object = object.getJSONObject("picture");
                                object = object.getJSONObject("data");
                                urlArray.add(object.getString("url"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        listAdapter = new CustomListAdapter(getActivity(),nameArray,phoneArray,urlArray);
                        listView.setAdapter(listAdapter);

                        //Communicate with server.
                        JSONObject postData = new JSONObject();
                        JSONArray friendArray = new JSONArray();
                        try{
                            for (int i = 0; i < nameArray.size(); i++) {
                                JSONObject friend = new JSONObject();
                                friend.put("name", nameArray.get(i));
                                //friend.put("id",idArray.get(i));
                                friend.put("id","null");
                                friend.put("phone", "No Phone Number");
                                friend.put("url", urlArray.get(i));

                                //Add to posting JSONObject.
                                friendArray.put(friend);
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SendPost send = new SendPost();
                        send.execute("http://52.78.103.222:8080/", friendArray.toString());

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                ImageView imageView =view.findViewById(R.id.custom_dp);
                                TextView textView = view.findViewById(R.id.info);
                                TextView nameView = view.findViewById(R.id.name);

                                JSONObject postData = new JSONObject();
                                try {
                                    postData.put("name", nameArray.get(position));
                                    //Put other fields for search later(when name could overlap).
                                    //postData.put("id", idArray.get(position));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                String name = nameArray.get(position);
                                String url = "http://52.78.103.222:8080/";

                                Uri.Builder b = Uri.parse(url).buildUpon().appendQueryParameter("name",name);

                                SendGet send = new SendGet();
                                try {
                                    send.execute(b.toString(), postData.toString()).get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                                Log.e("CHECK","CHECK RETURN STRING" +send.getData());

                                String data = send.getData();
                                JSONArray json = null;
                                try {
                                    json = new JSONArray(data);
                                    JSONObject temp = json.getJSONObject(0);
                                    textView.setText( temp.get("phone").toString());
                                    nameView.setText(temp.get("name").toString());
                                    new DownloadImageTask(imageView).execute(temp.get("url").toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                /*
                                try {
                                    Log.e("CHECK", send.get());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                                */
                                /*
                                if(urlArray.get(position).equals("null"))
                                    imageView.setImageResource(R.drawable.einstein);
                                textView.setText("010-0000-0000");
                                */
                            }
                        });
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

