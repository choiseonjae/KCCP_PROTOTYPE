package com.example.androideatit.Room;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.androideatit.Adapter.ListviewAdapter;
import com.example.androideatit.Common.Common;
import com.example.androideatit.ObjectClickImageView;
import com.example.androideatit.R;
import com.example.androideatit.Room.SmallMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MapFragment extends Fragment {

    static final String[] TOP_LIST = {"TOP1", "TOP2", "TOP3","TOP4"};
    String[][] rank_nm_cnt = new String[2][];
    ListviewAdapter top_adapter;
    final DatabaseReference top_database = Common.getDatabase(Common.ROOM);

    final int[] townImages = Common.getTownImages();

    final String[] townNames = Common.getTownNames();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        ObjectClickImageView[] town = new ObjectClickImageView[townImages.length];

        for (int i = 0; i < townImages.length; i++) {
            town[i] = view.findViewById(townImages[i]);
            final String townName = townNames[i];
            town[i].setOnObjectClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Fragment smallMapFragment = new SmallMapFragment(); // Fragment 생성
                    Bundle bundle = new Bundle(); // 파라미터는 전달할 데이터 개수
                    bundle.putString("townName", townName); // key , value
                    smallMapFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().add(R.id.home_fragment, smallMapFragment).commit();

                }
            });
        }
        top_adapter = new ListviewAdapter();
        ListView listView = (ListView) view.findViewById(R.id.rank_list_view);
        listView.setAdapter(top_adapter);

        add_list_item();
        return view;
    }
    public void add_list_item() {
        top_database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0, count = 0;
                rank_nm_cnt[0] = new String[(int) dataSnapshot.getChildrenCount()];
                rank_nm_cnt[1] = new String[(int) dataSnapshot.getChildrenCount()];
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    rank_nm_cnt[count][i] = snapshot.getKey();
                    rank_nm_cnt[count + 1][i] = Long.toString(snapshot.getChildrenCount());
                    i++;
                }
                //순위정렬
                for(int j=0;j<i-1;j++) {
                    for(int k=j;k<i;k++) {
                        if(Integer.parseInt(rank_nm_cnt[1][j])<Integer.parseInt(rank_nm_cnt[1][k])) {
                            String min = rank_nm_cnt[count+1][j];
                            String min_nm = rank_nm_cnt[count][j];
                            rank_nm_cnt[count+1][j]=rank_nm_cnt[count+1][k];
                            rank_nm_cnt[count][j] = rank_nm_cnt[count][k];
                            rank_nm_cnt[count][k] = min_nm;
                            rank_nm_cnt[count+1][k]=min;
                        }
                    }
                }
                top_adapter.clear();
                for(int j = 0;j<3;j++) {
                    add_list_item1(TOP_LIST[j],rank_nm_cnt[0][j],rank_nm_cnt[1][j]);
                    top_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void add_list_item1(String top, String rank_tw, String rank_cnt) {
        top_adapter.addItem(top, rank_tw,rank_cnt);
    }
}

