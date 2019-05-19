package com.example.androideatit;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.androideatit.Adapter.RoomAdapter;
import com.example.androideatit.Common.Common;
import com.example.androideatit.Model.Board;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MyScrap extends AppCompatActivity {

    private RoomAdapter adapter;

    private final String myID = Common.getMyId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scrap);

        // adapter 초기화
        init();

        getData();

    }

    private void init() {  //리사이클러뷰 초기화 및 동작
        RecyclerView recyclerView = findViewById(R.id.scrap);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RoomAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        final DatabaseReference scrapRef = Common.getDatabase(Common.SCRAP).child(myID);
        final DatabaseReference roomRef = Common.getDatabase(Common.ROOM);

        scrapRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // 내가 한 스크랩들
                for (DataSnapshot scrap : dataSnapshot.getChildren()) {
                    final String boardID = scrap.getKey(); // 게시글 ID
                    Log.e("게시글 ID : ", boardID);

                    // 방 DB를 검색해옴. / 나중에는 검색이 아니라 scrap에 구를 저장 해 놓을거임
                    roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Log.e("방 전체 정보 : ", dataSnapshot.toString());

                            for (DataSnapshot town : dataSnapshot.getChildren()) {

                                Log.e("방 정보 : ", town.toString());
                                Log.e("게시글과 글이 동일 : ", town.child(boardID).exists() + "");

                                // 해당 구에 스크랩 한 글이 존재
                                if (town.child(boardID).exists()) {
                                    Board board = town.child(boardID).getValue(Board.class);
                                    adapter.add(board, boardID);
                                    adapter.notifyDataSetChanged();
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
