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
        void OnItemClick(ViewHolder holder,View view,int pos);
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
        public TextView total_num;
        public TextView current_num;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sub_num = itemView.findViewById(R.id.subject_num);
            sub_title = itemView.findViewById(R.id.subject_title);
            pro_name = itemView.findViewById(R.id.professor_name);
            total_num = itemView.findViewById(R.id.total_number_of_persnals);
            current_num = itemView.findViewById(R.id.current_num);

            itemView.setOnClickListener(view -> {
                itemClickListener.OnItemClick(this,itemView,getAdapterPosition());
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
        holder.sub_title.setText(data.get(position).getSubject_title2());
        holder.pro_name.setText(data.get(position).getProfessor_name());
        holder.sub_num.setText(data.get(position).getSubject_num());
        holder.current_num.setText("0");
        holder.total_num.setText("30");

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

