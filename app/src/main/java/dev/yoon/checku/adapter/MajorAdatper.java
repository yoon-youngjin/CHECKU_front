package com.example.nodeproject2.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.example.nodeproject2.config.BuildLecturDatabase;
import com.example.nodeproject2.datas.Lecture;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MajorAdatper extends RecyclerView.Adapter<MajorAdatper.ViewHolder> implements Filterable {

    private ArrayList<Lecture> unFilteredlist;
    private ArrayList<Lecture> filteredList;
    private BuildLecturDatabase lecturDatabase;
    private ArrayList<Lecture> currentDB;

    private boolean checked = false;
    private String current_grade = "";
    private String type = "";

    Context context;


    public interface OnItemClickListener {
        void OnItemClick(ViewHolder holder, View view, int pos);
    }

    private OnItemClickListener itemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void swapItems(ArrayList<Lecture> items, String s, String current_grade, String type, boolean current_checked) {
        this.unFilteredlist = items;
        this.filteredList = items;
        this.current_grade = current_grade;
        this.checked = current_checked;
        this.type = type;
        currentDB = lecturDatabase.getLecturesFromDB();
        getFilter().filter(s);
        notifyDataSetChanged();
    }


    public MajorAdatper(Context context, ArrayList<Lecture> data) {
        this.unFilteredlist = data;
        this.filteredList = data;
        this.context = context;
        this.lecturDatabase = new BuildLecturDatabase(this.context, "lecture_table");
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString().toUpperCase(Locale.ROOT);
                ArrayList<Lecture> filteringList = new ArrayList<>();

                // ????????????x && ????????????x && ????????????x && ?????????x=> filtering x
                if (charString.equals("") && current_grade.equals("") && type.equals("") && checked == false) {
                    filteredList = unFilteredlist;
                }
                // ???????????? o && ???????????? x && ????????????x && ?????????x
                else if (charString.equals("") && type.equals("") && checked == false) {

                    for (Lecture lec : unFilteredlist) {
                        if (lec.getYear().contains(current_grade)) {
                            filteringList.add(lec);
                        }
                    }
                    filteredList = filteringList;
                }
                // ????????????x && ????????????o && ????????????x && ?????????x
                else if (current_grade.equals("") && type.equals("") && checked == false) {

                    for (Lecture lec : unFilteredlist) {
                        if (lec.getSubject_title().contains(charString)) {
                            filteringList.add(lec);
                        }
                    }
                    filteredList = filteringList;

                }
                // ????????????x && ????????????x && ????????????o && ?????????x
                else if (current_grade.equals("") && charString.equals("") && checked == false) {

                    for (Lecture lec : unFilteredlist) {
                        if (type.contains(lec.getMajor_division())) {
                            filteringList.add(lec);
                        }
                    }
                    filteredList = filteringList;

                }
                // ????????????x && ????????????x && ????????????x && ?????????o
                else if (current_grade.equals("") && charString.equals("") && type.equals("") && checked == true) {
                    for (Lecture lec : unFilteredlist) {
                        if (lec.getEmptySize() != 0) {
                            filteringList.add(lec);
                        }
                    }
                    filteredList = filteringList;
                }

                // ????????????o && ????????????o && ????????????x && ?????????x
                else if (type.equals("") && checked == false) {
                    for (Lecture lec : unFilteredlist) {
                        if (lec.getYear().contains(type) && lec.getSubject_title().contains(charString)) {
                            filteringList.add(lec);
                        }
                    }
                    filteredList = filteringList;
                }

                // ????????????o && ????????????x && ????????????x && ?????????o
                else if (charString.equals("") && type.equals("") && checked == true) {
                    System.out.println("checked");
                    for (Lecture lec : unFilteredlist) {
                        if (lec.getYear().contains(current_grade) && lec.getEmptySize() > 0) {
                            filteringList.add(lec);
                        }
                    }
                    filteredList = filteringList;
                }

                // ????????????o && ????????????x && ????????????o && ?????????x
                else if (charString.equals("") && checked == false) {

                    for (Lecture lec : unFilteredlist) {
                        if (lec.getYear().contains(current_grade) && type.contains(lec.getMajor_division())) {
                            filteringList.add(lec);
                        }
                    }
                    filteredList = filteringList;

                }
                // ????????????x && ????????????o && ????????????o && ?????????x
                else if (current_grade.equals("") && checked == false) {
                    for (Lecture lec : unFilteredlist) {
                        if (lec.getSubject_title().contains(charString) && type.contains(lec.getMajor_division())) {
                            filteringList.add(lec);
                        }
                    }
                    filteredList = filteringList;
                }

                // ????????????x && ????????????o && ????????????x && ?????????o
                else if (current_grade.equals("") && type.equals("") && checked == true) {
                    for (Lecture lec : unFilteredlist) {
                        if (lec.getSubject_title().contains(charString) && lec.getEmptySize() > 0) {
                            filteringList.add(lec);
                        }
                    }
                    filteredList = filteringList;
                }
                // ????????????x && ????????????x && ????????????o && ?????????o
                else if (current_grade.equals("") && charString.equals("") && checked == true) {
                    for (Lecture lec : unFilteredlist) {
                        if (type.contains(lec.getMajor_division()) && lec.getEmptySize() > 0) {
                            filteringList.add(lec);
                        }
                    }
                    filteredList = filteringList;
                }
                // ????????????x && ????????????o && ????????????o && ?????????o
                else if (current_grade.equals("") && checked == true) {
                    for (Lecture lec : unFilteredlist) {
                        if (lec.getSubject_title().contains(charString) && type.contains(lec.getMajor_division()) && lec.getEmptySize() > 0) {
                            filteringList.add(lec);
                        }
                    }
                    filteredList = filteringList;
                }

                // ????????????o && ????????????x && ????????????o && ?????????o
                else if (charString.equals("") && checked == true) {
                    for (Lecture lec : unFilteredlist) {
                        if (lec.getYear().contains(current_grade) && type.contains(lec.getMajor_division()) && lec.getEmptySize() > 0) {
                            filteringList.add(lec);
                        }
                    }
                    filteredList = filteringList;
                }

                // ????????????o && ????????????o && ????????????x && ?????????o
                else if (type.equals("") && checked == true) {
                    for (Lecture lec : unFilteredlist) {
                        if (lec.getYear().contains(current_grade) && lec.getSubject_title().contains(charString) && lec.getEmptySize() > 0) {
                            filteringList.add(lec);
                        }
                    }
                    filteredList = filteringList;
                }

                // ????????????o && ????????????o && ????????????o && ?????????x
                else if (checked == false) {
                    for (Lecture lec : unFilteredlist) {
                        if (lec.getYear().contains(current_grade) && lec.getSubject_title().contains(charString) && type.contains(lec.getMajor_division())) {
                            filteringList.add(lec);
                        }
                    }
                    filteredList = filteringList;
                }
                // ????????????o && ????????????o && ????????????o && ?????????o
                else {
                    for (Lecture lec : unFilteredlist) {
                        if (lec.getYear().contains(current_grade) && lec.getSubject_title().contains(charString) && type.contains(lec.getMajor_division()) && lec.getEmptySize() > 0) {
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


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sub_num;
        public TextView sub_title;
        public TextView pro_name;
        public TextView capacity_total;
        public TextView grade;
        public TextView empty;
        public TextView type;
        public TextView room;
        public TextView detail;
        public Button btn;
        public LinearLayout detail_layout;
//        public Switch start_switch;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sub_num = itemView.findViewById(R.id.subject_num);
            sub_title = itemView.findViewById(R.id.subject_title);
            pro_name = itemView.findViewById(R.id.professor_name);
            capacity_total = itemView.findViewById(R.id.capacity_total);
            grade = itemView.findViewById(R.id.grade);
            empty = itemView.findViewById(R.id.emptySize);
            type = itemView.findViewById(R.id.type);
            room = itemView.findViewById(R.id.room);
            detail = itemView.findViewById(R.id.detail);
            detail_layout = itemView.findViewById(R.id.detail_layout);
            btn = itemView.findViewById(R.id.favorite_btn);
            btn.setBackgroundResource(R.drawable.btn_favorite_off);
//            start_switch = itemView.findViewById(R.id.start_switch);

            setListener();

        }

        private void setListener() {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.OnItemClick(ViewHolder.this, itemView, getAdapterPosition());
                }
            });

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
        int sbj_num = filteredList.get(position).getSubject_num();
        String pro_name = filteredList.get(position).getProfessor_name();
        String room = filteredList.get(position).getRoom();
        String detail = filteredList.get(position).getDetail();

        holder.detail_layout.setVisibility(View.GONE);

        checkData(pro_name, room, detail, holder);


        holder.sub_title.setText(sub_title);
        holder.sub_num.setText(String.format("%04d", sbj_num));
        holder.type.setText(filteredList.get(position).getMajor_division());
        holder.capacity_total.setText(filteredList.get(position).getCapacity_total());
        holder.empty.setText(String.valueOf(filteredList.get(position).getEmptySize()));
        holder.grade.setText(filteredList.get(position).getYear() + "??????");

        holder.btn.setBackgroundResource(R.drawable.btn_favorite_off);
        holder.sub_num.setTextColor(Color.BLACK);
        holder.type.setTextColor(Color.BLACK);
        holder.grade.setTextColor(Color.BLACK);

        if (currentDB.contains(Lecture.builder().subject_num(sbj_num).build())) {
            holder.btn.setBackgroundResource(R.drawable.btn_favorite_on);
            holder.sub_num.setTextColor(Color.WHITE);
            holder.type.setTextColor(Color.WHITE);
            holder.grade.setTextColor(Color.WHITE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.detail_layout.getVisibility() == View.VISIBLE) {
                    holder.detail_layout.setVisibility(View.GONE);
                } else {
                    if (!holder.detail.getText().equals(""))
                        holder.detail_layout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void checkData(String pro_name, String room, String detail, ViewHolder holder) {

        if (!(pro_name == null)) {
            holder.pro_name.setText(pro_name.trim());
        } else {
            holder.pro_name.setText(pro_name);
        }
        if (!(room == null)) {
            holder.room.setText(room.trim());
        } else {
            holder.room.setText(room);
        }
        if (!(detail == null)) {
            holder.detail.setText(detail.trim());
        } else {
            holder.detail.setText(detail);
        }

    }

    @Override
    public int getItemCount() {
        if (filteredList == null) {
            return 0;
        } else {
            return filteredList.size();
        }

    }


}

