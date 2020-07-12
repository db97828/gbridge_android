package com.example.mymy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailTvActivity extends YouTubeBaseActivity {

    ArrayList<Youtube> youtubeList;

    private YouTubePlayerView youTubeView;
    private String trailer01;
    public String id;

    private RecyclerView recyclerViewTv;
    private MyRecyclerViewAdapterTv adapterTv;
    ArrayList<Tv> tvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_detail);

        youtubeList = new ArrayList<Youtube>();

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        id = intent.getStringExtra("id");
        String original_name = intent.getStringExtra("original_name");
        String poster_path = intent.getStringExtra("poster_path");
        String overview = intent.getStringExtra("overview");
        String first_air_date = intent.getStringExtra("first_air_date");

        TextView textView_name = (TextView)findViewById(R.id.tv_name);
        textView_name.setText(name);
        TextView textView_original_name = (TextView)findViewById(R.id.tv_original_name);
        textView_original_name.setText(original_name);
        ImageView imageView_poster = (ImageView) findViewById(R.id.iv_poster_tv);
        Glide.with(this)
                .load(poster_path)
                .centerCrop()
                .crossFade()
                .into(imageView_poster);

        TextView textView_overview = (TextView)findViewById(R.id.tv_overview_tv);
        textView_overview.setText(overview);
        TextView textView_first_air_date = (TextView)findViewById(R.id.tv_first_air_date);
        textView_first_air_date.setText(first_air_date);

        YoutubeAsyncTask mProcessTask = new YoutubeAsyncTask();
        mProcessTask.execute(id);

        recyclerViewTv = (RecyclerView)findViewById(R.id.recycler_view_tvs);
        tvList = new ArrayList<Tv>();

        MyAsyncTask mAsyncTask = new MyAsyncTask();
        mAsyncTask.execute();

        recyclerViewTv.setLayoutManager(new GridLayoutManager(DetailTvActivity.this,3));

    }

    public class MyAsyncTask extends AsyncTask<String, Void, Tv[]>{
        ProgressDialog progressDialog = new ProgressDialog(DetailTvActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("\t로딩중...");
            //show dialog
            progressDialog.show();
        }

        @Override
        protected Tv[] doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/tv/"+id+"/similar?api_key=b5375f6f4b4b28b957564bbfd05858d0&language=en-US&page=1")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Gson gson = new GsonBuilder().create();
                JsonParser parser = new JsonParser();
                JsonElement rootObject = parser.parse(response.body().charStream())
                        .getAsJsonObject().get("results");
                Tv[] posts = gson.fromJson(rootObject, Tv[].class);
                return posts;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Tv[] tvs) {
            super.onPostExecute(tvs);
            progressDialog.dismiss();
            if(tvs.length > 0){
                for(Tv p : tvs){
                    tvList.add(p);
                }
            }

            adapterTv = new MyRecyclerViewAdapterTv(DetailTvActivity.this, tvList);
            recyclerViewTv.setAdapter(adapterTv);
        }
    }

    public void playVideo(final String videoId, YouTubePlayerView youTubePlayerView){
        Log.d("Youtube", "trailer: " + videoId);
        youTubePlayerView.initialize("AIzaSyDjN2k6j77L48Y2AG25LFxLJzLbkkr7lD8", new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

    public class YoutubeAsyncTask extends AsyncTask<String, Void, Youtube[]>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Youtube[] youtubes) {
            super.onPostExecute(youtubes);

            if(youtubes.length > 0){
                for(Youtube p : youtubes){
                    youtubeList.add(p);
                }

                trailer01 = youtubeList.get(0).getKey();
                Log.d("Youtube", "trailer : " + trailer01);
                youTubeView = findViewById(R.id.youtube_view_tv);
                playVideo(trailer01, youTubeView);
            }
        }

        @Override
        protected Youtube[] doInBackground(String... strings) {
            String key = strings[0];

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://api.themoviedb.org/3/tv/"+key+"/videos?api_key=b5375f6f4b4b28b957564bbfd05858d0").build();
            try{
                Response response = client.newCall(request).execute();
                Gson gson = new GsonBuilder().create();
                JsonParser parser = new JsonParser();
                JsonElement rootObject = parser.parse(response.body().charStream()).getAsJsonObject().get("results");
                Youtube[] posts = gson.fromJson(rootObject, Youtube[].class);

                return posts;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
