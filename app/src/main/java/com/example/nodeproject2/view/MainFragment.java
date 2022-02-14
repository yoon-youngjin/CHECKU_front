package com.example.nodeproject2.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
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
import com.example.nodeproject2.API.viewmodel.LectureViewModel;
import com.example.nodeproject2.API.Lecture.LectureDao;
import com.example.nodeproject2.API.Lecture.LectureDatabase;
import com.example.nodeproject2.R;
import com.example.nodeproject2.adapter.MainAdatper;
import com.example.nodeproject2.databinding.FragmentMainBinding;
import com.example.nodeproject2.datas.Lecture;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private LectureDao lectureDao;
    private RecyclerView recyclerView;
    private MainAdatper adatper;
    private FragmentMainBinding binding;
    private LoadingDialog loadingDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LectureViewModel lectureViewModel;
    private String value="";
    private ArrayAdapter<String> adapter2;
    private String current_grade = "";
    private String type = "";
    private ArrayList<Lecture> maindata;
    private boolean current_checked = false;
    private Balloon balloon;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater, container, false);
        lectureViewModel = new ViewModelProvider(requireActivity()).get(LectureViewModel.class);
        loadingDialog = new LoadingDialog(getContext());
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        swipeRefreshLayout = binding.swipeLayout;
        lectureDao = getLectureDao();
        maindata = new ArrayList<>();
        balloon = new Balloon.Builder(getContext())
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowPosition(0.46f)
                .setWidthRatio(0.6f)
                .setHeight(65)
                .setTextSize(10f)
                .setCornerRadius(4f)
                .setAlpha(0.9f)
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
//        SpannableStringBuilder builder = new SpannableStringBuilder("쿠키님들의 전공을 알려주세요.");
//        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#3EAF36")), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        binding.tv.append(builder);

        binding.tooltipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("test");

                balloon.showAlignBottom(binding.tooltipbtn);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        balloon.dismiss();
                    }
                }, 5000);

            }
        });


        String[] items = getResources().getStringArray(R.array.spinner_data).clone();

        adapter2  = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);

        adapter2.setDropDownViewResource(android.R.layout.simple_list_item_1);
        binding.spinner.setAdapter(adapter2);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    loadingDialog.show();

                    String temp = (String) adapterView.getItemAtPosition(position);
                    temp = temp.replaceAll(" ", "");
                    String match = "[^\uAC00-\uD7A30-9a-zA-Z]";
                    temp = temp.replaceAll(match, "");

                    int id = getResources().getIdentifier(temp, "string", getContext().getPackageName());
                    value = getResources().getString(id);
                    lectureViewModel.getChangeAllData(value,"","subject");
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
                if(value.equals("")) {
                    Toast.makeText(getContext(),"선택해주세요.",Toast.LENGTH_LONG).show();
                }else {
                    loadingDialog.show();
                    lectureViewModel.getChangeAllData(value,"","subject");
                }
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int ckeckedID) {
                String edit_text = binding.findlectureEdittext.getText().toString();

                switch (ckeckedID) {
                    case R.id.radioButton1:
                        //TODO 속도비교후 변경
                        current_grade = "";
                        adatper.swapItems(maindata,edit_text,current_grade,type,current_checked);
                        break;
                    case R.id.radioButton2:
                        current_grade = "1";
                        adatper.swapItems(maindata,edit_text,current_grade,type,current_checked);

                        break;
                    case R.id.radioButton3:
                        current_grade = "2";
                        adatper.swapItems(maindata,edit_text,current_grade,type,current_checked);
                        break;
                    case R.id.radioButton4:
                        current_grade = "3";
                        adatper.swapItems(maindata,edit_text,current_grade,type,current_checked);
                        break;
                    case R.id.radioButton5:
                        current_grade = "4";
                        adatper.swapItems(maindata,edit_text,current_grade,type,current_checked);
//                        radioBtnClick("4");
                        break;
                }
            }
        });

        binding.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            String edit_text = binding.findlectureEdittext.getText().toString();

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                current_checked = checked;
                adatper.swapItems(maindata,edit_text,current_grade,type,current_checked);

            }
        });


        binding.typeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int ckeckedID) {
                String edit_text = binding.findlectureEdittext.getText().toString();

                switch (ckeckedID) {
                    case R.id.typeRadioButton1:
                        type = "";
                        adatper.swapItems(maindata,edit_text,current_grade,type, current_checked);
                        break;
                    case R.id.typeRadioButton2:
                        type = "전필";
                        adatper.swapItems(maindata,edit_text,current_grade,type, current_checked);

                        break;
                    case R.id.typeRadioButton3:
                        type = "전선";
                        adatper.swapItems(maindata,edit_text,current_grade,type, current_checked);
                        break;
                    case R.id.typeRadioButton4:
                        type = "지교,지필,교직";
                        adatper.swapItems(maindata,edit_text,current_grade,type, current_checked);
                        break;

                }
            }
        });

    }

//    private void radioBtnClick(String grade) {
//        Call<List<Lecture>> call = RetrofitClient.retrofitInterface.excuteChangeAll(value, grade);
//        call.enqueue(new Callback<List<Lecture>>() {
//            @SneakyThrows
//            @Override
//            public void onResponse(Call<List<Lecture>> call, Response<List<Lecture>> response) {
//                if (response.code() == 200) {
//                    ArrayList<Lecture> data = (ArrayList<Lecture>) response.body();
//                    adatper.swapItems(data, "", current_grade);
//                } else {
//                    //TODO 변경
//                }
//            }
//            @Override
//            public void onFailure(Call<List<Lecture>> call, Throwable t) {
//                Log.d("check", "Fail!!");
//            }
//        });
//    }

    private void initObserver() {
        lectureViewModel.lectures.observe(this, new Observer<ArrayList<Lecture>>() {
            @Override
            public void onChanged(ArrayList<Lecture> lectures) {
                binding.findlectureEdittext.setText("");
                maindata = lectures;
                adatper.swapItems(lectures, "" ,current_grade,type, current_checked);
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
        adatper = new MainAdatper(getContext(), lectureViewModel.getLectures());

        adatper.setOnItemClickListener(new MainAdatper.OnItemClickListener() {
            @Override
            public void OnItemClick(MainAdatper.ViewHolder holder, View view, int pos) {
                List<Lecture> lectures = lectureDao.getLectureAll();
                Lecture lecture = Lecture.builder().subject_num(Integer.parseInt(holder.sub_num.getText().toString())).subject_title(holder.sub_title.getText().toString())
                        .professor_name(holder.pro_name.getText().toString()).capacity_total(holder.capacity_total.getText().toString())
                        .year(holder.grade.getText().toString().substring(0,1))
                        .emptySize(Integer.parseInt(holder.empty.getText().toString()))
                        .major_division(holder.type.getText().toString())
                        .build();

                if(lectures.contains(lecture)) {
                    holder.btn.setBackgroundResource(R.drawable.btn_favorite_off);
                    holder.sub_num.setTextColor(Color.BLACK);
                    holder.type.setTextColor(Color.BLACK);
                    holder.grade.setTextColor(Color.BLACK);
                    Toast.makeText(getContext(),lecture.getSubject_title()+"이(가) 등록 해제되었습니다.",Toast.LENGTH_SHORT).show();
                    lectureDao.setDeleteLecture(lecture);
                }
                else {
                    holder.btn.setBackgroundResource(R.drawable.btn_favorite_on);
                    holder.sub_num.setTextColor(Color.WHITE);
                    holder.type.setTextColor(Color.WHITE);
                    holder.grade.setTextColor(Color.WHITE);
                    lectureDao.setInsertLecture(lecture);
                    Toast.makeText(getContext(),lecture.getSubject_title()+"이(가) 등록 되었습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });



//        adatper.setOnCheckedChangeListener(new MainAdatper.OnCheckedChangeListener() {
//            @Override
//            public void OnItemChange(MainAdatper.ViewHolder holder, View view, int pos, boolean isChecked) {
//
//
//                if (isChecked) {
////                    Lecture lecture = Lecture.builder().subject_num(Integer.parseInt(holder.sub_num.getText().toString())).subject_title(holder.sub_title.getText().toString())
////                            .professor_name(holder.pro_name.getText().toString()).capacity_total(holder.capacity_total.getText().toString())
////                            .capacity_year(holder.capacity_year.getText().toString())
////                            .build();
////                    lectureDao.setInsertLecture(lecture);
//
//                } else {
////                    Lecture lec = Lecture.builder().subject_num(Integer.parseInt(holder.sub_num.getText().toString())).build();;
////                    lectureDao.setDeleteLecture(lec);
//
//                }
//            }
//        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adatper);
    }


    //    public void initData() {
//
//        try (InputStream is = getResources().getAssets().open("file.csv");
//        ) {
//            lectures = new ArrayList();
//            CSVFile file = new CSVFile(is);
//            lectures = file.read();
//            lectureViewModel.setLectures(lectures);
//
//            // 엑셀 읽기
////            Workbook wb = new HSSFWorkbook(is);
////            Sheet sheet = wb.getSheetAt(0);
////            int rows = sheet.getPhysicalNumberOfRows();
////
////            for (int i = 0; i < rows; i++) {
////                HSSFRow row = (HSSFRow) sheet.getRow(i);
////                Subject sub;
////                if(row!=null) {
//////                    int cells = row.getPhysicalNumberOfCells();
////                    sub = Subject.builder().subject_title(row.getCell(7).getStringCellValue()).professor_name(row.getCell(16).getStringCellValue())
////                            .total_num("40").current_num("0").subject_num(row.getCell(6).getStringCellValue()).build();
////                    subData.add(sub);
////                }
////            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        showView();
//
//    }
    private LectureDao getLectureDao() {

        LectureDatabase db = Room.databaseBuilder(getContext(), LectureDatabase.class, "test_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();

        return db.lectureDao();

    }


}
