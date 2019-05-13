package com.example.androideatit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androideatit.Model.ChatData;
import com.example.androideatit.ViewHolder.MessageAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Chatting extends AppCompatActivity {

    private DatabaseReference contentsRef = Information.getDatabase(Information.CHAT_INFOMAION);
    private DatabaseReference relationRef = Information.getDatabase(Information.CHAT_RERALTION);
    TextView user;
    ArrayAdapter adapter;
    String userId, hostId, chatName, time;
    MessageAdapter messageAdapter;
    List<ChatData> mchat;
    ImageButton send;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        Intent intent = getIntent();

        userId = intent.getStringExtra("user");
        hostId = intent.getStringExtra("host");
        chatName = Information.integrate(hostId, userId);

        send = findViewById(R.id.send); // 보내기 버튼
        recyclerView = findViewById(R.id.listView2);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText write = (EditText) findViewById(R.id.writeEdit);
                ChatData chatData = new ChatData();
                chatData.setSender(hostId);
                chatData.setReceiver(userId);
                chatData.setMessage(write.getText().toString());
                // 현재 시간
                chatData.setTime(Information.chatTimeStamp());

                contentsRef.child(chatName).push().setValue(chatData);
                write.setText("");
            }
        });

        contentsRef.child(chatName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readMessage(hostId, userId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessage(final String myid, final String userid) {
        mchat = new ArrayList<>();

        contentsRef.child(chatName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatData chat = snapshot.getValue(ChatData.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        mchat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(Chatting.this, mchat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chatting);
//
//        Intent intent = getIntent();
//
//        userId = intent.getStringExtra("user");
//        hostId = intent.getStringExtra("host");
//        chatName = Information.integrate(hostId, userId);
//
//        final ListView listView = (ListView) findViewById(R.id.listView2);
//        write = (EditText) findViewById(R.id.writeEdit);
//        sendBtn = (Button) findViewById(R.id.send);
//
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
//        listView.setAdapter(adapter);
//
//        showChatContents(chatName);
//
//
//    }

    void sendBtn(View v) {
        EditText write = (EditText) findViewById(R.id.writeEdit);
        ChatData chatData = new ChatData();
        chatData.setSender(hostId);
        chatData.setReceiver(userId);
        chatData.setMessage(write.getText().toString());

        // 현재 시간
        time = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date(System.currentTimeMillis()));

        contentsRef.child(chatName).child(time).setValue(chatData);
        write.setText("");
    }

//    void showChatContents(String chatName) {
//
//        //user.setText(root);
//        contentsRef.child(chatName).addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                ChatData chatData = dataSnapshot.getValue(ChatData.class);
//                if (chatData.getSender().equals(hostId))
//                    adapter.add(hostId + " : " + chatData.getMessage());
//                else
//                    adapter.add(userId + " : " + chatData.getMessage());
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {          }
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {          }
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {            }
//
//        });
//    }
}
