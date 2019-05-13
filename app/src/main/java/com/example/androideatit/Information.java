package com.example.androideatit;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Information {

    private static final StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://kccp-a4bd9.appspot.com");

    private static String USER_NAME;

    private static String USER_ID;

    private static final int[] townImages = {R.id.dobong_image, R.id.nowon_image, R.id.dongdaemoon_image, R.id.enpyeong_image,
            R.id.joong_image, R.id.jongro_image, R.id.seodaemoon_image, R.id.seongbook_image,
            R.id.seongdong_image, R.id.guangjin_image, R.id.gangnam_image, R.id.gangbook_image,
            R.id.gangdong_image, R.id.gangseo_image, R.id.guanak_image, R.id.guro_image,
            R.id.geumcheon_image, R.id.dongjak_image, R.id.joonglang_image, R.id.mapo_image,
            R.id.seocho_image, R.id.songpa_image, R.id.yangcheon_image, R.id.yeongdeungpo_image,
            R.id.yongsan_image};

    private static final String[] townNames = {"도봉구", "노원구", "동대문구", "은평구", "중구", "종로구",
            "서대문구", "성북구", "성동구", "광진구", "강남구", "강북구", "강동구",
            "강서구", "관악구", "구로구", "금천구", "동작구", "중랑구", "마포구",
            "서초구", "송파구", "양천구", "영등포구", "용산구"};

    public static int[] getTownImages() {
        return townImages;
    }

    public static String[] getTownNames() {
        return townNames;
    }

    public static String getUserName() {
        return USER_NAME;
    }

    public static void setUserName(String userName) {
        USER_NAME = userName;
    }

    public static String getUserId() {
        return USER_ID;
    }

    public static void setUserId(String userId) {
        USER_ID = userId;
    }

    public static DatabaseReference getDatabase() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static DatabaseReference getDatabase(String ref) {
        return FirebaseDatabase.getInstance().getReference(ref);
    }

    public static StorageReference getStorageRef() {
        return storageRef;
    }

    public static StorageReference getStorageRef(String child) {
        return storageRef.child(child);
    }

    public static String timeStamp(String user, String type) {
        return user + "_" + timeStamp() + type;
    }

    public static String timeStamp() {
        return new SimpleDateFormat("yyyyMMHH_mmss").format(new Date());
    }


}
