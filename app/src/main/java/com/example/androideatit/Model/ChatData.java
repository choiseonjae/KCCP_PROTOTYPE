package com.example.androideatit.Model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ChatData {

    private String sender = "", message = "", receiver = "", time = "";

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("sender", sender);
        result.put("receiver", receiver);
        result.put("time", time);
        result.put("message", message);
        return result;
    }

}

