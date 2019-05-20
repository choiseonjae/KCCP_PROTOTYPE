package com.example.androideatit.Room;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androideatit.Common.Common;
import com.example.androideatit.ObjectClickImageView;
import com.example.androideatit.R;
import com.example.androideatit.Room.SmallMap;

import java.util.Map;

public class MapFragment extends Fragment {

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
                    Intent intent = new Intent(getContext(), SmallMap.class);
                    //원래 액티비티 화면을 제거하지는 않음.
                    intent.setFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);
                    // 이름을 넘겨준다.
                    intent.putExtra("townName", townName);
                    startActivity(intent);
                }
            });
        }
        return view;
    }

}
