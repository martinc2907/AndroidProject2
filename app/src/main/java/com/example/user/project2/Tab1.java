package com.example.user.project2;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.concurrent.ExecutionException;


/**
 * Created by user on 2017-12-31.
 */

public class Tab1 extends Fragment{
    boolean fbFlag = false;
    int indexOffset = 0;
    LoginButton loginButton;
    CallbackManager callbackManager;
    TextView textView;
    ListView listView;
    CustomListAdapter listAdapter;
    Button contactButton;
    ArrayList<String> nameArray = new ArrayList<String>();
    ArrayList<String> urlArray = new ArrayList<String>();
    ArrayList<String> picArray = new ArrayList<String>();   //Base64 encoded strings of pics.
    ArrayList<String> idArray = new ArrayList<String>();
    ArrayList<String> phoneArray = new ArrayList<String>();
    ArrayList<String> defaultPhoneArray = new ArrayList<String>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1, container, false);

        callbackManager  = CallbackManager.Factory.create();
        loginButton = (LoginButton) rootView.findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));


        textView = rootView.findViewById(R.id.email);
        listView = rootView.findViewById(R.id.listview);
        contactButton = rootView.findViewById(R.id.contact);

        EnableRuntimePermission();
        //Log.e("PHONE", "WTF" + find_number_in_phone().toString());

        // If using in a fragment
        loginButton.setFragment(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
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
                    defaultPhoneArray.set(position,temp.get("phone").toString());
                    //nameView.setText(temp.get("name").toString());
                    Log.e("HALAL",temp.get("pic").toString());
                    new DownloadImageTask(imageView, getActivity()).execute(temp.get("pic").toString());
                } catch (JSONException e) {
                    Log.e("HALAL","ERROR");
                    e.printStackTrace();
                }
            }
        });

        contactButton.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v){

                if(indexOffset !=0)
                    return;

                find_number_in_phone();

                listAdapter = new CustomListAdapter(getActivity(),nameArray,defaultPhoneArray,urlArray);

                listView.setAdapter(listAdapter);


                //Communicate with server.
                JSONArray friendArray = new JSONArray();
                //Toast.makeText(getActivity(),nameArray.toString(), Toast.LENGTH_LONG ).show();
                try{
                    for (int i = 0; i < nameArray.size(); i++) {
                        indexOffset++;
                        JSONObject friend = new JSONObject();
                        friend.put("name", nameArray.get(i));
                        //friend.put("id",idArray.get(i));
                        friend.put("id","null");
                        friend.put("phone", phoneArray.get(i));
                        friend.put("url", urlArray.get(i));
                        friend.put("pic",picArray.get(i));
                        //Add to posting JSONObject.
                        friendArray.put(friend);
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                SendPost send = new SendPost();
                try {
                    send.execute("http://52.78.103.222:8080/", friendArray.toString()).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request =  GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback(){
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response){

                        fbFlag = true;
                    JSONArray jsonArrayFriends = null;
                    String jsonTest = null;
                    String picURL = null;
                    //textView.setText("Tap Friend to Retrieve Data From Server");

                    try {
                        object = object.getJSONObject("taggable_friends");
                        jsonTest = object.toString();
                        jsonArrayFriends = object.getJSONArray("data");
                        int length  = jsonArrayFriends.length();
                        for(int i = 0; i< length; i++){

                            object = jsonArrayFriends.getJSONObject(i);
                            nameArray.add(object.getString("name"));
                            idArray.add(object.getString("id"));
                            phoneArray.add("No Phone Number");
                            defaultPhoneArray.add("");

                            object = object.getJSONObject("picture");
                            object = object.getJSONObject("data");
                            urlArray.add(object.getString("url"));

                            String index = Integer.toString(i+indexOffset);
                            String temp = object.getString("url");
                            try {
                                new ConvertImage(picArray).execute(index,temp).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    listAdapter = new CustomListAdapter(getActivity(),nameArray,defaultPhoneArray,urlArray);
                    listView.setAdapter(listAdapter);


                    //Communicate with server.
                    JSONArray friendArray = new JSONArray();
                    try{
                        for (int i = 0; i < nameArray.size(); i++) {
                            JSONObject friend = new JSONObject();
                            friend.put("name", nameArray.get(i));
                            //friend.put("id",idArray.get(i));
                            friend.put("id","null");
                            friend.put("phone", "No Phone Number");
                            friend.put("url", urlArray.get(i));
                            friend.put("pic",picArray.get(i));
                            //Add to posting JSONObject.
                            friendArray.put(friend);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SendPost send = new SendPost();
                    try {
                        send.execute("http://52.78.103.222:8080/", friendArray.toString()).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
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

//    private String find_number_in_phone(){
//        ContentResolver cr = getActivity().getContentResolver();
//        Cursor cursor = cr.query(
//                ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
//
//        int ididx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
//        int nameidx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//
//        StringBuilder result = new StringBuilder();
//        while(cursor.moveToNext())
//        {
//            result.append(cursor.getString(nameidx) + " :");
//
//            String id = cursor.getString(ididx);
//            Cursor cursor2 = cr.query(ContactsContract.CommonDataKinds.
//                            Phone.CONTENT_URI,null,
//                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?",
//                    new String[]{id},null);
//
//            int typeidx = cursor2.getColumnIndex(
//                    ContactsContract.CommonDataKinds.Phone.TYPE);
//
//            int numidx = cursor2.getColumnIndex(
//                    ContactsContract.CommonDataKinds.Phone.NUMBER);
//
//            while (cursor2.moveToNext()){
//                String num = cursor2.getString(numidx);
//                switch(cursor2.getInt(typeidx)){
//                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
//                        result.append(" Mobile:"+num);
//                        break;
//                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
//                        result.append(" Home:"+num);
//                        break;
//                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
//                        result.append(" Work:"+num);
//                        break;
//                }
//            }
//            cursor2.close();
//            result.append("\n");
//
//        }
//        cursor.close();
//
//        return result.toString();
//    }

//    private ArrayAdapter<String> phone_contact_find(){
//
//        String str= find_number_in_phone();
//        ArrayList<String> arr_list = new ArrayList<String>();
//
//        String[] str1=str.split("\n");
//        for(int i=0;i<str1.length;i++){
//            arr_list.add(str1[i]);
//        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
//                android.R.layout.simple_list_item_1,  arr_list);
//        // Assign adapter to ListView
//        adapter.sort(new Comparator<String>(){
//
//            @Override
//            public int compare(String arg1,String arg0){
//                return arg1.compareTo(arg0);
//            }
//        });
//
//        return adapter;
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)

    private void find_number_in_phone(){
        String[] result = null;
        Cursor cursor= getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);

        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if(!nameArray.contains(name)) {
                nameArray.add(name);
                phoneArray.add(phoneNumber);
                urlArray.add("");
                idArray.add("");
                picArray.add("");
                defaultPhoneArray.add("");
            }
        }
    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                getActivity(),
                Manifest.permission.READ_CONTACTS))
        {

            Toast.makeText(getActivity(),"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    Manifest.permission.READ_CONTACTS}, 1);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case 1:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getActivity(),"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getActivity(),"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

}

