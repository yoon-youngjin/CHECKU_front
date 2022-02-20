package com.example.nodeproject2.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
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
import com.skydoves.balloon.*;
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
        Log.d("onCreateView3", "onCreateView3");

        binding = FragmentLiberalArtsBinding.inflate(inflater, container, false);
        lectureViewModel = new ViewModelProvider(requireActivity()).get(LectureViewModel.class);
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        swipeRefreshLayout = binding.swipeLayout;
        lectureDao = getLectureDao();
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
                .setText("1. 우측버튼을 누르면 수강바구니에 추가할 수 있어요.\n2. 화면을 아래로 스크롤 하면 새로고침 할 수 있어요.")
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
        String[] items = {"교양을 구분하세요.", "기교", "심교", "일선"};
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

                if (type.equals("")) {
                    Toast.makeText(getContext(), "선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    loadingDialog.show();
                    lectureViewModel.getChangeAllData("", type, "liberalArts");
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

//        binding.toggleButton.setOnClickListener(new View.OnClickListener() {
//            String edit_text = binding.findlectureEdittext.getText().toString();
//            @Override
//            public void onClick(View view) {
//                empty_checked = binding.toggleButton.isChecked();
//                adatper.swapItems(originData, edit_text, empty_checked);
//            }
//        });

        binding.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            String edit_text = binding.findlectureEdittext.getText().toString();

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                empty_checked = checked;
                adatper.swapItems(originData, edit_text, empty_checked);

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

    private void showView() {
        recyclerView = binding.mainRecyclerview;
        adatper = new LiberalArtsAdapter(getContext(), lectureViewModel.getLiberalArts());

        adatper.setOnItemClickListener(new LiberalArtsAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(LiberalArtsAdapter.ViewHolder holder, View view, int pos) {

                List<Lecture> lectures = lectureDao.getLectureAll();

                String pro_name = holder.pro_name.getText().toString();
                Lecture lecture = Lecture.builder()
                        .subject_num(Integer.parseInt(holder.sub_num.getText().toString()))
                        .subject_title(holder.sub_title.getText().toString())
                        .capacity_total(holder.capacity_total.getText().toString())
                        .major_division(holder.type.getText().toString())
                        .emptySize(Integer.parseInt(holder.empty.getText().toString()))
                        .room(holder.room.getText().toString())
                        .detail(holder.detail.getText().toString())
                        .build();

                if (holder.year.getText().toString().equals("전체")) {
                    lecture.setYear("9");
                } else {
                    lecture.setYear(holder.year.getText().toString().substring(0, 1));
                }

                if (pro_name.equals("")) {
                    lecture.setProfessor_name(pro_name);
                } else {
                    lecture.setProfessor_name(pro_name.trim());
                }

                if (lectures.contains(lecture)) {
                    holder.btn.setBackgroundResource(R.drawable.btn_favorite_off);
                    holder.sub_num.setTextColor(Color.BLACK);
                    holder.type.setTextColor(Color.BLACK);
                    holder.year.setTextColor(Color.BLACK);
                    lectureDao.setDeleteLecture(lecture);
                    Toast.makeText(getContext(), lecture.getSubject_title() + "이(가) 등록 해제되었습니다.", Toast.LENGTH_SHORT).show();


                } else {
                    holder.btn.setBackgroundResource(R.drawable.btn_favorite_on);
                    holder.sub_num.setTextColor(Color.WHITE);
                    holder.type.setTextColor(Color.WHITE);
                    holder.year.setTextColor(Color.WHITE);
                    lectureDao.setInsertLecture(lecture);
                    Toast.makeText(getContext(), lecture.getSubject_title() + "이(가) 등록 되었습니다.", Toast.LENGTH_SHORT).show();

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
