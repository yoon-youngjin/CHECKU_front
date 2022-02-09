package com.example.nodeproject2.view;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.nodeproject2.API.viewmodel.LectureViewModel;
import com.example.nodeproject2.API.LectureDao;
import com.example.nodeproject2.API.LectureDatabase;
import com.example.nodeproject2.R;
import com.example.nodeproject2.adapter.MainAdatper;
import com.example.nodeproject2.databinding.FragmentMainBinding;
import com.example.nodeproject2.datas.Lecture;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private LectureDao lectureDao;
    RecyclerView recyclerView;
    MainAdatper adatper;
    FragmentMainBinding binding;
    LoadingDialog loadingDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LectureViewModel lectureViewModel;
    private String value;
    private int count;
    private boolean dataCheck;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        lectureViewModel = new ViewModelProvider(requireActivity()).get(LectureViewModel.class);
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loadingDialog.show();
        swipeRefreshLayout = binding.swipeLayout;

        LectureDatabase db = Room.databaseBuilder(getContext(), LectureDatabase.class, "my_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();

        lectureDao = db.lectureDao();




        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (dataCheck) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        init();
        initObserver();
        lectureViewModel.getData();
        return binding.getRoot();

    }


    private void init() {





        String[] items = getResources().getStringArray(R.array.spinner_data).clone();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_list_item_1, items
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_list_item_1);
        binding.spinner.setAdapter(adapter2);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    char pos = (char) (position + 96);
                    int id = getResources().getIdentifier(String.valueOf(pos), "string", getContext().getPackageName());
                    value = getResources().getString(id);
                    lectureViewModel.getChangeData(value);
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
                if (count == 0) {
                    showView();
                    loadingDialog.dismiss();
                    count++;
                } else {
                    adatper.swapItems(lectures);
                    dataCheck = true;
                }
            }
        });
    }

    private void showView() {
        recyclerView = binding.mainRecyclerview;
        adatper = new MainAdatper(getContext(), lectureViewModel.getLectures());

//        adatper.setOnItemClickListener(new MainAdatper.OnItemClickListener() {
//            @Override
//            public void OnItemClick(MainAdatper.ViewHolder holder, View view, int pos) {
//                Lecture lecture = Lecture.builder().subject_num(Integer.parseInt(holder.sub_num.getText().toString())).subject_title(holder.sub_title.getText().toString())
//                        .professor_name(holder.pro_name.getText().toString()).capacity_total(holder.capacity_total.getText().toString())
//                        .capacity_year(holder.capacity_year.getText().toString())
//                        .build();
//
//                lectureDao.setInsertLecture(lecture);
//                holder.btn.setBackground();
//
//                System.out.println(lectureDao.getLectureAll());
//
//            }
//        });
        adatper.setOnCheckedChangeListener(new MainAdatper.OnCheckedChangeListener() {
            @Override
            public void OnItemChange(MainAdatper.ViewHolder holder, View view, int pos, boolean isChecked) {


                if (isChecked) {
//                    Lecture lecture = Lecture.builder().subject_num(Integer.parseInt(holder.sub_num.getText().toString())).subject_title(holder.sub_title.getText().toString())
//                            .professor_name(holder.pro_name.getText().toString()).capacity_total(holder.capacity_total.getText().toString())
//                            .capacity_year(holder.capacity_year.getText().toString())
//                            .build();
//                    lectureDao.setInsertLecture(lecture);

                } else {
//                    Lecture lec = Lecture.builder().subject_num(Integer.parseInt(holder.sub_num.getText().toString())).build();;
//                    lectureDao.setDeleteLecture(lec);

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
