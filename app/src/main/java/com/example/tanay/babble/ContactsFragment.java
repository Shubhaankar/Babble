package com.example.tanay.babble;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ContactsFragment extends android.support.v4.app.Fragment {

    private static ContactsFragment contactsFragment= null;
    private static Context context;
    private UserAdapter adapter;
    private static final String TAG = "contacts";
    ProgressDialog progressDialog;
    View view;
    ArrayList<User> userList = null;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.d(TAG, "onAttach: ");

        //if(userList == null)
        //    populateDataSet();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_contacts,container,false);

        Log.d(TAG, "onCreateView: ");
        
        //if(userList != null)
        //    populateListView();

        populateDataSet();

        return view;

    }

    private synchronized void populateListView() {

        Log.d(TAG, "populateListView: ");
        ListView contactsList = (ListView) view.findViewById(R.id.contacts_list);
        adapter = new UserAdapter(userList);
        contactsList.setAdapter(adapter);

        contactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = userList.get(i);
                Intent chatScreenIntent = new Intent(context, ChatScreen.class);
                UserDetails.chatwith = user.getName();
                chatScreenIntent.putExtra("user",user);
                startActivity(chatScreenIntent);
            }
        });

        if(progressDialog != null)
            progressDialog.dismiss();

    }

    private void populateDataSet() {

        Log.d(TAG, "populateDataSet: ");

        userList = new ArrayList<>();

        String url = "https://babble-2c748.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.equals("null")) {
                    Toast.makeText(context, "No users found", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: No users found");
                }
                else{

                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d(TAG, "onResponse: "+response);
                        doOnSuccess(response);
    

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    private void doOnSuccess(String response) {
        Log.d(TAG, "doOnSuccess: ");
        try {
            JSONObject obj = new JSONObject(response);

            Iterator i = obj.keys();
            while(i.hasNext()){
                String key = i.next().toString();

                if(!key.equals(UserDetails.name)){

                    String image = "";
                    User user = new User(key, "", image, "");
                    userList.add(user);
                    Toast.makeText(context, UserDetails.name +"  ->  "+key, Toast.LENGTH_SHORT).show();
                }
            }

            populateListView();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static ContactsFragment getInstance(Context context){

        if(contactsFragment == null)
            contactsFragment = new ContactsFragment();

        ContactsFragment.context = context;
        return contactsFragment;
    }


}
