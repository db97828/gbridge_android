package com.example.mymy;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyRecyclerViewAdapterTv extends RecyclerView.Adapter<MyRecyclerViewAdapterTv.RecyclerViewHolders> {

    private ArrayList<Tv> mTvList;
    private LayoutInflater mInflate;
    private Context mContext;

    public MyRecyclerViewAdapterTv(Context context, ArrayList<Tv> itemList){
        this.mContext = context;
        this.mInflate = LayoutInflater.from(context);
        this.mTvList = itemList;
    }

    @NonNull
    @Override
    public RecyclerViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.list_item, parent, false);
        RecyclerViewHolders viewHolder = new RecyclerViewHolders(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolders holder, final int position) {
        String url = "https://image.tmdb.org/t/p/w500"+mTvList.get(position).getPoster_path();
        Glide.with(mContext).load(url).centerCrop().crossFade().into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailTvActivity.class);
                intent.putExtra("id", mTvList.get(position).getId());
                intent.putExtra("name", mTvList.get(position).getName());
                intent.putExtra("original_name", mTvList.get(position).getOriginal_name());
                intent.putExtra("poster_path", mTvList.get(position).getPoster_path());
                intent.putExtra("overview", mTvList.get(position).getOverview());
                intent.putExtra("first_air_date", mTvList.get(position).getFirst_air_date());

                mContext.startActivity(intent);
                Log.d("Adapter","Clicked: " + position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mTvList.size();
    }

    public static class RecyclerViewHolders extends RecyclerView.ViewHolder{
        public ImageView imageView;

        public RecyclerViewHolders(View itemView){
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
