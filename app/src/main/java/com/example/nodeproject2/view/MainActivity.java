package com.example.nodeproject2.view;


import android.content.Intent;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nodeproject2.API.RetrofitInterface;
import com.example.nodeproject2.CSVFile;
import com.example.nodeproject2.service.MyService;
import com.example.nodeproject2.adapter.MainAdatper;
import com.example.nodeproject2.databinding.ActivityMainBinding;
import com.example.nodeproject2.datas.Subject;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding = null;
    ArrayList<Subject> subData;
    MainAdatper adatper;
    RecyclerView recyclerView;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://172.30.1.30:2000";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.DAYS)
                .readTimeout(1, TimeUnit.DAYS)
//                .writeTimeout(100, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        initData();
//        Intent intent = new Intent(this,MyService.class);
//        intent.putExtra("init", "init");
//        startService(intent);
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
        adatper.setOnCheckedChangeListener(new MainAdatper.OnCheckedChangeListener() {
            @Override
            public void OnItemChange(MainAdatper.ViewHolder holder, View view, int pos,boolean isChecked) throws IOException {
                Log.d("checked", String.valueOf(isChecked));
                if(isChecked) {
                    String subject_num = holder.sub_num.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), MyService.class);
                    intent.putExtra("subject_num", subject_num);
                    startService(intent);
                }
                else {
                    //TODO 보낸 요청 취소
                    String subject_num = holder.sub_num.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), MyService.class);
                    intent.putExtra("test", "test");
                    intent.putExtra("subject_num", subject_num);
                    startService(intent);

                }

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adatper);

    }
}