package com.example.androideatit.Model;

public class ListviewItem {
    private String rank_str;
    private String tw_name;
    private String view_count;

    public void setRank_str(String rank) {
        rank_str = rank;
    }
    public void setTw_name(String tw_name) {
        this.tw_name = tw_name;
    }
    public void setView_count(String view_count) {
        this.view_count = view_count;
    }

    public String getRank_str() {
        return this.rank_str;
    }
    public String getTw_name() {
        return this.tw_name;
    }
    public String getView_count() {
        return this.view_count;
    }
}
