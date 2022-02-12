package com.example.nodeproject2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.example.nodeproject2.API.Lecture.LectureDao;
import com.example.nodeproject2.API.Lecture.LectureDatabase;
import com.example.nodeproject2.R;
import com.example.nodeproject2.datas.Lecture;

import java.util.ArrayList;
import java.util.List;

public class MainAdatper extends RecyclerView.Adapter<MainAdatper.ViewHolder> implements Filterable {

    private ArrayList<Lecture> unFilteredlist;
    private ArrayList<Lecture> filteredList;
    private List<Lecture> lectureList;
    private String current_grade;

    public void swapItems(ArrayList<Lecture> items, String s, String current_grade) {

        this.unFilteredlist = items;
        this.filteredList = items;
        this.current_grade = current_grade;
        getFilter().filter(s);
        notifyDataSetChanged();
    }


    public MainAdatper(Context context, ArrayList<Lecture> data) {
        this.unFilteredlist = data;
        this.filteredList = data;
        this.context = context;
        this.lectureList = getLectureDao().getLectureAll();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                ArrayList<Lecture> filteringList = new ArrayList<>();

                // 전체학년 && 과목입력x => filtering x
                if (charString.equals("") && current_grade.equals("")) {
                    filteredList = unFilteredlist;
                }
                // 학년선택 o && 과목입력 x
                if(charString.equals("")) {
                    for (Lecture lec : unFilteredlist) {
                        if (lec.getCredit().contains(current_grade)) {
                            System.out.println(lec);
                            filteringList.add(lec);
                        }
                    }
                    filteredList = filteringList;
                }
                // 학년 선택 o && 과목입력 o
                else {
                    for (Lecture lec : unFilteredlist) {
                        if (lec.getSubject_title().contains(charString) && lec.getCredit().contains(current_grade)) {
                            filteringList.add(lec);
                        }
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<Lecture>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener {
        void OnItemClick(ViewHolder holder, View view, int pos);
    }

    private OnItemClickListener itemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }



    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sub_num;
        public TextView sub_title;
        public TextView pro_name;
        public TextView capacity_total;
        public TextView capacity_year;
        public TextView grade;
        public Button btn;
//        public Switch start_switch;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sub_num = itemView.findViewById(R.id.subject_num);
            sub_title = itemView.findViewById(R.id.subject_title);
            pro_name = itemView.findViewById(R.id.professor_name);
            capacity_total = itemView.findViewById(R.id.capacity_total);
            capacity_year = itemView.findViewById(R.id.capacity_year);
            grade = itemView.findViewById(R.id.grade);
//            start_switch = itemView.findViewById(R.id.start_switch);
            btn = itemView.findViewById(R.id.favorite_btn);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.OnItemClick(ViewHolder.this, itemView, getAdapterPosition());
                }
            });


//            start_switch = itemView.findViewById(R.id.start_switch);
//
//            start_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @SneakyThrows
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                        itemChangeListener.OnItemChange(ViewHolder.this,itemView,getAdapterPosition(),isChecked);
//
//                }
//            });
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String sub_title = filteredList.get(position).getSubject_title();
        holder.sub_title.setText(sub_title);
        holder.pro_name.setText(filteredList.get(position).getProfessor_name());

        int sbj_num = filteredList.get(position).getSubject_num();
        if (lectureList.contains(Lecture.builder().subject_num(sbj_num).build())) {
            holder.btn.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
        }
        holder.sub_num.setText(String.format("%04d", sbj_num));

        holder.capacity_total.setText(filteredList.get(position).getCapacity_total());
        holder.capacity_year.setText(filteredList.get(position).getCapacity_year());
        holder.grade.setText(filteredList.get(position).getCredit()+ "학년");
    }

    @Override
    public int getItemCount() {
        if (filteredList == null) {
            return 0;
        } else {
            return filteredList.size();
        }

    }
    private LectureDao getLectureDao() {

        LectureDatabase db = Room.databaseBuilder(context, LectureDatabase.class, "my_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();

        return db.lectureDao();

    }

}

