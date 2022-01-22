package com.example.nodeproject2;


import android.view.View;
import android.widget.Toast;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://172.30.1.35:3000";
    ActivityMainBinding binding = null;
    ArrayList<Subject> subData;
    MainAdatper adatper;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

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
                    sub = new Subject(row.getCell(7).getStringCellValue(),row.getCell(16).getStringCellValue(),"40","0",row.getCell(6).getStringCellValue());
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
        adatper.setOnItemClickListener(new MainAdatper.OnItemClickListener() {
            @Override
            public void OnItemClick(MainAdatper.ViewHolder holder, View view, int pos) {



                String sub_title = holder.sub_title.getText().toString();
                String pro_name = holder.pro_name.getText().toString();
                String total_num = holder.total_num.getText().toString();
                String current_num = holder.current_num.getText().toString();
                String subject_num = holder.sub_num.getText().toString();
//                Subject subData = new Subject(sub_title, pro_name, total_num, current_num, subject_num);


                HashMap<String, String> map = new HashMap<>();

                map.put("title", sub_title);
                map.put("pro_name", pro_name);
                map.put("total_num", total_num);
                map.put("current_num", current_num);
                map.put("subject_num", subject_num);


                Call<Void> call = retrofitInterface.excuteClick(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code()== 200) {
                            Toast.makeText(MainActivity.this, "Signed up successfully", Toast.LENGTH_LONG).show();

                        }
                        else if(response.code() == 400) {
                            Toast.makeText(MainActivity.this, "Already registered", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        recyclerView.setAdapter(adatper);



    }
}