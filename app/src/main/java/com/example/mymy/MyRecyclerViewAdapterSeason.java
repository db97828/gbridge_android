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

public class MyRecyclerViewAdapterSeason extends RecyclerView.Adapter<MyRecyclerViewAdapterSeason.RecyclerViewHolders> {

    private ArrayList<Season> mSeasonList;
    private LayoutInflater mInflate;
    private Context mContext;

    public MyRecyclerViewAdapterSeason(Context context, ArrayList<Season> itemList){
        this.mContext = context;
        this.mInflate = LayoutInflater.from(context);
        this.mSeasonList = itemList;
    }

    @NonNull
    @Override
    public RecyclerViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.list_item_season, parent, false);
        RecyclerViewHolders viewHolder = new RecyclerViewHolders(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolders holder, final int position) {
        String url = "https://image.tmdb.org/t/p/w500"+mSeasonList.get(position).getPoster_path();
        Glide.with(mContext).load(url).centerCrop().crossFade().into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailTvActivity.class);
                intent.putExtra("id", mSeasonList.get(position).getId());
                intent.putExtra("name", mSeasonList.get(position).getName());
                intent.putExtra("poster_path", mSeasonList.get(position).getPoster_path());
                intent.putExtra("season_number", mSeasonList.get(position).getSeason_number());

                mContext.startActivity(intent);
                Log.d("Adapter","Clicked: " + position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mSeasonList.size();
    }

    public static class RecyclerViewHolders extends RecyclerView.ViewHolder{
        public ImageView imageView;

        public RecyclerViewHolders(View itemView){
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
