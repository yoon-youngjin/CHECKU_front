package com.example.nodeproject2.view;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.room.Room;
import com.example.nodeproject2.API.Lecture.LectureDao;
import com.example.nodeproject2.API.Lecture.LectureDatabase;
import com.example.nodeproject2.API.RetrofitClient;
import com.example.nodeproject2.R;
import com.example.nodeproject2.adapter.ListAdapter;
import com.example.nodeproject2.datas.Lecture;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CodeDialog {

    private Context context;
    final Dialog dlg;
    final Button okButton;
    final Button cancelButton;
    private TextView sbj_title_textView;
    private TextView sbj_num_textView;
    private TextView pro_name_textView;
    private TextView emptysize_textView;
    private Lecture lecture;

    private String sbj_num;


    public interface OnItemClickListener {
        void OnItemClick(Lecture lecture);
    }

    private OnItemClickListener itemClickListener = null;
    private OnItemClickListener item2ClickListener = null;

    // ok
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    //no
    public void setOnItemClickListener2(OnItemClickListener listener) {
        this.item2ClickListener = listener;
    }


    public CodeDialog(Context context) {
        this.context = context;
        this.dlg = new Dialog(context);
        // 액티비티의 타이틀바를 숨긴다.
//        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.code_dialog);

        okButton = dlg.findViewById(R.id.yesBtn);
        cancelButton =  dlg.findViewById(R.id.noBtn);
        sbj_title_textView = dlg.findViewById(R.id.sbj_title);
        sbj_num_textView =  dlg.findViewById(R.id.sbj_num);
        pro_name_textView = dlg.findViewById(R.id.professor);
        emptysize_textView =  dlg.findViewById(R.id.emSize);

        okButton.setOnClickListener(view -> {
            itemClickListener.OnItemClick(lecture);
        });

        cancelButton.setOnClickListener(view -> {
            item2ClickListener.OnItemClick(lecture);
        });

    }


    public void show(String sbj_num) {
        this.sbj_num = sbj_num;
        showData(sbj_num);
    }

    private void showData(String sbj_num) {
        HashMap<String, String[]> map = new HashMap<>();
        String[] data = {sbj_num};
        map.put("sbj_num", data);
        Call<List<Lecture>> call = RetrofitClient.retrofitInterface.excuteChange(map);
        call.enqueue(new Callback<List<Lecture>>() {
            @SneakyThrows
            @Override
            public void onResponse(Call<List<Lecture>> call, Response<List<Lecture>> response) {
                if (response.code() == 200) {
                    ArrayList<Lecture> data = (ArrayList<Lecture>) response.body();
                    ArrayList<Lecture> lectures = (ArrayList<Lecture>) data.stream().map(lecture -> {
                        String total = lecture.getCapacity_total();
                        String[] arr = total.split("/");
                        int emptySize = (Integer.parseInt(arr[1].trim()) - Integer.parseInt(arr[0].trim()));
                        lecture.setEmptySize(emptySize);
                        return lecture;
                    }).collect(Collectors.toList());

                    lecture = lectures.get(0);

                    sbj_title_textView.setText(lecture.getSubject_title());
                    sbj_num_textView.setText(String.valueOf(lecture.getSubject_num()));
                    pro_name_textView.setText(lecture.getProfessor_name());
                    emptysize_textView.setText(String.valueOf(lecture.getEmptySize()));
                    dlg.show();

                } else {
                    Toast.makeText(context,"없는 과목번호입니다.",Toast.LENGTH_SHORT).show();
                    dlg.dismiss();
                    //TODO 변경
                }
            }

            @Override
            public void onFailure(Call<List<Lecture>> call, Throwable t) {
                Log.d("check", "Fail!!");
            }
        });


    }

    public void dismiss() {
        dlg.dismiss();
    }

    public void okClick() {


    }

    private LectureDao getLectureDao() {

        LectureDatabase db = Room.databaseBuilder(context, LectureDatabase.class, "test_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();

        return db.lectureDao();

    }



}