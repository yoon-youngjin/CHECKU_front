package com.example.nodeproject2.API;

import com.example.nodeproject2.datas.Lecture;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface RetrofitInterface {

//    @POST("/login")
//    Call<LoginResult> excuteLogin(@Body HashMap<String, String> map);

    @POST("/signup")
    Call<Void> excuteSignup(@Body HashMap<String, String> map);

    @POST("/click")
    Call<String> excuteClick(@Body HashMap<String, String> map);

    @POST("/init")
    Call<String> excuteInit();


//    @GET("/subjects/{sbj_num}")
//    Call<List<Lecture>> excuteChangeAll(@Query("sbj_num") String sbj_num);

    @GET("/subjects")
    Call<List<Lecture>> excuteChangeAll(@QueryMap Map<String,String> map);

//    @POST("/changeAll")
//    Call<List<Lecture>> excuteChangeAll(@Body HashMap<String, String> map);

    @POST("/subjects")
    Call<List<Lecture>> excuteChange(@Body HashMap<String, String[]> map);
}