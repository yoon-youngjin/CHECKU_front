package com.example.nodeproject2;

import android.util.Log;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class NotifyThread implements Runnable{

    Call<String> call;

    public NotifyThread(Call<String> call) {
        this.call = call;
    }
    @SneakyThrows
    @Override
    public void run() {
        try {
            Response<String> execute = call.execute();
            if (execute.code() == 200) {
                Log.d("check", execute.body());
            } else {
                Log.d("fail", "fail");
            }
        }catch (SocketTimeoutException e) {
            Thread.interrupted();
            this.run();
        }
    }

}
