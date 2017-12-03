package com.example.tabul.myapplication.main;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.tabul.myapplication.R;
import com.example.tabul.myapplication.api.ApiClient;
import com.example.tabul.myapplication.api.dao.MainDao;
import com.example.tabul.myapplication.api.dao.MovieResponseDao;
import com.example.tabul.myapplication.data.MovieContract;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mRecyclerMain;
    private List<MainDao> mData = new ArrayList<>();
    private MainAdapter mAdapter;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportLoaderManager().initLoader(0,null,this);

        mAdapter = new MainAdapter(mData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        mRecyclerMain = findViewById(R.id.recyclerView);
        mRecyclerMain.setAdapter(mAdapter);
        mRecyclerMain.setLayoutManager(gridLayoutManager);

        ApiClient.service().getMovieList("8a25bde177b99b86fdb8e83b89cba44a")
                .enqueue(new Callback<MovieResponseDao>() {
                    @Override
                    public void onResponse(Call<MovieResponseDao> call, Response<MovieResponseDao> response) {
                        if (response.isSuccessful()) {

                            Uri deletUri = MovieContract.MovieEntry.CONTENT_URI;
                            getContentResolver().delete(deletUri, null, null);
                            for (MovieResponseDao.MovieData data : response.body().getResults()) {
                                ContentValues contentValues = new ContentValues();

                                contentValues.put(MovieContract.MovieEntry.COLUMN_FAVORITE_IDS, data.getId());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, data.getTitle());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_ORI_TITLE, data.getOriginal_title());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, data.getVote_count());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_VIDEO, data.isVideo()? 1:0);
                                contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVG, data.getVote_average());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, data.getPopularity());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, data.getPoster_path());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANG, data.getOriginal_language());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_GENRE, "");
                                contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, data.getBackdrop_path());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_ADULT, data.isAdult()? 1:0);
                                contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, data.getOverview());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, data.getRelease_date());

                                Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

                                if (uri != null){
                                    Log.d("onResponse", "INSERT DATA SUCCES!");
                                }
//                                mData.add(new MainDao(data.getTitle(), "https://image.tmdb.org/t/p/w185" + data.getPoster_path()));
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(getApplicationContext()){
            Context mContext = getApplicationContext();
            Cursor mMovieData = null;

            @Override
            protected void onStartLoading(){
                if (mMovieData != null){
                    deliverResult(mMovieData);
                }
                else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MovieContract.MovieEntry._ID);
                }
                catch (Exception e){
                    Log.e(TAG, "Failed to asynchronously load data. " + e.getMessage());
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data){
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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
