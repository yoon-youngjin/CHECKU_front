package com.example.nodeproject2.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.example.nodeproject2.LectureDao;
import com.example.nodeproject2.LectureDatabase;
import com.example.nodeproject2.R;
import com.example.nodeproject2.adapter.ListAdapter;
import com.example.nodeproject2.adapter.MainAdatper;
import com.example.nodeproject2.databinding.FragmentListBinding;
import com.example.nodeproject2.databinding.FragmentMainBinding;
import com.example.nodeproject2.datas.Lecture;

public class ListFragment extends Fragment {
    private LectureDao lectureDao;
    RecyclerView recyclerView;
    ListAdapter adatper;
    FragmentListBinding binding;



    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        LectureDatabase db = Room.databaseBuilder(getContext(), LectureDatabase.class, "my_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();

        lectureDao = db.lectureDao();
        init();

        return binding.getRoot();
    }

    private void init() {
        recyclerView = binding.listRecyclerview;
        adatper = new ListAdapter(getContext(), lectureDao.getLectureAll());

//        adatper.setOnItemClickListener(new MainAdatper.OnItemClickListener() {
//            @Override
//            public void OnItemClick(MainAdatper.ViewHolder holder, View view, int pos) {
////                Lecture lecture1 = Lecture.builder().subject_num(Integer.parseInt(holder.sub_num.toString())).subject_title(holder.sub_title.toString())
////                        .professor_name(holder.pro_name.toString()).capacity_total(holder.capacity_total.toString()).capacity_year(holder.capacity_year.toString())
////                        .build();
//                Lecture lecture = new Lecture();
//                lecture.setSubject_num(Integer.parseInt(holder.sub_num.getText().toString()));
//                lecture.setProfessor_name(holder.pro_name.getText().toString());
//                lecture.setSubject_title(holder.sub_title.getText().toString());
//                lecture.setCapacity_total(holder.capacity_total.getText().toString());
//                lecture.setCapacity_year(holder.capacity_year.getText().toString());
//
//
//                lectureDao.setInsertLecture(lecture);
//
//                System.out.println(lectureDao.getLectureAll());
//
//            }
//        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adatper);
    }

}