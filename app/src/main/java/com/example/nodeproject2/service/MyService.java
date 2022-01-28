package com.example.nodeproject2.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.example.nodeproject2.R;
import com.example.nodeproject2.API.RetrofitInterface;
import com.example.nodeproject2.view.MainActivity;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://172.30.1.30:2000";
    public static ArrayList<Call> callArrayList = new ArrayList<>();


    public MyService() {
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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return Service.START_STICKY;
        }
//        else if (intent.getStringExtra("init").equals("init")) {
//            getData();
//
//        }
        else {
            startMonitoring(intent);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void getData() {
        //TODO 앱시작 데이터 가져오기 구현

//        Call<String> call = retrofitInterface.excuteClick(map);

    }

    private void startMonitoring(Intent intent) {
        String subject_num = intent.getStringExtra("subject_num");

        HashMap<String, String> map = new HashMap<>();
        map.put("subject_num", subject_num);
        Call<String> call = retrofitInterface.excuteClick(map);
        callArrayList.add(call);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    Intent testIntent = new Intent(getApplicationContext(), MainActivity.class);
                    PendingIntent pendingIntent
                            = PendingIntent.getActivity(getApplicationContext(), 0, testIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    // 오래오 윗버젼일 때는 아래와 같이 채널을 만들어 Notification과 연결해야 한다.
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("channel", "play!!",
                                NotificationManager.IMPORTANCE_DEFAULT);

                        // Notification과 채널 연걸
                        NotificationManager mNotificationManager = ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));
                        mNotificationManager.createNotificationChannel(channel);

                        // Notification 세팅
                        NotificationCompat.Builder notification
                                = new NotificationCompat.Builder(getApplicationContext(), "channel")
                                .setSmallIcon(R.drawable.img)
                                .setContentTitle(response.body())
                                .setContentIntent(pendingIntent)
                                .setContentText("");

                        // id 값은 0보다 큰 양수가 들어가야 한다.
                        mNotificationManager.notify(1, notification.build());
                        // foreground에서 시작
                        startForeground(1, notification.build());
                        Log.d("check", response.body());
                    }
                } else if (response.code() == 400) {
//                        Toast.makeText(MainActivity.this, "Already registered", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("err", t.getMessage());
            }
        });

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}