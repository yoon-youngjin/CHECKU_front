package com.example.nodeproject2.view;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.*;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nodeproject2.API.viewmodel.LectureViewModel;
import com.example.nodeproject2.CSVFile;
import com.example.nodeproject2.service.MyService;
import com.example.nodeproject2.adapter.MainAdatper;
import com.example.nodeproject2.databinding.ActivityMainBinding;
import com.example.nodeproject2.datas.Lecture;


import java.io.*;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding = null;
    ArrayList<Lecture> lectures;
    MainAdatper adatper;
    RecyclerView recyclerView;
    LectureViewModel lectureViewModel;
    LoadingDialog loadingDialog;
    int count = 0 ;

    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        loadingDialog = new LoadingDialog(this);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loadingDialog.show();


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        lectureViewModel = new ViewModelProvider(this).get(LectureViewModel.class);

        initData();
        initObserver();
        lectureViewModel.getData();
//        Intent intent = new Intent(this,MyService.class);
//        intent.putExtra("init", "init");
//        startService(intent);
    }

    private void initObserver() {
        lectureViewModel.lectures.observe(this, new Observer<ArrayList<Lecture>>() {
            @Override
            public void onChanged(ArrayList<Lecture> lectures) {
                count++;
                Log.d("change", "change");
                adatper.notifyDataSetChanged();
                setContentView(binding.getRoot());
                if(count == 2) {
                    loadingDialog.dismiss();

                }
            }
        });
    }


    public void initData() {

        try (InputStream is = getResources().getAssets().open("file.csv");
        ) {
            lectures = new ArrayList();
            CSVFile file = new CSVFile(is);
            lectures = file.read();
            lectureViewModel.setLectures(lectures);

//            lectureViewModel.getData();

            // 엑셀 읽기
//            Workbook wb = new HSSFWorkbook(is);
//            Sheet sheet = wb.getSheetAt(0);
//            int rows = sheet.getPhysicalNumberOfRows();
//
//            for (int i = 0; i < rows; i++) {
//                HSSFRow row = (HSSFRow) sheet.getRow(i);
//                Subject sub;
//                if(row!=null) {
////                    int cells = row.getPhysicalNumberOfCells();
//                    sub = Subject.builder().subject_title(row.getCell(7).getStringCellValue()).professor_name(row.getCell(16).getStringCellValue())
//                            .total_num("40").current_num("0").subject_num(row.getCell(6).getStringCellValue()).build();
//                    subData.add(sub);
//                }
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        showView();

    }

    private void showView() {
        recyclerView = binding.mainRecyclerview;
        adatper = new MainAdatper(this, lectureViewModel.getLectures());
        adatper.setOnCheckedChangeListener(new MainAdatper.OnCheckedChangeListener() {
            @Override
            public void OnItemChange(MainAdatper.ViewHolder holder, View view, int pos, boolean isChecked) throws IOException {
                Intent intent = new Intent(getApplicationContext(), MyService.class);
                String subject_num = holder.sub_num.getText().toString();
                intent.putExtra("subject_num", subject_num);
                if (isChecked) {
                    intent.putExtra("checked", true);
                    startService(intent);
                } else {
                    intent.putExtra("checked", false);
                    startService(intent);
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adatper);
    }
}