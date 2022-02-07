package com.example.nodeproject2.view;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nodeproject2.API.RetrofitClient;
import com.example.nodeproject2.API.viewmodel.LectureViewModel;
import com.example.nodeproject2.CSVFile;
import com.example.nodeproject2.adapter.MainAdatper;
import com.example.nodeproject2.databinding.FragmentMainBinding;
import com.example.nodeproject2.datas.Lecture;
import com.example.nodeproject2.service.MyService;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainFragment extends Fragment {

    RecyclerView recyclerView;
    MainAdatper adatper;
    FragmentMainBinding binding;
    LoadingDialog loadingDialog;
    private LectureViewModel lectureViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
//        System.out.println(lectureViewModel);
        lectureViewModel = new ViewModelProvider(requireActivity()).get(LectureViewModel.class);
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loadingDialog.show();
        init();
//        initData();
        initObserver();

        lectureViewModel.getData();

        return binding.getRoot();

    }

    private void init() {

        String[] items = {"1", "2"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_list_item_1,items
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_list_item_1);
        binding.spinner.setAdapter(adapter2);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                    {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("test", "127428");
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

                                adatper.swapItems(arr_lec);


                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d("check", "Fail!!");
                            }
                        });
                        break;
                }
                    case 1:{
                        HashMap<String, String> map = new HashMap<>();
                        map.put("test", "126914");
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
//                                lectureViewModel.setLectures(arr_lec);
                                adatper.swapItems(arr_lec);

                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d("check", "Fail!!");
                            }
                        });
                        break;
                }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void initObserver() {
        lectureViewModel.lectures.observe(this, new Observer<ArrayList<Lecture>>() {
            @Override
            public void onChanged(ArrayList<Lecture> lectures) {
                showView();

//                lectureViewModel.setLectures(lectures);
//                System.out.println("check2"+lectures);
//
//                System.out.println("check3"+lectureViewModel.getLectures());

                loadingDialog.dismiss();

            }
        });
    }

    private void showView() {

        recyclerView = binding.mainRecyclerview;
        adatper = new MainAdatper(getContext(), lectureViewModel.getLectures());
        adatper.setOnCheckedChangeListener(new MainAdatper.OnCheckedChangeListener() {
            @Override
            public void OnItemChange(MainAdatper.ViewHolder holder, View view, int pos, boolean isChecked) {

                Intent intent = new Intent(getContext(), MyService.class);
                String subject_num = holder.sub_num.getText().toString();
                intent.putExtra("subject_num", subject_num);
                if (isChecked) {
                    intent.putExtra("checked", "true");

                    getActivity().startService(intent);
                } else {
                    intent.putExtra("checked", "false");
                    getActivity().startService(intent);
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adatper);

    }

//    public void initData() {
//
//        try (InputStream is = getResources().getAssets().open("file.csv");
//        ) {
//            lectures = new ArrayList();
//            CSVFile file = new CSVFile(is);
//            lectures = file.read();
//            lectureViewModel.setLectures(lectures);
//
//            // 엑셀 읽기
////            Workbook wb = new HSSFWorkbook(is);
////            Sheet sheet = wb.getSheetAt(0);
////            int rows = sheet.getPhysicalNumberOfRows();
////
////            for (int i = 0; i < rows; i++) {
////                HSSFRow row = (HSSFRow) sheet.getRow(i);
////                Subject sub;
////                if(row!=null) {
//////                    int cells = row.getPhysicalNumberOfCells();
////                    sub = Subject.builder().subject_title(row.getCell(7).getStringCellValue()).professor_name(row.getCell(16).getStringCellValue())
////                            .total_num("40").current_num("0").subject_num(row.getCell(6).getStringCellValue()).build();
////                    subData.add(sub);
////                }
////            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        showView();
//
//    }


}
