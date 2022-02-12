package com.example.nodeproject2.view;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.nodeproject2.API.viewmodel.LectureViewModel;
import com.example.nodeproject2.API.Lecture.LectureDao;
import com.example.nodeproject2.API.Lecture.LectureDatabase;
import com.example.nodeproject2.R;
import com.example.nodeproject2.adapter.MainAdatper;
import com.example.nodeproject2.databinding.FragmentMainBinding;
import com.example.nodeproject2.datas.Lecture;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private LectureDao lectureDao;
    private RecyclerView recyclerView;
    private MainAdatper adatper;
    private FragmentMainBinding binding;
    private LoadingDialog loadingDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LectureViewModel lectureViewModel;
    private String value;
    private ArrayAdapter<String> adapter2;
    private String current_grade = "";
    private ArrayList<Lecture> maindata;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater, container, false);
        lectureViewModel = new ViewModelProvider(requireActivity()).get(LectureViewModel.class);
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        swipeRefreshLayout = binding.swipeLayout;

        lectureDao = getLectureDao();
        maindata = new ArrayList<>();

        init();
        initObserver();
        return binding.getRoot();
    }


    private void init() {
        showView();
        String[] items = getResources().getStringArray(R.array.spinner_data).clone();
        adapter2 = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_list_item_1, items
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_list_item_1);
        binding.spinner.setAdapter(adapter2);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    loadingDialog.show();
                    char pos = (char) (position + 64);
                    int id = getResources().getIdentifier(String.valueOf(pos), "string", getContext().getPackageName());
                    value = getResources().getString(id);
                    lectureViewModel.getChangeAllData(value);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        binding.findlectureEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().equals("")) {
                    adatper.getFilter().filter("");
                } else {
                    adatper.getFilter().filter(s.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingDialog.show();
                lectureViewModel.getChangeAllData(value);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int ckeckedID) {
                String edit_text = binding.findlectureEdittext.getText().toString();

                switch (ckeckedID) {
                    case R.id.radioButton1:
                        //TODO 속도비교후 변경
                        current_grade = "";
                        adatper.swapItems(maindata,edit_text,current_grade);
                        break;
                    case R.id.radioButton2:
                        current_grade = "1";
                        adatper.swapItems(maindata,edit_text,current_grade);

                        break;
                    case R.id.radioButton3:
                        current_grade = "2";
                        adatper.swapItems(maindata,edit_text,current_grade);
                        break;
                    case R.id.radioButton4:
                        current_grade = "3";
                        adatper.swapItems(maindata,edit_text,current_grade);
                        break;
                    case R.id.radioButton5:
                        current_grade = "4";
                        adatper.swapItems(maindata,edit_text,current_grade);
//                        radioBtnClick("4");
                        break;
                }
            }
        });

    }

//    private void radioBtnClick(String grade) {
//        Call<List<Lecture>> call = RetrofitClient.retrofitInterface.excuteChangeAll(value, grade);
//        call.enqueue(new Callback<List<Lecture>>() {
//            @SneakyThrows
//            @Override
//            public void onResponse(Call<List<Lecture>> call, Response<List<Lecture>> response) {
//                if (response.code() == 200) {
//                    ArrayList<Lecture> data = (ArrayList<Lecture>) response.body();
//                    adatper.swapItems(data, "", current_grade);
//                } else {
//                    //TODO 변경
//                }
//            }
//            @Override
//            public void onFailure(Call<List<Lecture>> call, Throwable t) {
//                Log.d("check", "Fail!!");
//            }
//        });
//    }

    private void initObserver() {
        lectureViewModel.lectures.observe(this, new Observer<ArrayList<Lecture>>() {
            @Override
            public void onChanged(ArrayList<Lecture> lectures) {
                String current_text = binding.findlectureEdittext.getText().toString();
                maindata = lectures;
                adatper.swapItems(lectures, current_text ,current_grade);
                loadingDialog.dismiss();
            }
        });
    }

    private void showView() {
        recyclerView = binding.mainRecyclerview;
        adatper = new MainAdatper(getContext(), lectureViewModel.getLectures());

        adatper.setOnItemClickListener(new MainAdatper.OnItemClickListener() {
            @Override
            public void OnItemClick(MainAdatper.ViewHolder holder, View view, int pos) {

                List<Lecture> lectures = lectureDao.getLectureAll();
                Lecture lecture = Lecture.builder().subject_num(Integer.parseInt(holder.sub_num.getText().toString())).build();


                if(lectures.contains(lecture)) {
                    lectureDao.setDeleteLecture(lecture);
                    holder.btn.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);

                }
                else {
                    Lecture lecture2 = Lecture.builder().subject_num(Integer.parseInt(holder.sub_num.getText().toString())).subject_title(holder.sub_title.getText().toString())
                            .professor_name(holder.pro_name.getText().toString()).capacity_total(holder.capacity_total.getText().toString())
                            .capacity_year(holder.capacity_year.getText().toString())
                            .build();

                    lectureDao.setInsertLecture(lecture2);
                    holder.btn.setBackgroundResource(R.drawable.ic_baseline_favorite_24);


                }

            }
        });
//        adatper.setOnCheckedChangeListener(new MainAdatper.OnCheckedChangeListener() {
//            @Override
//            public void OnItemChange(MainAdatper.ViewHolder holder, View view, int pos, boolean isChecked) {
//
//
//                if (isChecked) {
////                    Lecture lecture = Lecture.builder().subject_num(Integer.parseInt(holder.sub_num.getText().toString())).subject_title(holder.sub_title.getText().toString())
////                            .professor_name(holder.pro_name.getText().toString()).capacity_total(holder.capacity_total.getText().toString())
////                            .capacity_year(holder.capacity_year.getText().toString())
////                            .build();
////                    lectureDao.setInsertLecture(lecture);
//
//                } else {
////                    Lecture lec = Lecture.builder().subject_num(Integer.parseInt(holder.sub_num.getText().toString())).build();;
////                    lectureDao.setDeleteLecture(lec);
//
//                }
//            }
//        });
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
    private LectureDao getLectureDao() {

        LectureDatabase db = Room.databaseBuilder(getContext(), LectureDatabase.class, "my_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();

        return db.lectureDao();

    }


}
