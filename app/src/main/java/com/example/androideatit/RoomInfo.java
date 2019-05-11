package com.example.androideatit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androideatit.Model.Board;
import com.example.androideatit.Model.BoardID;
import com.example.androideatit.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class RoomInfo extends AppCompatActivity {

    private Board board;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Recycler 에서 보낸 Board 정보
        board = (Board) getIntent().getSerializableExtra("INFO");

        super.onCreate(savedInstanceState);

        Log.e("아이디 : ", Information.getUserId());
        Log.e("글 아이디 : ", board.userId);

        if (!board.userId.equals(Information.getUserId())) {
            setContentView(R.layout.activity_roominfo);

            // 폰 번호 알아오기
            Information.getDatabase("User").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User id = dataSnapshot.child(board.userId).getValue(User.class);
                    phoneNumber = id.getPhone();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            LinearLayout call = (LinearLayout) findViewById(R.id.call);
            LinearLayout chat = (LinearLayout) findViewById(R.id.chat);
            LinearLayout report = (LinearLayout) findViewById(R.id.report);

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:/" + phoneNumber)));
                }
            });

            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(RoomInfo.this, ChatList.class));
                }
            });

            report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reportAlert();
                }
            });


        }
        else {
            setContentView(R.layout.activity_my_room_info);

            LinearLayout change = (LinearLayout) findViewById(R.id.change_board);
            LinearLayout delete = (LinearLayout) findViewById(R.id.delete_board);

            change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "chage", Toast.LENGTH_LONG).show();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_LONG).show();
                }
            });

        }

        // xml과 바인딩
        TextView textView1 = (TextView) findViewById(R.id.textView1);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        ImageView imageView = (ImageView) findViewById(R.id.imageview);

        textView1.setText(board.title);
        textView2.setText(board.content);

        Picasso.with(getApplicationContext()).load(board.uri).fit().centerInside().into(imageView);

    }

    // 신고 여부
    public void reportAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("신고");
        builder.setMessage("해당 게시글을 신고하시겠습니까?\n허위 신고는 제제를 받을 수 있습니다.");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "접수 되었습니다.", Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "취소 되었습니다.", Toast.LENGTH_LONG).show();
                    }
                });

        builder.show();
    }


}