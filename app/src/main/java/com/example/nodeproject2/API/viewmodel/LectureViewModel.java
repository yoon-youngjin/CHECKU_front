package com.example.nodeproject2.API.viewmodel;

import android.app.Dialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.nodeproject2.API.RetrofitClient;
import com.example.nodeproject2.datas.Lecture;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.*;
import java.util.stream.Collectors;

public class LectureViewModel extends ViewModel {

    /**
     * 수강바구니, 전공 과목, 교양 과목을 위한 LiveData
     */
    public MutableLiveData<ArrayList<Lecture>> myLectures
            = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Lecture>> lectures
            = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Lecture>> liberalArts
            = new MutableLiveData<>();

    /**
     * 404 요청 시 dlg dismiss를 위한 LiveData
     */
    public MutableLiveData<Integer> status = new MutableLiveData<>();


    public ArrayList<Lecture> getLectures() {
        return lectures.getValue();
    }

    public ArrayList<Lecture> getLiberalArts() {
        return liberalArts.getValue();
    }

    /**
     * 수강신청
     */
    public void letsRegister(String sbj_num) {
        HashMap<String, String> map = new HashMap<>();
        map.put("sbj_num", sbj_num);

        Call<String> call = RetrofitClient.retrofitInterface.excuteRegister(map);
        call.enqueue(new Callback<String>() {
            @SneakyThrows
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //TODO 수강신청 처리 개선
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("check", "Fail!!");
            }
        });
    }

    /**
     * 선택한 학부에 대한 모든 데이터 get
     * @param departmentId : 학부 아이디
     * @param type         : 필터링을 위한 타입 변수, 사용 x
     * @param category     : category가 subject인 경우 전공, 아닌 경우 교양 처리
     */
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
                    ArrayList<Lecture> updateData = addEmptyLecture((ArrayList<Lecture>) response.body());

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
        /**
         * 데이터 가공
         */
        String[] data = lectureList.stream().map(lecture ->
             String.format("%04d", lecture.getSubject_num())
        ).toArray(String[]::new);

        HashMap<String, String[]> map = new HashMap<>();
        map.put("sbj_num", data);
        Call<List<Lecture>> call = RetrofitClient.retrofitInterface.excuteChange(map);
        call.enqueue(new Callback<List<Lecture>>() {
            @SneakyThrows
            @Override
            public void onResponse(Call<List<Lecture>> call, Response<List<Lecture>> response) {
                if (response.code() == 200) {
                    ArrayList<Lecture> lectures = addEmptyLecture((ArrayList<Lecture>) response.body());
                    myLectures.setValue(lectures);
                } else {
                    //TODO 변경
                    status.setValue(response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Lecture>> call, Throwable t) {
                Log.d("check", "Fail!!");
            }
        });
    }

    private ArrayList<Lecture> addEmptyLecture(ArrayList<Lecture> data) {
        return (ArrayList<Lecture>) data.stream().map(lecture -> {
            String total = lecture.getCapacity_total();
            String[] arr = total.split("/");
            int emptySize = (Integer.parseInt(arr[1].trim()) - Integer.parseInt(arr[0].trim()));
            lecture.setEmptySize(emptySize);
            return lecture;
        }).collect(Collectors.toList());
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
