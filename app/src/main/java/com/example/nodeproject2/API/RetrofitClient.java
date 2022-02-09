package com.example.nodeproject2.API;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    private static String BASE_URL = "http:/172.30.1.54:3000";

    static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.DAYS)
            .readTimeout(1, TimeUnit.DAYS)
//                .writeTimeout(100, TimeUnit.SECONDS)
            .build();

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
                .build();

    public static RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);;

}
