package com.example.nodeproject2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nodeproject2.R;
import com.example.nodeproject2.datas.Lecture;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    public List<Lecture> data;

    public void swapItems(List<Lecture> items) {
        this.data = items;
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

    Context context;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sub_num;
        public TextView sub_title;
        public TextView pro_name;
        public TextView capacity_total;
        public TextView capacity_year;
        public TextView grade;
        public Button btn;
        public Switch start_switch;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sub_num = itemView.findViewById(R.id.subject_num);
            sub_title = itemView.findViewById(R.id.subject_title);
            pro_name = itemView.findViewById(R.id.professor_name);
            capacity_total = itemView.findViewById(R.id.capacity_total);
            capacity_year = itemView.findViewById(R.id.capacity_year);
            grade = itemView.findViewById(R.id.grade);

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
                    itemChangeListener.OnItemChange(ListAdapter.ViewHolder.this,itemView,getAdapterPosition(),isChecked);

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
        holder.btn.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
        holder.sub_title.setText(data.get(position).getSubject_title());
        holder.pro_name.setText(data.get(position).getProfessor_name());

        int sbj_num = data.get(position).getSubject_num();
        holder.sub_num.setText(String.format("%04d", sbj_num));
        holder.start_switch.setVisibility(View.VISIBLE);

        holder.capacity_total.setText(data.get(position).getCapacity_total());
        holder.capacity_year.setText(data.get(position).getCapacity_year());
        holder.grade.setText(data.get(position).getCredit()+"학년");


    }



    public ListAdapter(Context context, List<Lecture> data) {
        this.data = data;
        this.context = context;
    }


    @Override
    public int getItemCount() {
        if(data == null) {
            return 0;
        }else {
            return data.size();
        }

    }
}
