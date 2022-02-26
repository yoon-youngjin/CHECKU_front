package com.example.nodeproject2.API;

import com.example.nodeproject2.datas.Lecture;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface RetrofitInterface {


    /**
     * 모니터링 서비스
     */
    @POST("/click")
    Call<String> excuteClick(@Body HashMap<String, String> map);

    /**
     * 학과에 해당하는 모든 데이터 get route
     */
    @GET("/subjects")
    Call<List<Lecture>> excuteChangeAll(@QueryMap Map<String,String> map);

    /**
     * 수강바구니에 들어있는 과목 새로고침을 위한 route
     */
    @POST("/subjects")
    Call<List<Lecture>> excuteChange(@Body HashMap<String, String[]> map);

    /**
     * 수강신청 route
     */
    @POST("/register")
    Call<String> excuteRegister(@Body HashMap<String,String> map);
}