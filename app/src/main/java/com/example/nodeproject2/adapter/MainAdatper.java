package com.example.nodeproject2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class MainAdatper extends RecyclerView.Adapter<MainAdatper.ViewHolder> {

    ArrayList<Lecture> data;

     public interface OnCheckedChangeListener {
        void OnItemChange(ViewHolder holder,View view,int pos,boolean isChecked) throws IOException;
    }
    private OnCheckedChangeListener itemChangeListener = null;
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.itemChangeListener = listener;
    }

    Context context;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView sub_num;
        public TextView sub_title;
        public TextView pro_name;
        public TextView capacity_total;
        public TextView capacity_year;
        public Switch start_switch;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sub_num = itemView.findViewById(R.id.subject_num);
            sub_title = itemView.findViewById(R.id.subject_title);
            pro_name = itemView.findViewById(R.id.professor_name);
            capacity_total = itemView.findViewById(R.id.capacity_total);
            capacity_year = itemView.findViewById(R.id.capacity_year);
            start_switch = itemView.findViewById(R.id.start_switch);

            start_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @SneakyThrows
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        itemChangeListener.OnItemChange(ViewHolder.this,itemView,getAdapterPosition(),isChecked);

                }
            });
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
        holder.sub_title.setText(data.get(position).getSubject_title());
        holder.pro_name.setText(data.get(position).getProfessor_name());
        holder.sub_num.setText(data.get(position).getSubject_num());
        holder.capacity_total.setText(data.get(position).getCapacity_total());
        holder.capacity_year.setText(data.get(position).getCapacity_year());

    }



    public MainAdatper(Context context, ArrayList<Lecture> data) {
        this.data = data;
        this.context = context;

    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}

