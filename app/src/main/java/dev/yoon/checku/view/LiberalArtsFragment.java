package com.example.nodeproject2.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.nodeproject2.API.viewmodel.LectureViewModel;
import com.example.nodeproject2.R;
import com.example.nodeproject2.adapter.LiberalArtsAdapter;
import com.example.nodeproject2.config.BuildBallon;
import com.example.nodeproject2.config.BuildLecturDatabase;
import com.example.nodeproject2.databinding.FragmentLiberalArtsBinding;
import com.example.nodeproject2.datas.Lecture;
import com.skydoves.balloon.*;

import java.util.ArrayList;
import java.util.List;

public class LiberalArtsFragment extends Fragment {
    private FragmentLiberalArtsBinding binding;

    private RecyclerView recyclerView;
    private LiberalArtsAdapter adatper;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BuildLecturDatabase lecturDatabase;
    private LectureViewModel lectureViewModel;

    private LoadingDialog loadingDialog;

    private ArrayList<Lecture> originData = new ArrayList<>();

    private boolean empty_checked = false;
    private String type = "";

    private Balloon balloon;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLiberalArtsBinding.inflate(inflater, container, false);
        lecturDatabase = new BuildLecturDatabase(getContext(), "lecture_table");

        swipeRefreshLayout = binding.swipeLayout;

        lectureViewModel = new ViewModelProvider(requireActivity()).get(LectureViewModel.class);
        balloon = BuildBallon.getBalloon(getContext(), getViewLifecycleOwner(),
                "1. ??????????????? ????????? ?????????????????? ????????? ??? ?????????.\n\n2. ????????? ????????? ????????? ?????? ???????????? ??? ??? ?????????."
        );

        initRecyclerView();
        init();
        initSpinner();
        initDialog();
        initObserver();

        return binding.getRoot();
    }


    private void init() {
        /**
         * ?????? ?????? ??????
         */
        adatper.setOnItemClickListener(new LiberalArtsAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(LiberalArtsAdapter.ViewHolder holder, View view, int pos) {
                insertDeleteSbj(holder);
            }
        });
        /**
         * ?????? ??????
         */
        binding.tooltipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                balloon.showAlignBottom(binding.tooltipbtn);
            }
        });
        /**
         * ?????? ????????? ?????????
         */
        binding.findlectureEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    adatper.getFilter().filter("");
                } else {
                    adatper.getFilter().filter(s.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        /**
         * ????????????
         */
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (type.equals("")) {
                    Toast.makeText(getContext(), "???????????????.", Toast.LENGTH_SHORT).show();
                } else {
                    loadingDialog.show();
                    lectureViewModel.getChangeAllData("", type, "liberalArts");
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        /**
         * ????????? ??????
         */
        binding.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            String edit_text = binding.findlectureEdittext.getText().toString();
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                empty_checked = checked;
                adatper.swapItems(originData, edit_text, empty_checked);
            }
        });


    }

    private void insertDeleteSbj(LiberalArtsAdapter.ViewHolder holder) {
        List<Lecture> lectures = lecturDatabase.getLecturesFromDB();

        Lecture lecture = Lecture.createLectureFromArts(holder);

        if (lectures.contains(lecture)) {
            holder.btn.setBackgroundResource(R.drawable.btn_favorite_off);
            holder.sub_num.setTextColor(Color.BLACK);
            holder.type.setTextColor(Color.BLACK);
            holder.year.setTextColor(Color.BLACK);
            lecturDatabase.setDeleteLecture(lecture);
            Toast.makeText(getContext(), lecture.getSubject_title() + "???(???) ?????? ?????????????????????.", Toast.LENGTH_SHORT).show();
        } else {
            holder.btn.setBackgroundResource(R.drawable.btn_favorite_on);
            holder.sub_num.setTextColor(Color.WHITE);
            holder.type.setTextColor(Color.WHITE);
            holder.year.setTextColor(Color.WHITE);
            lecturDatabase.setInsertLecture(lecture);
            Toast.makeText(getContext(), lecture.getSubject_title() + "???(???) ?????? ???????????????.", Toast.LENGTH_SHORT).show();
        }
    }


    private void initObserver() {
        lectureViewModel.liberalArts.observe(this, new Observer<ArrayList<Lecture>>() {
            @Override
            public void onChanged(ArrayList<Lecture> lectures) {
                originData = lectures;
                String current_text = binding.findlectureEdittext.getText().toString();
                adatper.swapItems(lectures, current_text, empty_checked);
                loadingDialog.dismiss();
            }
        });

        lectureViewModel.status.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                loadingDialog.dismiss();
            }
        });

    }


    private void initRecyclerView() {
        recyclerView = binding.mainRecyclerview;
        adatper = new LiberalArtsAdapter(getContext(), lectureViewModel.getLiberalArts());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adatper);
    }


    private void initSpinner() {
        ArrayAdapter<String> spinnerAdapter;
        String[] items = {"????????? ???????????????.", "??????", "??????", "??????"};
        spinnerAdapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_list_item_1, items
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        binding.artsSpinner.setAdapter(spinnerAdapter);
        /**
         * ????????? ??????
         */
        binding.artsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position == 0) {
                    type = "";
                    return;
                }
                getArtsData(adapterView, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    /**
     * ????????? ?????? ?????? ??? ???????????? ????????? ????????????
     */
    private void getArtsData(AdapterView<?> adapterView, int position) {
        loadingDialog.show();
        char pos = (char) (position + 96);
        int id = getResources().getIdentifier(String.valueOf(pos), "string", getContext().getPackageName());
        type = getResources().getString(id);
        lectureViewModel.getChangeAllData("", type, "liberalArts");

    }

    private void initDialog() {
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }


}
