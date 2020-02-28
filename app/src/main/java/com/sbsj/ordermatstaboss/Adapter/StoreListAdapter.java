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
import com.sbsj.ordermatstaboss.DB.StoreInfoColumn;
import com.sbsj.ordermatstaboss.Util.OnItemClickListener;
import com.sbsj.ordermatstaboss.R;

import java.util.ArrayList;

public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.ViewHolder> {

	private Context context;
	private OnItemClickListener listener;
	private ArrayList<StoreInfoColumn> datalist;

	public StoreListAdapter (Context context, ArrayList<StoreInfoColumn> datalist, OnItemClickListener onItemClickListener) {
		this.context = context;
		this.datalist = datalist;
		this.listener = onItemClickListener;
	}

	public ArrayList<StoreInfoColumn> getDatalist() {
		return datalist;
	}

	public void setDatalist(ArrayList<StoreInfoColumn> datalist) {
		this.datalist = datalist;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_storelist, parent,false));
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		StoreInfoColumn column = getDatalist().get(position);

		if(column.getImage() != null)
			holder.iv_store.setImageBitmap(((BaseActivity)context).StringtoBitmap(column.getImage(),"Store_Activity"));
		holder.tv_name.setText(column.getName());
		holder.tv_address.setText(column.getAddress() + " " + column.getAddress_detail());
		if(column.getOpen() == 0) {
			holder.iv_prepare.setVisibility(View.VISIBLE);
		} else {
			holder.iv_prepare.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public int getItemCount() {
		return datalist.size();
	}

	public StoreInfoColumn getItem(int position) {
		return datalist.get(position);
//		return null;
	}

	class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		RelativeLayout rl_item;
		ImageView iv_store;
		ImageView iv_prepare;
		TextView tv_name;
		TextView tv_address;
		ImageView iv_review;
		TextView tv_review;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);

			rl_item = itemView.findViewById(R.id.rl_storeitem);
			iv_store = itemView.findViewById(R.id.iv_storeimage);
			iv_prepare = itemView.findViewById(R.id.iv_storeprepare);
			tv_name = itemView.findViewById(R.id.tv_storeName);
			tv_address = itemView.findViewById(R.id.tv_storeaddress);
			iv_review = itemView.findViewById(R.id.iv_storereview);
			tv_review = itemView.findViewById(R.id.tv_storereview);

			rl_item.setOnClickListener(this);
			iv_review.setOnClickListener(this);
			tv_review.setOnClickListener(this);
		}

		@Override
		public void onClick(View view) {
			listener.onItemClick(view, getAdapterPosition());
		}
	}
}
