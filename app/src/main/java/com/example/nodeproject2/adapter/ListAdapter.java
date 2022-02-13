package com.example.nodeproject2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nodeproject2.R;
import com.example.nodeproject2.datas.Lecture;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> implements Filterable {

    public List<Lecture> unFilteredlist;
    public List<Lecture> filteredList;
    private boolean checked;
    Context context;

    public void swapItems(List<Lecture> items,boolean checked) {
        this.unFilteredlist = items;
        this.filteredList = items;
        this.checked = checked;
        getFilter().filter("");
        notifyDataSetChanged();
    }



    public interface OnItemClickListener {
        void OnItemClick(ViewHolder holder, View view, int pos);
    }

    public interface OnCheckedChangeListener {
        void OnItemChange(ViewHolder holder, View view, int pos, boolean isChecked) throws IOException;
    }

    private OnCheckedChangeListener itemChangeListener = null;

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.itemChangeListener = listener;
    }

    private OnItemClickListener itemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }


    public ListAdapter(Context context, List<Lecture> data) {
        this.unFilteredlist = data;
        this.filteredList = data;
        this.context = context;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Lecture> filteringList = new ArrayList<>();
                if(checked == false) {
                    filteredList = unFilteredlist;
                }
                else {
                    for (Lecture lec : unFilteredlist) {
                        if (lec.getEmptySize() > 0) {
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
        public TextView empty;
        public TextView grade;
        public TextView type;
        public Button btn;
        public Switch start_switch;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sub_num = itemView.findViewById(R.id.subject_num);
            sub_title = itemView.findViewById(R.id.subject_title);
            pro_name = itemView.findViewById(R.id.professor_name);
            capacity_total = itemView.findViewById(R.id.capacity_total);
            grade = itemView.findViewById(R.id.grade);
            empty = itemView.findViewById(R.id.emptySize);
            type = itemView.findViewById(R.id.type);
            btn = itemView.findViewById(R.id.favorite_btn);
            start_switch = itemView.findViewById(R.id.start_switch);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.OnItemClick(ViewHolder.this, itemView, getAdapterPosition());
                }
            });


            start_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @SneakyThrows
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    itemChangeListener.OnItemChange(ListAdapter.ViewHolder.this, itemView, getAdapterPosition(), isChecked);

                }
            });
        }
    }


    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewitem, parent, false);
        return new ListAdapter.ViewHolder(view);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
        int sbj_num = filteredList.get(position).getSubject_num();
        String year = filteredList.get(position).getYear();

        holder.btn.setBackgroundResource(R.drawable.btn_favorite_on);
        holder.sub_title.setText(filteredList.get(position).getSubject_title());
        holder.pro_name.setText(filteredList.get(position).getProfessor_name().trim());
        holder.sub_num.setText(String.format("%04d", sbj_num));
        holder.sub_num.setTextColor(Color.WHITE);
//        holder.start_switch.setVisibility(View.VISIBLE);
        holder.capacity_total.setText(filteredList.get(position).getCapacity_total());
        holder.empty.setText(String.valueOf(filteredList.get(position).getEmptySize()));
        holder.grade.setText(year);
        if (year.equals("9")) {
//            holder.grade.setVisibility(View.INVISIBLE);
            holder.grade.setText("전체");
        }
        holder.grade.setTextColor(Color.WHITE);
        holder.type.setText(filteredList.get(position).getMajor_division());
        holder.type.setTextColor(Color.WHITE);

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
