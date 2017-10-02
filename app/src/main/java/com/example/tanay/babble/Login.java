package com.example.tanay.babble;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends Activity {

    EditText username, password;
    TextView register;
    Button login;
    CheckBox loggedIn;
    String uname, pass;

    private static final String TAG = "loginactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: ");
        
        if(ApplicationSettings.getInstance(this).isUserAuthenticated()) {

            UserDetails.name = ApplicationSettings.getInstance(this).getCurrentUser();
            Log.d(TAG, "onCreate: "+UserDetails.name);
            startActivity(new Intent(this,MainActivity.class));

        }
        setContentView(R.layout.activity_login);

        init();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
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

                            if(response.equals("null")){
                                Toast.makeText(Login.this, "No such user found", Toast.LENGTH_SHORT).show();

                            }
                            else {

                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if (!obj.has(uname)) {
                                        Toast.makeText(Login.this, "No such user found", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    if(obj.getJSONObject(uname).getString("password").equals(pass)){

                                        UserDetails.name = uname;
                                        startBabbleActivity(uname);

                                    }
                                    else
                                        Toast.makeText(Login.this, "Wrong password", Toast.LENGTH_SHORT).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();

                                }
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "onErrorResponse: "+error );
                        }
                    });

                    RequestQueue queue = Volley.newRequestQueue(Login.this);
                    queue.add(request);
                }
            }
        });
    }

    private void startBabbleActivity(String username) {

        Intent babbleIntent = new Intent(this,MainActivity.class);

        if(loggedIn.isChecked())
            ApplicationSettings.getInstance(this).setUserAuthentication(true);

        ApplicationSettings.getInstance(this).setCurrentUser(username);
        startActivity(babbleIntent);

    }

    private void init() {

        username = (EditText) findViewById(R.id.username_login);
        password = (EditText) findViewById(R.id.password_login);
        register = (TextView) findViewById(R.id.register_login);
        login = (Button) findViewById(R.id.login);
        loggedIn = (CheckBox) findViewById(R.id.logged_in);

    }
}
