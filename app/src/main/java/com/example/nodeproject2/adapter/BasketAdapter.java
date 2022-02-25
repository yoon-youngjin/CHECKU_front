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

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> implements Filterable {

    public List<Lecture> unFilteredlist;
    public List<Lecture> filteredList;
    private boolean checked;
    Context context;

    public void swapItems(List<Lecture> items, boolean checked) {
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
    private OnItemClickListener RegisterListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }
    public void setOnRegisterClickListener(OnItemClickListener listener) {
        this.RegisterListener = listener;
    }


    public BasketAdapter(Context context, List<Lecture> data) {
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
                if (checked == false) {
                    filteredList = unFilteredlist;
                } else {
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
        public TextView note;
        public TextView room;
        public TextView detail;
        public Button register;
        public LinearLayout detail_layout;


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
            note = itemView.findViewById(R.id.detail);
            room = itemView.findViewById(R.id.room);
            start_switch = itemView.findViewById(R.id.start_switch);
            detail = itemView.findViewById(R.id.detail);
            register = itemView.findViewById(R.id.register_btn);
            detail_layout = itemView.findViewById(R.id.detail_layout);

//            register.setVisibility(View.VISIBLE);


            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.OnItemClick(ViewHolder.this, itemView, getAdapterPosition());
                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RegisterListener.OnItemClick(ViewHolder.this,itemView,getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(note.getVisibility() == View.VISIBLE) {
                        note.setVisibility(View.GONE);
                    }else {
                        note.setVisibility(View.VISIBLE);
                    }
                }
            });


            start_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @SneakyThrows
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    itemChangeListener.OnItemChange(BasketAdapter.ViewHolder.this, itemView, getAdapterPosition(), isChecked);

                }
            });
        }
    }


    @NonNull
    @Override
    public BasketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewitem, parent, false);
        return new BasketAdapter.ViewHolder(view);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull BasketAdapter.ViewHolder holder, int position) {
        int sbj_num = filteredList.get(position).getSubject_num();
        String year = filteredList.get(position).getYear();
        String pro_name = filteredList.get(position).getProfessor_name();
        String room = filteredList.get(position).getRoom();
        String detail = filteredList.get(position).getDetail();
        holder.detail_layout.setVisibility(View.GONE);

        if (!(pro_name == null)) {
            holder.pro_name.setText(pro_name.trim());
        } else {
            holder.pro_name.setText(pro_name);
        }
        holder.btn.setBackgroundResource(R.drawable.btn_favorite_on);
        holder.sub_title.setText(filteredList.get(position).getSubject_title());
        holder.sub_num.setText(String.format("%04d", sbj_num));
        holder.sub_num.setTextColor(Color.WHITE);
//        holder.start_switch.setVisibility(View.VISIBLE);
        holder.capacity_total.setText(filteredList.get(position).getCapacity_total());
        holder.empty.setText(String.valueOf(filteredList.get(position).getEmptySize()));
        holder.room.setText(filteredList.get(position).getRoom());

        if (year.equals("9")) {
            holder.grade.setText("전체");
        } else {
            holder.grade.setText(year + "학년");
        }


        holder.grade.setTextColor(Color.WHITE);
        holder.type.setText(filteredList.get(position).getMajor_division());
        holder.type.setTextColor(Color.WHITE);


        if (!(room == null)) {
            holder.room.setText(room.trim());
        } else {
            holder.room.setText(room);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.detail.getVisibility() == View.VISIBLE) {
                    holder.detail.setVisibility(View.GONE);
                }else {
                    if(!holder.detail.getText().equals(""))
                        holder.detail.setVisibility(View.VISIBLE);
                }
            }
        });
        if (!(detail == null)) {
            holder.detail.setText(detail.trim());
        } else {
            holder.detail.setText(detail);
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


    @Override
    public int getItemCount() {
        if (filteredList == null) {
            return 0;
        } else {
            return filteredList.size();
        }

    }
}
