package com.example.mymy;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment2 extends Fragment {

    private RecyclerView recyclerView;
    private MyRecyclerViewAdapterTv adapter;
    ArrayList<Tv> tvList;

    public Fragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_2,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view2);
        tvList = new ArrayList<Tv>();

        MyAsyncTask mAsyncTask = new MyAsyncTask();
        mAsyncTask.execute();

//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        // Inflate the layout for this fragment
        return view;
    }
    public class MyAsyncTask extends AsyncTask<String, Void, Tv[]> {
        ProgressDialog progressDialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("\t로딩중.....");
            progressDialog.show();
        }

        @Override
        protected Tv[] doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://api.themoviedb.org/3/trending/tv/day?api_key=b5375f6f4b4b28b957564bbfd05858d0&language=ko-KR&page=1&time_window=day").build();
            try{
                Response response = client.newCall(request).execute();
                Gson gson = new GsonBuilder().create();
                JsonParser parser = new JsonParser();
                JsonElement rootObject = parser.parse(response.body().charStream()).getAsJsonObject().get("results");
                Tv[] posts = gson.fromJson(rootObject, Tv[].class);
                return posts;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Tv[] result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if(result.length > 0){
                for(Tv p : result){
                    tvList.add(p);
                }
            }

            adapter = new MyRecyclerViewAdapterTv(getContext(), tvList);
            recyclerView.setAdapter(adapter);
        }
    }
}
