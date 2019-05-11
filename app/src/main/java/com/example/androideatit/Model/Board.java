package com.example.androideatit.Model;

import android.net.Uri;

import java.io.Serializable;

public class Board implements Serializable { //저거 implements intent에서 값 넘길때 board 객체 넘기려고 추가한거임.


    public int boardId;
    public String userId;
    public String title;
    public String date;
    public String content;
    public String filename;
    public String uri;
    public String userName;

}
