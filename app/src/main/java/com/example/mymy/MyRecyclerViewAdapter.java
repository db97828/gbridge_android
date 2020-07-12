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

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.RecyclerViewHolders> {

    private ArrayList<Movie> mMovieList;
    private LayoutInflater mInflate;
    private Context mContext;

    public MyRecyclerViewAdapter(Context context, ArrayList<Movie> itemList){
        this.mContext = context;
        this.mInflate = LayoutInflater.from(context);
        this.mMovieList = itemList;
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
        String url = "https://image.tmdb.org/t/p/w500"+mMovieList.get(position).getPoster_path();
        Glide.with(mContext).load(url).centerCrop().crossFade().into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("id", mMovieList.get(position).getId());
                intent.putExtra("title", mMovieList.get(position).getTitle());
                intent.putExtra("original_title", mMovieList.get(position).getOriginal_title());
                intent.putExtra("poster_path", mMovieList.get(position).getPoster_path());
                intent.putExtra("overview", mMovieList.get(position).getOverview());
                intent.putExtra("release_date", mMovieList.get(position).getRelease_date());

                mContext.startActivity(intent);
                Log.d("Adapter","Clicked: " + position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mMovieList.size();
    }


    public static class RecyclerViewHolders extends RecyclerView.ViewHolder{
        public ImageView imageView;

        public RecyclerViewHolders(View itemView){
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
