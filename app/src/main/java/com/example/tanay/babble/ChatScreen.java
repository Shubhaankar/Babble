package com.example.tanay.babble;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ChatScreen extends AppCompatActivity implements ChildEventListener {

    private User user;

    LinearLayout chatsLayout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    FirebaseDatabase database;
    DatabaseReference ref1, ref2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        user = getIntent().getExtras().getParcelable("user");

        init();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.name);
                    ref1.push().setValue(map);
                    ref2.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });

    }

    private void init() {

        chatsLayout = (LinearLayout) findViewById(R.id.chats_layout);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        database = FirebaseDatabase.getInstance();

        ref1 = database.getReference().child("messages").getRef().child(UserDetails.name+"_"+user.getName());
        ref2 = database.getReference().child("messages").getRef().child(user.getName()+"_"+UserDetails.name);

        ref1.addChildEventListener(this);

    }

    private void addMessageBox(String message, int type) {

        TextView textView = new TextView(ChatScreen.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 2) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        chatsLayout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);

    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
        String message = map.get("message").toString();
        String userName = map.get("user").toString();

        if(userName.equals(UserDetails.name)){
            addMessageBox("You:-\n" + message, 1);
        }
        else{
            addMessageBox(UserDetails.chatwith + ":-\n" + message, 2);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
