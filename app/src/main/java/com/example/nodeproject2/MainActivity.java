package com.example.nodeproject2;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nodeproject2.adapter.MainAdatper;
import com.example.nodeproject2.databinding.ActivityMainBinding;
import com.example.nodeproject2.datas.Subject;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


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
        initData();

    }

    public void initData() {

        try {
            subData = new ArrayList();
            InputStream is = getResources().getAssets().open("maindata.xls");
            Workbook wb = new HSSFWorkbook(is);
            Sheet sheet = wb.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();

            for (int i = 0; i < rows; i++) {
                HSSFRow row = (HSSFRow) sheet.getRow(i);
                Subject sub;
                if(row!=null) {
                    int cells = row.getPhysicalNumberOfCells();
                    sub = new Subject(row.getCell(7).getStringCellValue(),row.getCell(16).getStringCellValue(),null,null,row.getCell(6).getStringCellValue());
                    subData.add(sub);
                }
            }
        } catch (IOException  e) {
            System.out.println("error...");
            e.printStackTrace();
        }


        showView();
//        for (Subject sub : subData) {
//            System.out.println(sub.getSubject_num());
//        }

    }

    private void showView() {
        recyclerView = binding.mainRecyclerview;
        adatper = new MainAdatper(this,subData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adatper);



    }
}