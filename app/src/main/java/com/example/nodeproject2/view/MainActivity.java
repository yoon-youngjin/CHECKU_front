package com.example.nodeproject2.view;


import android.content.Intent;
import android.os.*;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nodeproject2.CSVFile;
import com.example.nodeproject2.service.MyService;
import com.example.nodeproject2.adapter.MainAdatper;
import com.example.nodeproject2.databinding.ActivityMainBinding;
import com.example.nodeproject2.datas.Subject;


import java.io.*;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding = null;
    ArrayList<Subject> subData;
    MainAdatper adatper;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        initData();
    }


    public void initData() {

        try {
            subData = new ArrayList();
            InputStream is = getResources().getAssets().open("file.csv");
            CSVFile file = new CSVFile(is);
            subData = file.read();
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
        adatper = new MainAdatper(this, subData);
        adatper.setOnItemClickListener(new MainAdatper.OnItemClickListener() {
            @Override
            public void OnItemClick(MainAdatper.ViewHolder holder, View view, int pos) {

                String subject_num = holder.sub_num.getText().toString();

                Intent intent = new Intent(getApplicationContext(), MyService.class);
                intent.putExtra("subject_num", subject_num);
                startService(intent);


            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adatper);

    }
}