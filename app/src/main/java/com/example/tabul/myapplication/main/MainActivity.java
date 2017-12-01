package com.example.tabul.myapplication.main;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.tabul.myapplication.R;
import com.example.tabul.myapplication.data.ApiClient;
import com.example.tabul.myapplication.data.MovieResponseDao;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerMain;
    private List<MainDao> mData = new ArrayList<>();
    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new MainAdapter(mData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);

        mRecyclerMain = findViewById(R.id.recyclerView);
        mRecyclerMain.setAdapter(mAdapter);
        mRecyclerMain.setLayoutManager(gridLayoutManager);

        ApiClient.service().getMovieList("8a25bde177b99b86fdb8e83b89cba44a")
                .enqueue(new Callback<MovieResponseDao>() {
                    @Override
                    public void onResponse(Call<MovieResponseDao> call, Response<MovieResponseDao> response) {
                        if (response.isSuccessful()) {
                            for (MovieResponseDao.MovieData data : response.body().getResults()) {
                                mData.add(new MainDao(data.getTitle(), "https://image.tmdb.org/t/p/w185"+data.getPoster_path()));
                            }

                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponseDao> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mData.add(new MainDao("Satu","https://marketplace.canva.com/MAB7lLo7q14/1/thumbnail/canva-young-male-profile-icon-vector-illustration-MAB7lLo7q14.png"));
//                mData.add(new MainDao("Dua", "https://thumbs.dreamstime.com/b/profile-icon-male-avatar-portrait-casual-person-silhouette-face-flat-design-vector-46846328.jpg"));
//                mData.add(new MainDao("Tiga","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX17277719.jpg"));
//                mData.add(new MainDao("Empat","https://img3.stockfresh.com/files/w/wad/m/84/6390217_stock-vector-business-profile-icon-flat-design.jpg"));mData.add(new MainDao("Satu","https://marketplace.canva.com/MAB7lLo7q14/1/thumbnail/canva-young-male-profile-icon-vector-illustration-MAB7lLo7q14.png"));
//                mData.add(new MainDao("Dua", "https://thumbs.dreamstime.com/b/profile-icon-male-avatar-portrait-casual-person-silhouette-face-flat-design-vector-46846328.jpg"));
//                mData.add(new MainDao("Tiga","https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX17277719.jpg"));
//                mData.add(new MainDao("Empat","https://img3.stockfresh.com/files/w/wad/m/84/6390217_stock-vector-business-profile-icon-flat-design.jpg"));
//
//                mAdapter.notifyDataSetChanged();
//            }
//        }, 5000);

        Toast.makeText(this, "Loading data!", Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putString("text",editText.getText().toString());
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        if(savedInstanceState != null) {
//            editText.setText(savedInstanceState.getString("text"));
//        }
//    }
}
