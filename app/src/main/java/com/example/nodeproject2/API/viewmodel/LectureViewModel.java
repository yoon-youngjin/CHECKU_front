package com.example.nodeproject2.API.viewmodel;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.nodeproject2.API.RetrofitClient;
import com.example.nodeproject2.datas.Lecture;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class LectureViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Lecture>> lectures
            = new MutableLiveData<>();

    ArrayList<Lecture> arrayList = new ArrayList<>();

    public void setLectures(ArrayList<Lecture> lecture) {

        arrayList.addAll(lecture);
        if(lectures.getValue() != null) {
            lectures.getValue().clear();
        }
        lectures.setValue(arrayList);
    }

    public ArrayList<Lecture> getLectures() {
        return lectures.getValue();
    }

    public void getData() {
        Call<String> call = RetrofitClient.retrofitInterface.excuteInit();

        call.enqueue(new Callback<String>() {
            @SneakyThrows
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                JSONObject jsonObject = new JSONObject(response.body());
                ArrayList<Lecture> arr_lec = new ArrayList<>();
                for (int i = 0; i < jsonObject.length(); i++) {
                    JSONArray temp = (JSONArray) jsonObject.get(String.valueOf(i));
                    Lecture lec = Lecture.builder().capacity_total(temp.get(3).toString().trim()).capacity_year(temp.get(4).toString().trim())
                            .professor_name(temp.get(1).toString().trim()).subject_title(temp.get(2).toString().trim()).subject_num(temp.get(0).toString().trim()).build();
                    arr_lec.add(lec);
                }
                lectures.setValue(arr_lec);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("check", "Fail!!");
            }
        });
    }

    public void getChangeData(String value) {
        HashMap<String, String> map = new HashMap<>();
        map.put("sbj_num",value);
        Call<String> call = RetrofitClient.retrofitInterface.excuteChange(map);
        call.enqueue(new Callback<String>() {
            @SneakyThrows
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject jsonObject = new JSONObject(response.body());
                ArrayList<Lecture> arr_lec = new ArrayList<>();
                for (int i = 0; i < jsonObject.length(); i++) {
                    JSONArray temp = (JSONArray) jsonObject.get(String.valueOf(i));
                    Lecture lec = Lecture.builder().capacity_total(temp.get(3).toString().trim()).capacity_year(temp.get(4).toString().trim())
                            .professor_name(temp.get(1).toString().trim()).subject_title(temp.get(2).toString().trim()).subject_num(temp.get(0).toString().trim()).build();
                    arr_lec.add(lec);
                }
                lectures.setValue(arr_lec);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("check", "Fail!!");
            }
        });
    }


}
