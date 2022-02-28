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
import com.example.nodeproject2.adapter.MajorAdatper;
import com.example.nodeproject2.config.BuildBallon;
import com.example.nodeproject2.config.BuildLecturDatabase;
import com.example.nodeproject2.databinding.FragmentMajorBinding;
import com.example.nodeproject2.datas.Lecture;
import com.skydoves.balloon.*;

import java.util.ArrayList;
import java.util.List;

public class MajorFragment extends Fragment {
    private FragmentMajorBinding binding;

    private RecyclerView recyclerView;
    private MajorAdatper adatper;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BuildLecturDatabase lecturDatabase;
    private LectureViewModel lectureViewModel;

    private LoadingDialog loadingDialog;

    /**
     * 선택된 학년, 이수구분, 필터링 텍스트, 빈자리
     */
    private String current_grade = "";
    private String type = "";
    private String value="";
    private boolean current_checked = false;

    private ArrayList<Lecture> maindata = new ArrayList<>();

    private Balloon balloon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMajorBinding.inflate(inflater, container, false);
        lecturDatabase = new BuildLecturDatabase(getContext(),"lecture_table");

        swipeRefreshLayout = binding.swipeLayout;

        lectureViewModel = new ViewModelProvider(requireActivity()).get(LectureViewModel.class);
        balloon = BuildBallon.getBalloon(getContext(),getViewLifecycleOwner(),
                "1. 우측버튼을 누르면 수강바구니에 추가할 수 있어요.\n\n2. 화면을 아래로 스크롤 하면 새로고침 할 수 있어요."
        );

        initRecyclerView();
        init();
        initSpinner();
        initDialog();
        initObserver();

        return binding.getRoot();

    }




    private void init() {
//        SpannableStringBuilder builder = new SpannableStringBuilder("쿠키님들의 전공을 알려주세요.");
//        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#3EAF36")), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        binding.tv.append(builder);
        /**
         * 과목 추가 버튼
         */
        adatper.setOnItemClickListener(new MajorAdatper.OnItemClickListener() {
            @Override
            public void OnItemClick(MajorAdatper.ViewHolder holder, View view, int pos) {
                insertDeleteSbj(holder);
            }
        });

        /**
         * 툴팁 클릭
         */
        binding.tooltipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                balloon.showAlignBottom(binding.tooltipbtn);
            }
        });

        /**
         * 강의 타이틀 필터링
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
         * 새로고침
         */
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(value.equals("")) {
                    Toast.makeText(getContext(),"선택해주세요.",Toast.LENGTH_LONG).show();
                }else {
                    loadingDialog.show();
                    lectureViewModel.getChangeAllData(value,"","subject");
                }
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        /**
         * 학년 라디오 버튼
         */
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int ckeckedID) {
                String edit_text = binding.findlectureEdittext.getText().toString();
                switch (ckeckedID) {
                    case R.id.radioButton1:
                        current_grade = "";
                        break;
                    case R.id.radioButton2:
                        current_grade = "1";
                        break;
                    case R.id.radioButton3:
                        current_grade = "2";
                        break;
                    case R.id.radioButton4:
                        current_grade = "3";
                        break;
                    case R.id.radioButton5:
                        current_grade = "4";
                        break;
                }
                adatper.swapItems(maindata,edit_text,current_grade,type,current_checked);
            }
        });
        /**
         * 빈자리 버튼
         */
        binding.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            String edit_text = binding.findlectureEdittext.getText().toString();
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                current_checked = checked;
                adatper.swapItems(maindata,edit_text,current_grade,type,current_checked);
            }
        });
        /**
         * 이수구분 라디오 버튼
         */
        binding.typeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int ckeckedID) {
                String edit_text = binding.findlectureEdittext.getText().toString();
                switch (ckeckedID) {
                    case R.id.typeRadioButton1:
                        type = "";
                        break;
                    case R.id.typeRadioButton2:
                        type = "전필";
                        break;
                    case R.id.typeRadioButton3:
                        type = "전선";
                        break;
                    case R.id.typeRadioButton4:
                        type = "지교,지필,교직";
                        break;
                }
                adatper.swapItems(maindata,edit_text,current_grade,type, current_checked);

            }
        });

    }
    private void initObserver() {
        /**
         * 새로고침으로 인해 들어온 데이터로 바꿔주는 observer
         */
        lectureViewModel.lectures.observe(this, new Observer<ArrayList<Lecture>>() {
            @Override
            public void onChanged(ArrayList<Lecture> lectures) {
                binding.findlectureEdittext.setText("");
                maindata = lectures;
                adatper.swapItems(lectures, "" ,current_grade,type, current_checked);
                loadingDialog.dismiss();
            }
        });
        /**
         * 404오류 시 loading dlg를 지우기 위한 observe
         */
        lectureViewModel.status.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                loadingDialog.dismiss();
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = binding.mainRecyclerview;
        adatper = new MajorAdatper(getContext(), lectureViewModel.getLectures());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adatper);
    }

    private void initDialog() {
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    private void initSpinner() {
        ArrayAdapter<String> spinnerAdapter;
        String[] items = getResources().getStringArray(R.array.spinner_data).clone();
        spinnerAdapter  = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        binding.spinner.setAdapter(spinnerAdapter);
        /**
         * 스피너 선택
         */
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position == 0) {
                    value = "";
                    return;
                }
                getSbjData(adapterView, position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /**
     * 라디오 버튼 클릭 후 서버에서 데이터 가져오기
     */
    private void getSbjData(AdapterView<?> adapterView, int position) {
        loadingDialog.show();
        String itemValue = (String) adapterView.getItemAtPosition(position);
        itemValue = itemValue.replaceAll(" ", "");
        String match = "[^\uAC00-\uD7A30-9a-zA-Z]";
        itemValue = itemValue.replaceAll(match, "");
        int id = getResources().getIdentifier(itemValue, "string", getContext().getPackageName());
        value = getResources().getString(id);
        lectureViewModel.getChangeAllData(value,"","subject");
    }

    private void insertDeleteSbj(MajorAdatper.ViewHolder holder) {
        List<Lecture> lectures = lecturDatabase.getLecturesFromDB();
        Lecture lecture = Lecture.createLectureFromMajor(holder);

        if(lectures.contains(lecture)) {
            holder.btn.setBackgroundResource(R.drawable.btn_favorite_off);
            holder.sub_num.setTextColor(Color.BLACK);
            holder.type.setTextColor(Color.BLACK);
            holder.grade.setTextColor(Color.BLACK);
            Toast.makeText(getContext(),lecture.getSubject_title()+"이(가) 등록 해제되었습니다.",Toast.LENGTH_SHORT).show();
            lecturDatabase.setDeleteLecture(lecture);
        }
        else {
            holder.btn.setBackgroundResource(R.drawable.btn_favorite_on);
            holder.sub_num.setTextColor(Color.WHITE);
            holder.type.setTextColor(Color.WHITE);
            holder.grade.setTextColor(Color.WHITE);
            lecturDatabase.setInsertLecture(lecture);
            Toast.makeText(getContext(),lecture.getSubject_title()+"이(가) 등록 되었습니다.",Toast.LENGTH_SHORT).show();
        }

    }



}
