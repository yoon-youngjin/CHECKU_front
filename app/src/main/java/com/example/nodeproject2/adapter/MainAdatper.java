package com.example.nodeproject2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nodeproject2.R;
import com.example.nodeproject2.datas.Subject;

import java.util.ArrayList;

public class MainAdatper extends RecyclerView.Adapter<MainAdatper.ViewHolder> {

    ArrayList<Subject> data;

    public interface OnItemClickListener {
        void OnItemClick(ViewHolder holder,View view,int pos) throws InterruptedException;
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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sub_num = itemView.findViewById(R.id.subject_num);
            sub_title = itemView.findViewById(R.id.subject_title);
            pro_name = itemView.findViewById(R.id.professor_name);
            capacity_total = itemView.findViewById(R.id.capacity_total);
            capacity_year = itemView.findViewById(R.id.capacity_year);

            itemView.setOnClickListener(view -> {
                try {
                    itemClickListener.OnItemClick(this,itemView,getAdapterPosition());
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.sub_title.setText(data.get(position).getSubject_title());
        holder.pro_name.setText(data.get(position).getProfessor_name());
        holder.sub_num.setText(data.get(position).getSubject_num());
        holder.capacity_total.setText(data.get(position).getCapacity_total());
        holder.capacity_year.setText(data.get(position).getCapacity_year());

    }

    public MainAdatper(Context context, ArrayList<Subject> data) {
        this.data = data;
        this.context = context;

    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}

