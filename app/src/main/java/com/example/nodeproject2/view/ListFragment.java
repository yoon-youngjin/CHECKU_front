package com.example.nodeproject2.view;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
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
import com.example.nodeproject2.API.Lecture.LectureDao;
import com.example.nodeproject2.API.Lecture.LectureDatabase;
import com.example.nodeproject2.API.RetrofitClient;
import com.example.nodeproject2.API.viewmodel.LectureViewModel;
import com.example.nodeproject2.R;
import com.example.nodeproject2.adapter.ListAdapter;
import com.example.nodeproject2.databinding.FragmentListBinding;
import com.example.nodeproject2.datas.Lecture;
import com.skydoves.balloon.*;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListFragment extends Fragment {
    private LectureDao lectureDao;
    RecyclerView recyclerView;
    ListAdapter adatper;
    FragmentListBinding binding;
    LoadingDialog loadingDialog;
    private LectureViewModel lectureViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CodeDialog codeDialog;
    private DeleteDialog deleteDialog;

    private boolean empty_check = false;
    private Balloon balloon;



    @Override
    public void onResume() {
        super.onResume();
        loadingDialog.show();
        lectureViewModel.getChangeData(lectureDao.getLectureAll());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentListBinding.inflate(inflater, container, false);
        swipeRefreshLayout = binding.swipeLayout;

        lectureViewModel = new ViewModelProvider(requireActivity()).get(LectureViewModel.class);
        codeDialog = new CodeDialog(getContext());
        deleteDialog = new DeleteDialog(getContext());
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LectureDatabase db = Room.databaseBuilder(getContext(), LectureDatabase.class, "test_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();

        lectureDao = db.lectureDao();
        balloon = new Balloon.Builder(getContext())
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowPosition(0.5f)
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setTextSize(12f)
                .setCornerRadius(8f)
                .setPadding(10)
                .setAlpha(0.9f)
                .setTextGravity(Gravity.START)
                .setBackgroundColorResource(R.color.kukie_gray)
                .setText("1. 우측 하단 탭을 눌러 수강바구니에 과목을 추가해보세요.\n\n2. 화면을 아래로 스크롤 하면 새로고침할 수 있습니다.\n\n3. 우측 버튼을 클릭하면 과목을 지울 수 있습니다.\n\n" +
                        "4. 강의를 클릭하면 비고를 확인할 수 있어요." )
                .setTextColor(ContextCompat.getColor(getContext(), R.color.black))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .setLifecycleOwner(getViewLifecycleOwner())
                .build();

        binding.clicklayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                balloon.showAlignBottom(binding.tooltipbtn);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        balloon.dismiss();
                    }
                }, 5000);
            }
        });

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


        binding.codeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sbj_num = binding.codeEditText.getText().toString();
                binding.codeEditText.setText("");

                if(sbj_num.equals("")) {
                    Toast.makeText(getContext(), "과목번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    codeDialog.show(sbj_num);
                }
                codeDialog.setOnItemClickListener(new CodeDialog.OnItemClickListener() {
                    @Override
                    public void OnItemClick(Lecture lecture) {
                        List<Lecture> lectures = lectureDao.getLectureAll();

                        if(lectures.contains(lecture)) {
                            Toast.makeText(getContext(),"이미 등록된 강의입니다.",Toast.LENGTH_LONG).show();
                        }else {
                            lectureDao.setInsertLecture(lecture);
                            adatper.swapItems(lectureDao.getLectureAll(),empty_check);
                        }
                        codeDialog.dismiss();

                    }
                });
                codeDialog.setOnItemClickListener2(new CodeDialog.OnItemClickListener() {
                    @Override
                    public void OnItemClick(Lecture lecture) {
                        codeDialog.dismiss();
                    }
                });
            }
        });


        return binding.getRoot();

    }

    private void initObserver() {
        lectureViewModel.myLectures.observe(this, new Observer<ArrayList<Lecture>>() {
            @Override
            public void onChanged(ArrayList<Lecture> lectures) {
                for (Lecture lecture:lectures) {
                    lectureDao.setUpdateLecture(lecture);
                }
                adatper.swapItems(lectures,empty_check);
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

    private void init() {

        recyclerView = binding.listRecyclerview;
        adatper = new ListAdapter(getContext(), lectureDao.getLectureAll());

        adatper.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(ListAdapter.ViewHolder holder, View view, int pos) {
                Lecture lecture = new Lecture();
                lecture.setSubject_title(holder.sub_title.getText().toString());
                lecture.setSubject_num(Integer.parseInt(holder.sub_num.getText().toString()));
                deleteDialog.show(lecture);
                deleteDialog.setOnItemClickListener(new DeleteDialog.OnItemClickListener() {

                    @Override
                    public void OnItemClick() {
                        lectureDao.setDeleteLecture(lecture);
                        adatper.swapItems(lectureDao.getLectureAll(),empty_check);
                        deleteDialog.dlg.dismiss();
                    }
                });
                deleteDialog.setOnItemClickListener2(new DeleteDialog.OnItemClickListener() {
                    @Override
                    public void OnItemClick() {
                        deleteDialog.dlg.dismiss();
                    }
                });

            }
        });

        adatper.setOnItemClickListener2(new ListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(ListAdapter.ViewHolder holder, View view, int pos) {

                holder.sub_num.getText().toString();

                HashMap<String, String> map = new HashMap<>();
                map.put("sbj_num",  holder.sub_num.getText().toString());

                Call<String> call = RetrofitClient.retrofitInterface.excuteRegister(map);

                call.enqueue(new Callback<String>() {
                    @SneakyThrows
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        System.out.println(response.body());

                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("check", "Fail!!");
                    }
                });




            }
        });

        binding.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                empty_check = checked;
                adatper.swapItems(lectureDao.getLectureAll(),empty_check);

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
//                    //TODO 보낸 요청 취소
//                    intent.putExtra("checked", "false");
//                    getActivity().startService(intent);
//                }
//            }
//        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adatper);



    }

}