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

public class LiberalArtsAdapter extends RecyclerView.Adapter<LiberalArtsAdapter.ViewHolder> implements Filterable{


    private ArrayList<Lecture> unFilteredlist;
    private ArrayList<Lecture> filteredList;
    private BuildLecturDatabase lecturDatabase;
    private ArrayList<Lecture> currentDB;

    private boolean checked;

    Context context;


    public interface OnItemClickListener {
        void OnItemClick(LiberalArtsAdapter.ViewHolder holder, View view, int pos);
    }

    private LiberalArtsAdapter.OnItemClickListener itemClickListener = null;

    public void setOnItemClickListener(LiberalArtsAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void swapItems(ArrayList<Lecture> items,String s,boolean checked) {
        this.unFilteredlist = items;
        this.filteredList = items;
        this.checked = checked;
        currentDB = lecturDatabase.getLecturesFromDB();
        getFilter().filter(s);
        notifyDataSetChanged();
    }

    public LiberalArtsAdapter(Context context, ArrayList<Lecture> data) {
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
                if(constraint.equals("") && checked == false) {
                    filteredList = unFilteredlist;
                }else if(constraint.equals("")) {
                    for (Lecture lec : unFilteredlist) {
                        if (lec.getEmptySize() > 0) {
                            filteringList.add(lec);
                        }
                    }
                    filteredList = filteringList;
                }else if(checked == true) {
                    for (Lecture lec : unFilteredlist) {
                        if (lec.getSubject_title().contains(charString)) {
                            filteringList.add(lec);
                        }
                    }
                    filteredList = filteringList;
                }
                else {
                    for (Lecture lec : unFilteredlist) {
                        if (lec.getSubject_title().contains(charString) && lec.getEmptySize() > 0) {
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
        public TextView type;
        public TextView empty;
        public TextView year;
        public TextView room;
        public Button btn;
        public TextView detail;
        public LinearLayout detail_layout;

//        public Switch start_switch;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sub_num = itemView.findViewById(R.id.subject_num);
            sub_title = itemView.findViewById(R.id.subject_title);
            pro_name = itemView.findViewById(R.id.professor_name);
            capacity_total = itemView.findViewById(R.id.capacity_total);
            type = itemView.findViewById(R.id.type);
            btn = itemView.findViewById(R.id.favorite_btn);
            year = itemView.findViewById(R.id.grade);
            room = itemView.findViewById(R.id.room);
            empty = itemView.findViewById(R.id.emptySize);
            detail = itemView.findViewById(R.id.detail);
            detail_layout = itemView.findViewById(R.id.detail_layout);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.OnItemClick(LiberalArtsAdapter.ViewHolder.this, itemView, getAdapterPosition());
                }
            });
        }
    }


    @NonNull
    @Override
    public LiberalArtsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewitem, parent, false);
        return new LiberalArtsAdapter.ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull LiberalArtsAdapter.ViewHolder holder, int position) {
        int sbj_num = filteredList.get(position).getSubject_num();
        String pro_name = filteredList.get(position).getProfessor_name();
        String sub_title = filteredList.get(position).getSubject_title();
        String room = filteredList.get(position).getRoom();
        String detail = filteredList.get(position).getDetail();
        String year = filteredList.get(position).getYear();

        holder.detail_layout.setVisibility(View.GONE);

        checkData(pro_name, year, room, detail, holder);

        holder.sub_title.setText(sub_title);
        holder.sub_num.setText(String.format("%04d", sbj_num));
        holder.type.setText(filteredList.get(position).getMajor_division());
        holder.empty.setText(String.valueOf(filteredList.get(position).getEmptySize()));
        holder.btn.setBackgroundResource(R.drawable.btn_favorite_off);
        holder.sub_num.setTextColor(Color.BLACK);
        holder.type.setTextColor(Color.BLACK);
        holder.year.setTextColor(Color.BLACK);
        holder.capacity_total.setText(filteredList.get(position).getCapacity_total());

        if(currentDB.contains(Lecture.builder().subject_num(sbj_num).build())) {
            holder.btn.setBackgroundResource(R.drawable.btn_favorite_on);
            holder.sub_num.setTextColor(Color.WHITE);
            holder.type.setTextColor(Color.WHITE);
            holder.year.setTextColor(Color.WHITE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.detail_layout.getVisibility() == View.VISIBLE) {
                    holder.detail_layout.setVisibility(View.GONE);
                }else {
                    if(!holder.detail.getText().equals(""))
                        holder.detail_layout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void checkData(String pro_name, String year, String room, String detail, ViewHolder holder) {

        if(!(pro_name == null)) {
            holder.pro_name.setText(pro_name.trim());
        }else {
            holder.pro_name.setText(pro_name);
        }
        if(year.equals("9")) {
            holder.year.setText("??????");
        }else {
            holder.year.setText(year+"??????");
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
