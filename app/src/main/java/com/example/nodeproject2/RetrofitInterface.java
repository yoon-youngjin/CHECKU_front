package com.example.nodeproject2;

import com.example.nodeproject2.datas.Subject;
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
    Call<Void> excuteClick(@Body HashMap<String, String> map);


}
