package com.example.nodeproject2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.example.nodeproject2.API.Lecture.LectureDao;
import com.example.nodeproject2.API.Lecture.LectureDatabase;
import com.example.nodeproject2.R;
import com.example.nodeproject2.datas.Lecture;

import java.util.ArrayList;
import java.util.List;

public class LiberalArtsAdapter extends RecyclerView.Adapter<LiberalArtsAdapter.ViewHolder> implements Filterable{


    private ArrayList<Lecture> unFilteredlist;
    private ArrayList<Lecture> filteredList;

    private List<Lecture> lectureList;
    private String type="";
    Context context;


    public interface OnItemClickListener {
        void OnItemClick(LiberalArtsAdapter.ViewHolder holder, View view, int pos);
    }

    private LiberalArtsAdapter.OnItemClickListener itemClickListener = null;

    public void setOnItemClickListener(LiberalArtsAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void swapItems(ArrayList<Lecture> items,String s) {
        this.unFilteredlist = items;
        this.filteredList = items;
        getFilter().filter(s);
        notifyDataSetChanged();
    }

    public LiberalArtsAdapter(Context context, ArrayList<Lecture> data) {
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
                if(constraint.equals("")) {
                    filteredList = unFilteredlist;
                }else {
                    for (Lecture lec : unFilteredlist) {
                        if (lec.getSubject_title().contains(charString)) {
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
        public TextView year;
        public Button btn;
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
        String sub_title = filteredList.get(position).getSubject_title();
        holder.sub_title.setText(sub_title);
        holder.pro_name.setText(filteredList.get(position).getProfessor_name());

        int sbj_num = filteredList.get(position).getSubject_num();

        holder.sub_num.setText(String.format("%04d", sbj_num));
        holder.type.setText(filteredList.get(position).getMajor_division());
        holder.year.setText(filteredList.get(position).getYear());
        holder.year.setVisibility(View.INVISIBLE);
        holder.capacity_total.setText(filteredList.get(position).getCapacity_total());
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
