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
import com.example.nodeproject2.API.RetrofitClient;
import com.example.nodeproject2.R;
import com.example.nodeproject2.API.RetrofitInterface;
import com.example.nodeproject2.view.MainActivity;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {


    public static HashMap<String, Call> hmap = new HashMap<>();

    public MyService() {
        hmap = new HashMap<>();
    }

    @SneakyThrows
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent.getStringExtra("init").equals("init")) {
//            getData();
//        }
        if (intent == null) {
            return Service.START_STICKY;
        } else if (intent.getStringExtra("checked").equals("true")) {
            startMonitoring(intent);
        } else if (intent.getStringExtra("checked").equals("false")) {
            System.out.println("check false3");
            stopMonitoring(intent);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void stopMonitoring(Intent intent) {
        String subject_num = intent.getStringExtra("subject_num");
        hmap.get(subject_num).cancel();

        if (hmap.get(subject_num).isCanceled()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("checked", "false");
            map.put("subject_num", subject_num);
            Call<String> call = RetrofitClient.retrofitInterface.excuteClick(map);
            call.enqueue(new Callback<String>() {
                @SneakyThrows
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d("response", response.body() + "stop");

//                    JSONObject obj = new JSONObject(response.body());
//
//                    System.out.println(obj.getString("0"));
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    intent.getStringExtra(response.body());
//                    startActivity(intent);
//                    PendingIntent pendingIntent
//                            = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);


                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("check", "Fail!!");
                }
            });
        } else {
            Log.d("check", "cancel fail");
        }


    }

    private void getData() {
        //TODO 앱시작 데이터 가져오기 구현

        Call<String> call = RetrofitClient.retrofitInterface.excuteInit();
        call.enqueue(new Callback<String>() {
            @SneakyThrows
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println(response.body());

                JSONObject jsonObject = new JSONObject(response.body());
                System.out.println(jsonObject.get("0"));
                System.out.println(jsonObject.get("1"));

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("check", "Fail!!");
            }
        });
        //TODO 액태비티로 데이터 전달 및 새로고침 구현

    }

    private void startMonitoring(Intent intent)  {
            String subject_num = intent.getStringExtra("subject_num");

            HashMap<String, String> map = new HashMap<>();
            map.put("checked", "true");
            map.put("subject_num", subject_num);
            Call<String> call = RetrofitClient.retrofitInterface.excuteClick(map);
            hmap.put(subject_num, call);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 200) {
                        sniping_notification(response);
                    } else if (response.code() == 400) {
                        Log.d("sniping_fail", "fail");
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("err", t.getMessage());
                }
            });
        }



    private void sniping_notification(Response<String> response) {
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
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}