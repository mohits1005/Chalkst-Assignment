package com.example.mohits1005.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mohits1005 on 5/8/2017.
 */

public class ChatRoom  extends AppCompatActivity {
    Button block_btn;
    Button del_conversation;
    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    Integer flag = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);

        layout = (LinearLayout)findViewById(R.id.layout1);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        /*Set text of reciever's name*/
        final String user = UserDetails.chatWith;
        TextView reciever_name = (TextView) findViewById(R.id.reciever_name);
        reciever_name.setText(user);
        //Check if user is not blocked
        final String user_first = UserDetails.username;
        final String user_second = UserDetails.chatWith;
        String url = "https://myapplication-8e299.firebaseio.com/users_blocked.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String user_str = user_second+"-"+user_first;

                if (s.equals("null")) {

                } else {
                    try {
                        JSONObject obj = new JSONObject(s);
                        if (!obj.has(user_str)) {

                        } else {
                            flag = 1;
                            Toast.makeText(ChatRoom.this, "You have been blocked by the user!", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(ChatRoom.this);
        rQueue.add(request);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://myapplication-8e299.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://myapplication-8e299.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();
                if(!messageText.equals("") && flag == 0){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                }
            }
        });
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                    if (userName.equals(UserDetails.username)) {
                        addMessageBox(message, 1);
                    } else {
                        addMessageBox(message, 2);
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
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        block_btn = (Button) findViewById(R.id.block_btn);
        /*Setting Onclick listener for block button*/
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
                        //reference.child(user_b).setValue(user+'-'+chatWith);
                        reference.child(user+'-'+chatWith).child("status").setValue("1");
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
        /*Create onclick listener for delete conversation*/
        del_conversation = (Button) findViewById(R.id.del_conversation);
        del_conversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://myapplication-8e299.firebaseio.com/messages.json";
                final String user = UserDetails.username;
                final String chatWith = UserDetails.chatWith;
                final String user_b = user+"-"+chatWith;
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Firebase reference1 = new Firebase("https://myapplication-8e299.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
                        reference1.setValue(null);
                        Toast.makeText(ChatRoom.this," Conversation Deleted !", Toast.LENGTH_LONG).show();
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
    public void addMessageBox(String message, int type){
        TextView textView = new TextView(ChatRoom.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 0, 50, 20);
        if(type == 1)
            lp.gravity = Gravity.RIGHT;
        textView.setLayoutParams(lp);

        if(type == 1) {
            //textView.setGravity(Gravity.RIGHT);
            textView.setBackgroundResource(R.drawable.rounded_corner1);
        }
        else{
            //textView.setGravity(Gravity.LEFT);
            textView.setBackgroundResource(R.drawable.rounded_corner2);
        }

        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
