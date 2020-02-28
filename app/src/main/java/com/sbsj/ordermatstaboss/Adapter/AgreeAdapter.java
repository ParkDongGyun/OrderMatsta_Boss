package com.sbsj.ordermatstaboss.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sbsj.ordermatstaboss.R;
import com.sbsj.ordermatstaboss.Util.OnItemClickListener;

import java.util.ArrayList;

public class AgreeAdapter extends RecyclerView.Adapter<AgreeAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> datalist;
    private OnItemClickListener onItemClickListener;
    public ArrayList<Boolean> checkAgree = new ArrayList<>();

    public AgreeAdapter(Context context, ArrayList<String> datalist, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.datalist = datalist;
        this.onItemClickListener = onItemClickListener;

        for(int i=0;i<datalist.size();++i) {
            checkAgree.add(false);
        }
    }

    public void setDatalist(ArrayList<String> datalist) {
        this.datalist = datalist;
    }

    public ArrayList<String> getDatalist() {
        return datalist;
    }

    @NonNull
    @Override
    public AgreeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AgreeAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agreement, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AgreeAdapter.ViewHolder holder, int position) {
        holder.tv_agree.setText(datalist.get(position));
        holder.checkBox.setChecked(checkAgree.get(position));
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CheckBox checkBox;
        TextView tv_agree;
        TextView tv_agreedetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.cb_agree);
            checkBox.setOnClickListener(this);
            tv_agree = itemView.findViewById(R.id.tv_agree);
            tv_agree.setOnClickListener(this);
            tv_agreedetail = itemView.findViewById(R.id.tv_agree_detail);
            tv_agreedetail.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}