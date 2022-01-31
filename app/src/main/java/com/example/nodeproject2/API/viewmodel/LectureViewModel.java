package com.example.nodeproject2.API.viewmodel;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.nodeproject2.API.RetrofitClient;
import com.example.nodeproject2.datas.Lecture;
import lombok.SneakyThrows;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

public class LectureViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Lecture>> lectures
            = new MutableLiveData<>();

    ArrayList<Lecture> arrayList = new ArrayList<>();

    public void setLectures(ArrayList<Lecture> lecture) {
        arrayList.addAll(lecture);
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
                System.out.println(response.body());

//                ArrayList<String> temp = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(response.body());
                System.out.println(jsonObject.get("0").toString().trim());
//                System.out.println(jsonObject.get("1").toString().trim());
//                System.out.println(jsonObject.length());

                ArrayList<Lecture> arr_lec = lectures.getValue();


                for (int i = 0; i < jsonObject.length(); i++) {
                    Lecture lec = arr_lec.get(i);
                    lec.setCapacity_total(jsonObject.get(String.valueOf(i)).toString().trim());
                    arr_lec.set(i, lec);

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
