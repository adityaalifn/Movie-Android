package com.example.tabul.myapplication.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Tabul on 01/12/2017.
 */

public class ApiClient {
    public static ApiRequestInterface service(){
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.themoviedb.org/3/")
                .build();
        return retrofit.create(ApiRequestInterface.class);
    }
}
