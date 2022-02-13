package com.example.nodeproject2.view;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.nodeproject2.API.RetrofitClient;
import com.example.nodeproject2.API.viewmodel.LectureViewModel;
import com.example.nodeproject2.API.Lecture.LectureDao;
import com.example.nodeproject2.API.Lecture.LectureDatabase;
import com.example.nodeproject2.R;
import com.example.nodeproject2.adapter.LiberalArtsAdapter;
import com.example.nodeproject2.adapter.MainAdatper;
import com.example.nodeproject2.databinding.FragmentLiberalArtsBinding;
import com.example.nodeproject2.databinding.FragmentMainBinding;
import com.example.nodeproject2.datas.Lecture;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiberalArtsFragment extends Fragment {

    private LectureDao lectureDao;
    private RecyclerView recyclerView;
    private LiberalArtsAdapter adatper;
    private FragmentLiberalArtsBinding binding;
    private LoadingDialog loadingDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LectureViewModel lectureViewModel;
    private ArrayAdapter<String> adapter2;
    private ArrayList<Lecture> originData;
    private boolean empty_checked = false;
    private String type = "";
    private Balloon balloon;


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLiberalArtsBinding.inflate(inflater, container, false);
        lectureViewModel = new ViewModelProvider(requireActivity()).get(LectureViewModel.class);
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        swipeRefreshLayout = binding.swipeLayout;
        lectureDao = getLectureDao();
        balloon = new Balloon.Builder(getContext())
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowPosition(0.32f)
                .setWidthRatio(0.6f)
                .setHeight(65)
                .setTextSize(10f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
                .setText("1. 좌측버튼을 누르면 수강바구니에 추가할 수 있어요.\n2. 화면을 위로 스크롤 하면 새로고침 할 수 있어요.")
                .setTextColor(ContextCompat.getColor(getContext(), R.color.black))
                .setBackgroundColor(ContextCompat.getColor(getContext(), R.color.kukie_gray))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build();
        init();
        initObserver();
        return binding.getRoot();
    }


    private void init() {
        showView();
        String[] items = {"선택하세요", "기교", "심교", "일선"};
        adapter2 = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_list_item_1, items
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_list_item_1);
        binding.artsSpinner.setAdapter(adapter2);

        binding.artsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    loadingDialog.show();
                    char pos = (char) (position + 96);
                    int id = getResources().getIdentifier(String.valueOf(pos), "string", getContext().getPackageName());
                    type = getResources().getString(id);
                    lectureViewModel.getChangeAllData("", type, "liberalArts");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

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


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(type.equals("")) {
                    Toast.makeText(getContext(),"선택하세요.",Toast.LENGTH_SHORT).show();
                }else {
                    loadingDialog.show();
                    lectureViewModel.getChangeAllData("", type, "liberalArts");
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        binding.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            String edit_text = binding.findlectureEdittext.getText().toString();

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                empty_checked = checked;
                adatper.swapItems(originData,edit_text,empty_checked);

            }
        });
        binding.tooltipbtn.setOnClickListener(new View.OnClickListener() {
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


    }


    private void initObserver() {
        lectureViewModel.liberalArts.observe(this, new Observer<ArrayList<Lecture>>() {
            @Override
            public void onChanged(ArrayList<Lecture> lectures) {
                originData = lectures;
                String current_text = binding.findlectureEdittext.getText().toString();
                adatper.swapItems(lectures, current_text,empty_checked);
                loadingDialog.dismiss();
            }
        });
    }

    private void showView() {
        recyclerView = binding.mainRecyclerview;
        adatper = new LiberalArtsAdapter(getContext(), lectureViewModel.getLiberalArts());

        adatper.setOnItemClickListener(new LiberalArtsAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(LiberalArtsAdapter.ViewHolder holder, View view, int pos) {

                List<Lecture> lectures = lectureDao.getLectureAll();
                Lecture lecture = Lecture.builder().subject_num(Integer.parseInt(holder.sub_num.getText().toString())).build();



                if (lectures.contains(lecture)) {
                    Toast.makeText(getContext(), "이미 등록된 강의입니다.", Toast.LENGTH_LONG).show();
                } else {
                    Lecture lecture2 = Lecture.builder()
                            .subject_num(Integer.parseInt(holder.sub_num.getText().toString()))
                            .subject_title(holder.sub_title.getText().toString())
                            .professor_name(holder.pro_name.getText().toString())
                            .capacity_total(holder.capacity_total.getText().toString())
                            .year(holder.year.getText().toString())
                            .build();
                    lectureDao.setInsertLecture(lecture2);

                    Toast.makeText(getContext(),lecture2.getSubject_title()+"이 등록되었습니다.",Toast.LENGTH_LONG).show();

                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adatper);
    }

    private LectureDao getLectureDao() {

        LectureDatabase db = Room.databaseBuilder(getContext(), LectureDatabase.class, "test_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();

        return db.lectureDao();

    }


}
