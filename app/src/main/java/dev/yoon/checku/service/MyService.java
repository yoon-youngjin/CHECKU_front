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
import com.example.nodeproject2.view.MainActivity;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.HashMap;

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

        // ????????? ???????????? ?????? ????????? ?????? ????????? ????????? Notification??? ???????????? ??????.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel", "play!!",
                    NotificationManager.IMPORTANCE_DEFAULT);

            // Notification??? ?????? ??????
            NotificationManager mNotificationManager = ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));
            mNotificationManager.createNotificationChannel(channel);

            // Notification ??????
            NotificationCompat.Builder notification
                    = new NotificationCompat.Builder(getApplicationContext(), "channel")
//                    .setSmallIcon(R.drawable.img)
                    .setContentTitle(response.body())
                    .setContentIntent(pendingIntent)
                    .setContentText("");

            // id ?????? 0?????? ??? ????????? ???????????? ??????.
            mNotificationManager.notify(1, notification.build());
            // foreground?????? ??????
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