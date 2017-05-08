package com.example.mohits1005.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

public class ChatRoom  extends AppCompatActivity {
    Firebase reference1;
    Button block_btn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);
        Firebase.setAndroidContext(this);
        block_btn = (Button) findViewById(R.id.block_btn);
        block_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://myapplication-8e299.firebaseio.com/users_blocked.json";
                final String user = UserDetails.username;
                final String chatWith = UserDetails.chatWith;
                final String user_b = "Blocked";
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Firebase reference = new Firebase("https://myapplication-8e299.firebaseio.com/users_blocked");
                        reference.child(user_b).setValue(user+'-'+chatWith);
                        Toast.makeText(ChatRoom.this, chatWith+" blocked successfully", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("" + volleyError);
                    }
                });
                RequestQueue rQueue = Volley.newRequestQueue(ChatRoom.this);
                rQueue.add(request);
            }
        });
    }
}
