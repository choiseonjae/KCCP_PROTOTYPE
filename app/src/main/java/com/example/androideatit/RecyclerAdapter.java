package com.example.androideatit;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androideatit.Model.Board;
import com.example.androideatit.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {
    // adapter에 들어갈 list 입니다.
    ArrayList<Board> mBoard = new ArrayList<>();
    Context context;
    View view;
    RecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        // 리사이클러 뷰랑 ItemHolder랑 연결 / 매개변수로 홀더를 가져오고 그 위치의 값을 가져옴
        holder.onBind(mBoard.get(position));

    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return mBoard.size();
    }

    void add(Board board) {
        // 외부에서 item을 추가시킬 함수입니다.
        mBoard.add(board);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textView1;
        private TextView textView2;
        private ImageView imageView;
        private Board board;

        ItemViewHolder(View view) {
            super(view);
            textView1 = view.findViewById(R.id.textView1);
            textView2 = view.findViewById(R.id.textView2);
            imageView = view.findViewById(R.id.imageView);

            view.setOnClickListener(new View.OnClickListener(){



                public void onClick(View v){
                    Intent intent = new Intent(context, RoomInfo.class); //클릭시 DetailItem

                    intent.putExtra("INFO", board);
                    context.startActivity(intent);
                }
            });
        }

        void onBind(Board board) {
            textView1.setText(board.getTitle());
            textView2.setText(board.getContent());
            this.board = board;

            Picasso.with(context).load(board.getUri()).fit().centerInside().into(imageView);



        }



    }

}
