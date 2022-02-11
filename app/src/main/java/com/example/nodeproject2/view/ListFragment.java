package com.example.nodeproject2.view;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.nodeproject2.API.LectureDao;
import com.example.nodeproject2.API.LectureDatabase;
import com.example.nodeproject2.API.RetrofitClient;
import com.example.nodeproject2.API.viewmodel.LectureViewModel;
import com.example.nodeproject2.adapter.ListAdapter;
import com.example.nodeproject2.databinding.FragmentListBinding;
import com.example.nodeproject2.datas.Lecture;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    private LectureDao lectureDao;
    RecyclerView recyclerView;
    ListAdapter adatper;
    FragmentListBinding binding;
    LoadingDialog loadingDialog;
    private LectureViewModel lectureViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

//    @Override
//    public void onResume() {
//        super.onResume();
//        lectureViewModel.getChangeData(lectureDao.getLectureAll());
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        swipeRefreshLayout = binding.swipeLayout;
        lectureViewModel = new ViewModelProvider(requireActivity()).get(LectureViewModel.class);
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LectureDatabase db = Room.databaseBuilder(getContext(), LectureDatabase.class, "my_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();

        lectureDao = db.lectureDao();
        init();
        initObserver();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO 변경
                loadingDialog.show();
                lectureViewModel.getChangeData(lectureDao.getLectureAll());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return binding.getRoot();
    }

    private void initObserver() {
        lectureViewModel.myLectures.observe(this, new Observer<ArrayList<Lecture>>() {
            @Override
            public void onChanged(ArrayList<Lecture> lectures) {
                adatper.swapItems(lectures);
                loadingDialog.dismiss();
            }
        });
    }

    private void init() {
        recyclerView = binding.listRecyclerview;
        adatper = new ListAdapter(getContext(), lectureDao.getLectureAll());

        adatper.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(ListAdapter.ViewHolder holder, View view, int pos) {
                Lecture lecture = new Lecture();
                lecture.setSubject_num(Integer.parseInt(holder.sub_num.getText().toString()));
                lectureDao.setDeleteLecture(lecture);
                adatper.swapItems(lectureDao.getLectureAll());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adatper);
    }

}