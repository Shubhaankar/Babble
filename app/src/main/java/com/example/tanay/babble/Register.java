package com.example.tanay.babble;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    EditText username, password;
    Button register;
    String uname, pass;

    private static final String TAG = "register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uname = username.getText().toString().trim();
                pass = password.getText().toString().trim();

                if(uname.equals(""))
                    username.setError("Please enter the username");
                else
                if(pass.equals(""))
                    password.setError("Please enter the password");
                else{

                    String url = "https://babble-2c748.firebaseio.com/users.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            DatabaseReference reference  = FirebaseDatabase.getInstance().getReference().child("users").getRef();

                            if(response.equals("null")){
                                reference.child(uname).child("password").setValue(pass);

                                Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();

                            }

                            else {
                                try {

                                    JSONObject obj = new JSONObject(response);

                                    if (obj.has(uname))
                                        username.setError("Username already exists");
                                    else {

                                        reference.child(uname).child("password").setValue(pass);
                                        Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(Register.this, Login.class));
                                    }

                                } catch (JSONException e) {

                                    Log.e(TAG, "onResponse: ", e);

                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.e(TAG, "onErrorResponse: ", error );

                        }
                    });

                    RequestQueue queue = Volley.newRequestQueue(Register.this);
                    queue.add(request);

                }
            }
        });

    }

    private void init() {

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);

    }
}
