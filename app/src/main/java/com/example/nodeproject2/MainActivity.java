package com.example.nodeproject2;


import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nodeproject2.adapter.MainAdatper;
import com.example.nodeproject2.databinding.ActivityMainBinding;
import com.example.nodeproject2.datas.Subject;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


import java.io.*;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://172.30.1.43:3000";
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

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        initData();
    }


    public void initData() {

        try {

            subData = new ArrayList();
            InputStream is = getResources().getAssets().open("file.csv");
            CSVFile file = new CSVFile(is);
            subData = file.read();
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
            System.out.println("error...");
            e.printStackTrace();
        }
        showView();


    }

    private void showView() {
        recyclerView = binding.mainRecyclerview;
        adatper = new MainAdatper(this, subData);
        Handler handler;
        adatper.setOnItemClickListener(new MainAdatper.OnItemClickListener() {
            @Override
            public void OnItemClick(MainAdatper.ViewHolder holder, View view, int pos) {

//                String sub_title = holder.sub_title.getText().toString();
//                String pro_name = holder.pro_name.getText().toString();
//                String total_num = holder.total_num.getText().toString();
//                String current_num = holder.current_num.getText().toString();
                String subject_num = holder.sub_num.getText().toString();
//                Subject subData = new Subject(sub_title, pro_name, total_num, current_num, subject_num);

                HashMap<String, String> map = new HashMap<>();
//
//                map.put("title", sub_title);
//                map.put("pro_name", pro_name);
//                map.put("total_num", total_num);
//                map.put("current_num", current_num);
                map.put("subject_num", subject_num);

                Call<String> call = retrofitInterface.excuteClick(map);
                NotifyThread nt;
                nt = new NotifyThread(call);
                nt.run();

//                    Thread thread = new Thread(new Runnable() {
//                        Response<String> execute;
//                        @SneakyThrows
//                        @Override
//                        public void run() {
//                            try {
//                                execute = call.clone().execute();
//                                if (execute.code() == 200) {
//                                    Log.d("check", execute.body());
//                                } else {
//                                    Log.d("fail", "fail");
//                                }
//                            }
//                            catch (SocketTimeoutException e) {
//                                this.run();
//                            }
//                        }
//                    });


//                    Thread.sleep(3000);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.code() == 200) {
//                                response.wait();
                                Toast.makeText(MainActivity.this, response.body(), Toast.LENGTH_LONG).show();
                            } else if (response.code() == 400) {

                                Toast.makeText(MainActivity.this, "Already registered", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adatper);

    }
}