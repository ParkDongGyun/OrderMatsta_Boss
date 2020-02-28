package com.sbsj.ordermatstaboss.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sbsj.ordermatstaboss.Activity.BaseActivity;
import com.sbsj.ordermatstaboss.DB.GoodsInfoColumn;
import com.sbsj.ordermatstaboss.Util.OnItemClickListener;
import com.sbsj.ordermatstaboss.R;

import java.util.ArrayList;

public class StoreDetailAdapter extends RecyclerView.Adapter<StoreDetailAdapter.ViewHolder> {

    private Context context;
    private OnItemClickListener listener;
    private ArrayList<GoodsInfoColumn> datalist;

    public StoreDetailAdapter (Context context, ArrayList<GoodsInfoColumn> datalist, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.datalist = datalist;
        this.listener = onItemClickListener;
    }

    public void setDatalist(ArrayList<GoodsInfoColumn> datalist) {
        this.datalist = datalist;
    }

    public ArrayList<GoodsInfoColumn> getDatalist() {
        return datalist;
    }

    @NonNull
    @Override
    public StoreDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StoreDetailAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goodslist, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull StoreDetailAdapter.ViewHolder holder, int position) {
        holder.tv_name.setText(datalist.get(position).getName());
        holder.tv_price.setText(BaseActivity.printPrice(context.getString(R.string.oneprice_msg), datalist.get(position).getPrice()));
        holder.iv_store.setImageBitmap(((BaseActivity)context).StringtoBitmap(datalist.get(position).getImage(), "StoreDetail_Activity"));
        holder.tv_detail.setText(datalist.get(position).getDetail());
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public GoodsInfoColumn getItem(int position) {
        return datalist.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout rl_item;
        ImageView iv_store;
        TextView tv_name;
        TextView tv_price;
        TextView tv_detail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rl_item = itemView.findViewById(R.id.rl_sd_item);
            iv_store = itemView.findViewById(R.id.iv_goodsimage);
            tv_name = itemView.findViewById(R.id.tv_goodsname);
            tv_price = itemView.findViewById(R.id.tv_goodsprice);
            tv_detail = itemView.findViewById(R.id.tv_goodsexplain);

            rl_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(view, getAdapterPosition());
        }
    }
}