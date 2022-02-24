package com.example.nodeproject2.API.viewmodel;

import android.util.Log;
import android.widget.Toast;
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
import java.util.*;
import java.util.stream.Collectors;

public class LectureViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Lecture>> lectures
            = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Lecture>> myLectures
            = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Lecture>> liberalArts
            = new MutableLiveData<>();
    public MutableLiveData<Integer> status = new MutableLiveData<>();


    public ArrayList<Lecture> getLectures() {
        return lectures.getValue();
    }

    public ArrayList<Lecture> getLiberalArts() {
        return liberalArts.getValue();
    }


    public void getChangeAllData(String departmentId, String type, String category) {
        Map<String, String> map = new HashMap<>();
        map.put("departmentId", departmentId);
        map.put("type", type);

        Call<List<Lecture>> call = RetrofitClient.retrofitInterface.excuteChangeAll(map);
        call.enqueue(new Callback<List<Lecture>>() {
            @SneakyThrows
            @Override
            public void onResponse(Call<List<Lecture>> call, Response<List<Lecture>> response) {

                if (response.code() == 200) {
                    ArrayList<Lecture> data = (ArrayList<Lecture>) response.body();
                    ArrayList<Lecture> updateData = (ArrayList<Lecture>) data.stream().map(lecture -> {
                        String total = lecture.getCapacity_total();
                        String[] arr = total.split("/");
                        int emptySize = (Integer.parseInt(arr[1].trim()) - Integer.parseInt(arr[0].trim()));
                        lecture.setEmptySize(emptySize);
                        return lecture;
                    }).collect(Collectors.toList());

                    if (category.equals("subject")) {
                        lectures.setValue(updateData);
                    } else {
                        liberalArts.setValue(updateData);
                    }
                } else {
                    status.setValue(response.code());
                }

            }

            @Override
            public void onFailure(Call<List<Lecture>> call, Throwable t) {
                Log.d("check", "Fail!!");
            }
        });
    }


    public void getChangeData(List<Lecture> lectureList) {
        HashMap<String, String[]> map = new HashMap<>();
        String[] data = new String[lectureList.size()];
        int i = 0;
        Iterator<Lecture> it = lectureList.iterator();
        while (it.hasNext()) {
            int sbj_num = it.next().getSubject_num();
            data[i++] = String.format("%04d", sbj_num);
        }
        map.put("sbj_num", data);
        Call<List<Lecture>> call = RetrofitClient.retrofitInterface.excuteChange(map);
        call.enqueue(new Callback<List<Lecture>>() {
            @SneakyThrows
            @Override
            public void onResponse(Call<List<Lecture>> call, Response<List<Lecture>> response) {
                if (response.code() == 200) {
                    ArrayList<Lecture> data = (ArrayList<Lecture>) response.body();

                    ArrayList<Lecture> lectures = (ArrayList<Lecture>) data.stream().map(lecture -> {
                        String total = lecture.getCapacity_total();
                        String[] arr = total.split("/");
                        int emptySize = (Integer.parseInt(arr[1].trim()) - Integer.parseInt(arr[0].trim()));
                        lecture.setEmptySize(emptySize);
                        return lecture;
                    }).collect(Collectors.toList());

                    myLectures.setValue(lectures);
                } else {
                    //TODO 변경
                    status.setValue(response.code());
                }

//                JSONObject jsonObject = new JSONObject(response.body());
//                ArrayList<Lecture> arr_lec = new ArrayList<>();
//                for (int i = 0; i < jsonObject.length(); i++) {
//                    JSONArray temp = (JSONArray) jsonObject.get(String.valueOf(i));
//                    Lecture lec = Lecture.builder().capacity_total(temp.get(3).toString().trim()).capacity_year(temp.get(4).toString().trim())
//                            .professor_name(temp.get(1).toString().trim()).subject_title(temp.get(2).toString().trim()).subject_num(Integer.parseInt(temp.get(0).toString())).build();
//                    arr_lec.add(lec);
//                }

            }

            @Override
            public void onFailure(Call<List<Lecture>> call, Throwable t) {
                Log.d("check", "Fail!!");
            }
        });
    }


}
