package com.example.androideatit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androideatit.Model.ChatData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Chatting extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("chat_contents");
    TextView user;
    EditText write;
    Button sendBtn;
    ArrayAdapter adapter;
    String userId, hostId, chatName, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        Intent intent = getIntent();

        userId = intent.getStringExtra("user");
        hostId = intent.getStringExtra("host");
        chatName = hostId.compareTo(userId) > 0 ? hostId + userId : userId + hostId;
        chatName += "_chat";


        final ListView listView = (ListView) findViewById(R.id.listView2);
        write = (EditText) findViewById(R.id.writeEdit);
        sendBtn = (Button) findViewById(R.id.send);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(adapter);

        showChatContents(chatName);


    }

    void sendBtn(View v) {
        EditText write = (EditText) findViewById(R.id.writeEdit);
        ChatData chatData = new ChatData();
        chatData.setSenderName(hostId);
        chatData.setMessage(write.getText().toString());

        // 현재 시간
        time = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date(System.currentTimeMillis()));

        databaseReference.child(chatName).child(time).setValue(chatData);
        write.setText("");
    }

    void showChatContents(String chatName) {

        //user.setText(root);
        databaseReference.child(chatName).addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatData chatData = dataSnapshot.getValue(ChatData.class);
                if (chatData.getSenderName().equals(hostId))
                    adapter.add(hostId + " : " + chatData.getMessage());
                else
                    adapter.add(userId + " : " + chatData.getMessage());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {          }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {          }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {            }
            @Override
            public void onCancelled(DatabaseError databaseError) {            }

        });
    }

    public String compareTo(String host, String user) {
        if (host.compareTo(user) >= 0)
            return host + user;
        return user + host;
    }
}
