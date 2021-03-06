package com.example.nodeproject2.view;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.nodeproject2.API.viewmodel.LectureViewModel;
import com.example.nodeproject2.config.BuildBallon;
import com.example.nodeproject2.adapter.BasketAdapter;
import com.example.nodeproject2.config.BuildLecturDatabase;
import com.example.nodeproject2.databinding.FragmentBasketBinding;
import com.example.nodeproject2.datas.Lecture;
import com.skydoves.balloon.*;

import java.util.ArrayList;
import java.util.List;


public class BasketFragment extends Fragment {
    private FragmentBasketBinding binding;

    private RecyclerView recyclerView;
    private BasketAdapter adatper;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BuildLecturDatabase lecturDatabase;
    private LectureViewModel lectureViewModel;

    private SearchDialog searchDialog;
    private DeleteDialog deleteDialog;
    private LoadingDialog loadingDialog;

    private Balloon balloon;
    private boolean empty_check = false;

    private List<Lecture> myLectures;

    @Override
    public void onResume() {
        super.onResume();
        loadingDialog.show();
        myLectures = lecturDatabase.getLecturesFromDB();
        lectureViewModel.getChangeData(myLectures);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBasketBinding.inflate(inflater, container, false);
        lecturDatabase = new BuildLecturDatabase(getContext(), "lecture_table");

        myLectures = lecturDatabase.getLecturesFromDB();

        swipeRefreshLayout = binding.swipeLayout;


        lectureViewModel = new ViewModelProvider(requireActivity()).get(LectureViewModel.class);
        balloon = BuildBallon.getBalloon(getContext(), getViewLifecycleOwner(),
                "1. ?????? ?????? ?????? ?????? ?????????????????? ????????? ??????????????????.\n\n2. ????????? ????????? ????????? ?????? ??????????????? ??? ????????????.\n\n3. ?????? ????????? ???????????? ????????? ?????? ??? ????????????.\n\n" +
                        "4. ????????? ???????????? ????????? ????????? ??? ?????????."
        );

        initRecyclerView();
        init();
        initDialog();
        initObserver();

        return binding.getRoot();
    }


    private void initObserver() {
        /**
         * ?????????????????? ?????? ????????? ???????????? ???????????? observer
         */
        lectureViewModel.myLectures.observe(this, new Observer<ArrayList<Lecture>>() {
            @Override
            public void onChanged(ArrayList<Lecture> lectures) {
                for (Lecture lecture : lectures) {
                    lecturDatabase.updateLecture(lecture);
                }
                myLectures = lecturDatabase.getLecturesFromDB();
                adatper.swapItems(myLectures, empty_check);
                loadingDialog.dismiss();
            }
        });
        /**
         * 404?????? ??? loading dlg??? ????????? ?????? observe
         */
        lectureViewModel.status.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                loadingDialog.dismiss();
            }
        });
    }


    private void init() {

        /**
         * ??????????????? ?????? ???????????? ??????
         */
        binding.codeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sbj_num = binding.codeEditText.getText().toString();
                binding.codeEditText.setText("");
                if (sbj_num.equals("")) {
                    Toast.makeText(getContext(), "??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                } else {
                    searchDialog.show(sbj_num);
                }

            }
        });

        /**
         * ????????????
         */
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingDialog.show();

                lectureViewModel.getChangeData(myLectures);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        /**
         * ?????? ??????
         */
        binding.clicklayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                balloon.showAlignBottom(binding.tooltipbtn);
            }
        });

        /**
         * ?????? item ??????
         */
        adatper.setOnItemClickListener(new BasketAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(BasketAdapter.ViewHolder holder, View view, int pos) {
                Lecture lecture = new Lecture();
                lecture.setSubject_title(holder.sub_title.getText().toString());
                lecture.setSubject_num(Integer.parseInt(holder.sub_num.getText().toString()));
                deleteDialog.show(lecture);
            }
        });

        /**
         * ???????????? ??????
         */
        adatper.setOnRegisterClickListener(new BasketAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(BasketAdapter.ViewHolder holder, View view, int pos) {
                lectureViewModel.letsRegister(holder.sub_num.getText().toString());
            }
        });

        /**
         * ????????? ??????
         */
        binding.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                empty_check = checked;
                adatper.swapItems(myLectures, empty_check);
            }
        });


//        adatper.setOnCheckedChangeListener(new ListAdapter.OnCheckedChangeListener() {
//            @Override
//            public void OnItemChange(ListAdapter.ViewHolder holder, View view, int pos, boolean isChecked) throws IOException {
//
//                Intent intent = new Intent(getContext(), MyService.class);
//                String subject_num = holder.sub_num.getText().toString();
//                intent.putExtra("subject_num", subject_num);
//
//                if (isChecked) {
//                    intent.putExtra("checked", "true");
//                    getActivity().startService(intent);
//                } else {
//                    //TODO ?????? ?????? ??????
//                    intent.putExtra("checked", "false");
//                    getActivity().startService(intent);
//                }
//            }
//        });

    }

    private void initRecyclerView() {
        recyclerView = binding.listRecyclerview;
        adatper = new BasketAdapter(getContext(), myLectures);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adatper);
    }


    private void initDialog() {
        searchDialog = new SearchDialog(getContext());
        deleteDialog = new DeleteDialog(getContext());
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        searchDialog.setOnAcceptItemClickListener(new SearchDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(Lecture lecture) {
                List<Lecture> lectures = lecturDatabase.getLecturesFromDB();
                if (lectures.contains(lecture)) {
                    Toast.makeText(getContext(), "?????? ????????? ???????????????.", Toast.LENGTH_LONG).show();
                } else {
                    lecturDatabase.setInsertLecture(lecture);
                    adatper.swapItems(myLectures, empty_check);
                }
                searchDialog.dismiss();

            }
        });
        searchDialog.setOnRejectItemClickListener(new SearchDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(Lecture lecture) {
                searchDialog.dismiss();
            }
        });
        deleteDialog.setOnAcceptItemClickListener(new DeleteDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(Lecture lecture) {
                lecturDatabase.setDeleteLecture(lecture);
                myLectures = lecturDatabase.getLecturesFromDB();
                adatper.swapItems(myLectures, empty_check);
                deleteDialog.dismiss();
            }
        });
        deleteDialog.setOnItemRejectClickListener(new DeleteDialog.OnItemClickListener() {
            @Override
            public void OnItemClick(Lecture lecture) {
                deleteDialog.dismiss();
            }
        });

    }

}