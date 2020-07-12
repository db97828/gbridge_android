package com.example.mymy;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyAsyncTaskMovie extends AsyncTask<String, Void, Movie[]> {

    private Context mContext;
    private ProgressDialog progressDialog;
    private TaskCompletedMovie mCallback;

    public MyAsyncTaskMovie(Context context, TaskCompletedMovie mCallback){
        this.mContext = context;
        this.mCallback = mCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("\t로딩중...");
        progressDialog.show();
    }

    @Override
    protected Movie[] doInBackground(String... strings) {
        String url = strings[0];
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            Gson gson = new GsonBuilder().create();
            JsonParser parser = new JsonParser();
            JsonElement rootObject = parser.parse(response.body().charStream()).getAsJsonObject().get("results");
            Movie[] posts = gson.fromJson(rootObject, Movie[].class);
            return posts;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Movie[] result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        mCallback.onTaskComplete(result);
    }
}
