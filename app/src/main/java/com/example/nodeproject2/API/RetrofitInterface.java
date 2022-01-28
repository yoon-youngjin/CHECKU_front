package com.example.nodeproject2.API;

import org.json.JSONArray;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.HashMap;

public interface RetrofitInterface {

//    @POST("/login")
//    Call<LoginResult> excuteLogin(@Body HashMap<String, String> map);

    @POST("/signup")
    Call<Void> excuteSignup(@Body HashMap<String, String> map);

    @POST("/click")
    Call<String> excuteClick(@Body HashMap<String, String> map);

    @POST("/click")
    Call<Void> excuteInit(@Body HashMap<String, String> map);


}