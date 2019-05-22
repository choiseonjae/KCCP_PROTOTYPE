package com.example.androideatit.Model;

import android.net.Uri;

import java.io.Serializable;

public class Board implements Serializable { //저거 implements intent에서 값 넘길때 board 객체 넘기려고 추가한거임.


    private String boardID, userId, title, date, roomType, contractType, floor, adminExpenses, roomAverge, filename, uri, userName, location;

    public String getBoardID() {
        return boardID;
    }

    public void setBoardID(String boardID) {
        this.boardID = boardID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getAdminExpenses() {
        return adminExpenses;
    }

    public void setAdminExpenses(String adminExpenses) {
        this.adminExpenses = adminExpenses;
    }

    public String getRoomAverge() {
        return roomAverge;
    }

    public void setRoomAverge(String roomAverge) {
        this.roomAverge = roomAverge;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
