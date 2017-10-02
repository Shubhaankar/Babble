package com.example.tanay.babble;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ChatFragment extends android.support.v4.app.Fragment {

    private static ChatFragment chatFragment = null;
    private static Context context;
    private UserAdapter adapter;
    private static final String TAG = "chats";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    ProgressDialog progressDialog;
    View view;
    ArrayList<User> chatList = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_chat,container, false);

        populateDataSet();

        return view;

    }

    private void populateDataSet() {

        Log.d(TAG, "populateDataSet: ");

        chatList = new ArrayList<>();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();

        DatabaseReference ref = database.getReference().child("messages").getRef();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = iterable.iterator();
                Log.d(TAG, "onDataChange: Current user is "+UserDetails.name);
                while (iterator.hasNext()) {

                    DataSnapshot data = iterator.next();
                    String key = data.getKey();
                    Log.d(TAG, "onDataChange: " + key);
                    if (key.startsWith(UserDetails.name)) {
                        String[] array = key.split("_");
                        User user = new User(array[1], "", "", "");
                        chatList.add(user);
                    }
                }

                populateListView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private synchronized void populateListView() {

        Log.d(TAG, "populateListView: ");
        ListView contactsList = (ListView) view.findViewById(R.id.chat_list);
        adapter = new UserAdapter(chatList);
        contactsList.setAdapter(adapter);

        contactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = chatList.get(i);
                Intent chatScreenIntent = new Intent(context, ChatScreen.class);
                UserDetails.chatwith = user.getName();
                chatScreenIntent.putExtra("user",user);
                startActivity(chatScreenIntent);
            }
        });

        progressDialog.dismiss();
    }

    public static ChatFragment getInstance(Context context){

        if(chatFragment == null)
            chatFragment = new ChatFragment();

        ChatFragment.context = context;
        return chatFragment;

    }

}
