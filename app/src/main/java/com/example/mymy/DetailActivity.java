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

public class DetailActivity extends YouTubeBaseActivity {

    ArrayList<Youtube> youtubeList;
    private YouTubePlayerView youTubeView;
    private String trailer01;

    private RecyclerView recyclerView_similar;
    private MyRecyclerViewAdapter adapter_similar;
    private ArrayList<Movie> movieArrayList_similar;

    public static String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        youtubeList = new ArrayList<Youtube>();

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        id = intent.getStringExtra("id");
        String original_title = intent.getStringExtra("original_title");
        String poster_path = intent.getStringExtra("poster_path");
        String overview = intent.getStringExtra("overview");
        String release_date = intent.getStringExtra("release_date");

        TextView textView_title = (TextView)findViewById(R.id.tv_title);
        textView_title.setText(title);
        TextView textView_original_title = (TextView)findViewById(R.id.tv_original_title);
        textView_original_title.setText(original_title);
        ImageView imageView_poster = (ImageView) findViewById(R.id.iv_poster);
        Glide.with(this)
                .load(poster_path)
                .centerCrop()
                .crossFade()
                .into(imageView_poster);

        TextView textView_overview = (TextView)findViewById(R.id.tv_overview);
        textView_overview.setText(overview);
        TextView textView_release_date = (TextView)findViewById(R.id.tv_release_date);
        textView_release_date.setText(release_date);

        YoutubeAsyncTask mProcessTask = new YoutubeAsyncTask();
        mProcessTask.execute(id);

        recyclerView_similar = (RecyclerView)findViewById(R.id.recycler_view_similar);
        movieArrayList_similar = new ArrayList<Movie>();

        MyAsyncTask mAsyncTask = new MyAsyncTask();
        mAsyncTask.execute();

        recyclerView_similar.setLayoutManager(new GridLayoutManager(DetailActivity.this,3));
    }

    public class MyAsyncTask extends AsyncTask<String, Void, Movie[]>{
        ProgressDialog progressDialog = new ProgressDialog(DetailActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("\t로딩중...");
            //show dialog
            progressDialog.show();
        }

        @Override
        protected Movie[] doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/"+id+"/similar?api_key=b5375f6f4b4b28b957564bbfd05858d0&language=en-US&page=1")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Gson gson = new GsonBuilder().create();
                JsonParser parser = new JsonParser();
                JsonElement rootObject = parser.parse(response.body().charStream())
                        .getAsJsonObject().get("results");
                Movie[] posts = gson.fromJson(rootObject, Movie[].class);
                return posts;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Movie[] result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if(result.length > 0){
                for(Movie p: result){
                    movieArrayList_similar.add(p);
                }
            }
            adapter_similar = new MyRecyclerViewAdapter(DetailActivity.this, movieArrayList_similar);
            recyclerView_similar.setAdapter(adapter_similar);
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
                youTubeView = findViewById(R.id.youtube_view);
                playVideo(trailer01, youTubeView);
            }
        }

        @Override
        protected Youtube[] doInBackground(String... strings) {
            String id = strings[0];

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://api.themoviedb.org/3/movie/"+id+"/videos?api_key=b5375f6f4b4b28b957564bbfd05858d0").build();
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
