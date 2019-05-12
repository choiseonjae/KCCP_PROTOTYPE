package com.example.androideatit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androideatit.Common.Common;
import com.example.androideatit.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {
    EditText logId, logPassword;
    CheckBox edtCheck;
    Button btnSignIn;
    public static String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        logPassword = (MaterialEditText)findViewById(R.id.logPassword);
        logId = (MaterialEditText)findViewById(R.id.logId);
        edtCheck = findViewById(R.id.edtCheck);
        btnSignIn = findViewById(R.id.btnSignIn);

        //Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Check if user not exist in database
                        if (dataSnapshot.child(logId.getText().toString()).exists()) {
                            //Get User information
                            mDialog.dismiss();
                            User user = dataSnapshot.child(logId.getText().toString()).getValue(User.class);
                            if (user.getPassword().equals(logPassword.getText().toString())){
                                if(edtCheck.isChecked()){
                                    //phone, password 일치시, 자동 로그인에 필요한 정보
                                    Log.d("LOG", "자동로그인!");
                                    SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("edtPhone", logId.getText().toString());
                                    editor.putString("edtPassword", logPassword.getText().toString());
                                    editor.apply();
                                }

                                Toast.makeText(SignIn.this, user.getName()+"님 환영합니다!", Toast.LENGTH_SHORT).show();
                                Intent homeIntent = new Intent(SignIn.this, Home.class);
                                // ID, 이름 저장
                                Information.setUserName(user.getName());
                                Information.setUserId(logId.getText().toString());
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();
                            }
                            else
                                Toast.makeText(SignIn.this, "Wrong Password !!!", Toast.LENGTH_SHORT).show();
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "User not exist in Database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
