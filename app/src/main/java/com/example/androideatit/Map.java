package com.example.androideatit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Map extends AppCompatActivity {

    final int[] townImages = Infomation.getTownImages();

    final String[] townNames = Infomation.getTownNames();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ObjectClickImageView[] town = new ObjectClickImageView[townImages.length];

        for (int i = 0; i < townImages.length; i++) {
            town[i] = findViewById(townImages[i]);
            final String townName = townNames[i];
            town[i].setOnObjectClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Map.this, SmallMap.class);
                    //원래 액티비티 화면을 제거하지는 않음.
                    intent.setFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);
                    // 이름을 넘겨준다.
                    intent.putExtra("townName", townName);
                    startActivity(intent);
                }
            });
        }
    }
}
