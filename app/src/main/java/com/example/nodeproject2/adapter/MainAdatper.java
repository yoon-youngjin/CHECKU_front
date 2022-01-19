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
    Context context;
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView sub_num;
        TextView sub_title;
        TextView pro_name;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sub_num = itemView.findViewById(R.id.subject_num);
            sub_title = itemView.findViewById(R.id.subject_title);
            pro_name = itemView.findViewById(R.id.professor_name);
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

