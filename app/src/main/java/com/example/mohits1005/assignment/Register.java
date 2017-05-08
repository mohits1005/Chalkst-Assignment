package com.example.mohits1005.assignment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mohits1005 on 5/8/2017.
 */

public class Register  extends AppCompatActivity{
    EditText username, password, usr_mobile;
    Button registerButton;
    String user, pass, mobile;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        usr_mobile = (EditText)findViewById(R.id.usr_mobile);
        registerButton = (Button)findViewById(R.id.registerButton);
        login = (TextView)findViewById(R.id.login);

        Firebase.setAndroidContext(this);
        /*Adding on click listener for login*/
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, MainActivity.class));
            }
        });
        /*Adding onclick listener for register button*/
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();
                mobile = usr_mobile.getText().toString();
                if(user.equals("")){
                    username.setError("Can't be blank");
                }
                else if(pass.equals("")){
                    password.setError("Can't be blank");
                }
                else if(mobile.equals("")){
                    usr_mobile.setError("Can't be blank");
                }
                else if(!user.matches("[A-Za-z0-9]+")){
                    username.setError("Alphabet or Number allowed");
                }
                else if(user.length()<5){
                    username.setError("5 characters long");
                }
                else if(pass.length()<5){
                    password.setError("5 characters long");
                }
                else if(mobile.length()!=10){
                    usr_mobile.setError("Invalid mobile number");
                }
                else {
                    Toast.makeText(Register.this, "Loading", Toast.LENGTH_LONG).show();
                    String url = "https://myapplication-8e299.firebaseio.com/users.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            Firebase reference = new Firebase("https://myapplication-8e299.firebaseio.com/users");

                            if(s.equals("null")) {
                                reference.child(user).child("password").setValue(pass);
                                Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                            }
                            else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        reference.child(user).child("password").setValue(pass);
                                        reference.child(user).child("mobile").setValue(mobile);
                                        Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(Register.this, "username already exists", Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError );
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                    rQueue.add(request);
                }
            }
        });

    }
}
