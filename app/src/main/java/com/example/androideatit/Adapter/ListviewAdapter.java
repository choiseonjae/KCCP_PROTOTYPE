package com.example.androideatit.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androideatit.Model.ListviewItem;
import com.example.androideatit.R;

import java.util.ArrayList;

public class ListviewAdapter extends BaseAdapter {
    private ArrayList<ListviewItem> listviewItemList = new ArrayList<ListviewItem>();

    public ListviewAdapter() {

    }

    public void clear(){
        listviewItemList.clear();
    }
    @Override
    public int getCount() {
        return listviewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listviewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_rank, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView rankView = (TextView) convertView.findViewById(R.id.rank) ;
        TextView town_name_View = (TextView) convertView.findViewById(R.id.tw_name) ;
        TextView view_count_View = (TextView) convertView.findViewById(R.id.view_count) ;
        Log.e("!!!",(rankView == null) + "");

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListviewItem listViewItem = listviewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        rankView.setText(listViewItem.getRank_str());
        town_name_View.setText(listViewItem.getTw_name());
        view_count_View.setText(listViewItem.getView_count());

        return convertView;
    }

    public void addItem(String rank, String tw_name, String vw_count) {
        ListviewItem item = new ListviewItem();

        Log.e("ss",vw_count);
        item.setRank_str(rank);
        item.setTw_name(tw_name);
        item.setView_count(vw_count);

        listviewItemList.add(item);
    }

    public ArrayList<ListviewItem> getdata() {
        return listviewItemList;
    }
}