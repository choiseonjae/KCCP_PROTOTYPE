package com.example.androideatit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.androideatit.Model.Board;
import com.example.androideatit.Model.BoardID;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;

public class Register extends Activity {


    private static final String TAG = "Register";
    ImageView image_preview;
    ImageButton button_choice;
    Button button_upload;
    EditText title;
    EditText content;
    String filename;

    private Uri filePath;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    private DatabaseReference database = Information.getDatabase("RoomInfo");
    String townName;
    int boardID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        image_preview = (ImageView) findViewById(R.id.preview);            //미리보기
        button_choice = (ImageButton) findViewById(R.id.camera_connect);  //사진선택
        button_upload = (Button) findViewById(R.id.bt_upload);            //이거 클릭시 데이터 베이스에 업로드
        title = (EditText) findViewById(R.id.EditText_title);
        content = (EditText) findViewById(R.id.EditText_content);
        townName = getIntent().getExtras().getString("townName");

        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }
        //버튼 클릭 이벤트(사진선택)
        button_choice.setOnClickListener(new View.OnClickListener() { //버튼 클릭했을시 사진첩 접근
            @Override
            public void onClick(View view) {
                //이미지를 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
            }
        });

        //완료버튼 클릭시 데이터베이스에 삽입!!!! uploadFile은 밑에 태우가 따로 정의함.
        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();       //사진 데이터베이스에 집어넣기
            }
        });
    }

    //결과 처리. 즉, 선택한 이미지 보여주기.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request 코드가 0이고 OK를 선택했고 데이터에 뭔가가 들어 있다면
        if (requestCode == 0 && resultCode == RESULT_OK) {
            filePath = data.getData();
            Log.d(TAG, "uri:" + String.valueOf(filePath));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image_preview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //upload the file. 데이터베이스에 삽입 하는 부분임.
    private void uploadFile() {

        //업로드할 파일이 있으면 수행
        if (filePath != null) {

            final DatabaseReference BOARD_ID = Information.getDatabase("BoardID");

            // 게시글 ID 받아오기
            BOARD_ID.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    BoardID id = dataSnapshot.getValue(BoardID.class);
                    boardID = id.boardId;
                    BOARD_ID.setValue(new BoardID(boardID + 1));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });


            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //Unique한 파일명 / 사용자 이름, 타입
            filename = Information.timeStamp(Information.getUserName(),".png");

            // room 사진 storage 참조
            final StorageReference roomStorage = Information.getStorageRef("room/" + filename);

            //storage 주소와 폴더 파일명을 지정해 준다.
            roomStorage.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override // 파일 업로드 성공시
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    roomStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override // 파일 uri 가져오는 거 성공 시
                        public void onSuccess(Uri uri) {

                            // 파일 올리기에 성공 하면 DB에도 해당 파일의 메타 데이터를 저장
                            Board board = new Board();

                            board.setUserId(Information.getUserId());
                            board.setBoardId(boardID);
                            board.setTitle(title.getText().toString());
                            board.setDate(Information.timeStamp());
                            board.setContent(content.getText().toString());
                            board.setFilename(filename);
                            board.setUserName(Information.getUserName());
                            board.setUri(uri.toString());
                            board.setLocation(townName);

                            database.child(townName).push().setValue(board);
                        }
                    });
                    progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                    Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_LONG).show();
        }
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }
}