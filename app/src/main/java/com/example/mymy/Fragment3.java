package com.example.mymy;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
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
public class Fragment3 extends Fragment {

    ImageView click_btn;
    EditText editText;

    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter adapter;
    private ArrayList<Movie> mMovieList;
    String name;

    public Fragment3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_3, container, false);

        click_btn = view.findViewById(R.id.click);
        editText = view.findViewById(R.id.search_edit);
        recyclerView = view.findViewById(R.id.recycler_view_search);

        mMovieList = new ArrayList<>();

        return view;
    }

    public class MyAsyncTask extends AsyncTask<String, Void, Movie[]> {
        ProgressDialog progressDialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("\t로딩중.....");
            progressDialog.show();
        }

        @Override
        protected Movie[] doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(strings[0]).build();
            try{
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
            if(mMovieList != null){
                mMovieList.clear();
            }
            if(result.length > 0){
                for(Movie p : result){
                    mMovieList.add(p);
                }
            }

            adapter = new MyRecyclerViewAdapter(getContext(), mMovieList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        click_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                name = editText.getText().toString();
                String search_url = "https://api.themoviedb.org/3/search/multi?api_key=b5375f6f4b4b28b957564bbfd05858d0&query="+name+"&language=ko-KR&page=1";
                String[] strings = {search_url};
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute(strings[0]);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
