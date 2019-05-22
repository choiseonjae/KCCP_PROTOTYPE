package com.example.androideatit;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androideatit.Chat.ChatList;
import com.example.androideatit.Chat.ChatListFragment;
import com.example.androideatit.Common.Common;
import com.example.androideatit.Model.Category;
import com.example.androideatit.Room.MapFragment;
import com.example.androideatit.ViewHolder.MenuViewHolder;
import com.example.androideatit.WantRoom.ListActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView nameView;
    FirebaseDatabase database;
    DatabaseReference category;
    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;

    TextView txtFullName;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;


    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Menu");

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        nameView = findViewById(R.id.nameView);
        nameView.setText(Common.getUserName() + " 님 환영합니다!");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set Name for user
        View headerView = navigationView.getHeaderView(0);
        txtFullName = headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.getUserName());

//        //Load menu
//        recycler_menu = findViewById(R.id.recycler_menu);
//        recycler_menu.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        recycler_menu.setLayoutManager(layoutManager);

    }

    //뒤로가기 버튼
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.map) {
            toolbar.setTitle("방 찾기");
            getSupportFragmentManager().beginTransaction().add(R.id.home_fragment, new MapFragment()).commit();
//            startActivity(new Intent(this, Map.class));
        }
        if (id == R.id.scrap) {
            toolbar.setTitle("내 스크랩");
            getSupportFragmentManager().beginTransaction().add(R.id.home_fragment, new MyScrapFragment()).commit();
//            startActivity(new Intent(getApplicationContext(), MyScrap.class));
        }
        if (id == R.id.chat) {
            toolbar.setTitle("채팅 목록");
            getSupportFragmentManager().beginTransaction().add(R.id.home_fragment, new ChatListFragment()).commit();
//            startActivity(new Intent(Home.this, ChatList.class));
        }
        if (id == R.id.getroom)
            startActivity(new Intent(Home.this, ListActivity.class));
        if (id == R.id.setting)
            startActivity(new Intent(Home.this, Setting.class));
        if (id == R.id.nav_log_out) {
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(Home.this);
            alertdialog.setTitle("로그아웃");
            alertdialog.setMessage("로그아웃 하시겠습니까?");
            // 게시할 때
            alertdialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.remove("userID");
                    editor.remove("userPassword");
                    editor.apply();

                    Toast.makeText(Home.this, "로그아웃이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Home.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            // 게시 안하는 경우
            alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Do nothing
                }
            });
            // 다이얼로그 보여줌
            alertdialog.show();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
