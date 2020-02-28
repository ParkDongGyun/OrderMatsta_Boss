package com.sbsj.ordermatstaboss.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sbsj.ordermatstaboss.Activity.BaseActivity;
import com.sbsj.ordermatstaboss.Activity.OrderlistActivity;
import com.sbsj.ordermatstaboss.DB.GoodsInfoColumn;
import com.sbsj.ordermatstaboss.DB.OrderDetailInfoColumn;
import com.sbsj.ordermatstaboss.Util.OnItemClickListener;
import com.sbsj.ordermatstaboss.R;

import java.util.ArrayList;
import java.util.List;

public class OrderlistAdapter extends RecyclerView.Adapter<OrderlistAdapter.ViewHolder> {

   /* public static final int HEADER = 0;
    public static final int CHILD = 1;

    private ArrayList<Item> datalist;

    private OnItemClickListener listener;

    public OrderlistAdapter(ArrayList<Item> datalist, OnItemClickListener listener) {
        this.datalist = datalist;
        this.listener = listener;
    }

    public void setDatalist(ArrayList<Item> datalist) {
        this.datalist = datalist;
    }

    public ArrayList<Item> getDatalist() {
        return datalist;
    }

    @NonNull
    @Override
    public OrderlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        Context context = parent.getContext();
        float dp = context.getResources().getDisplayMetrics().density;
        int subItemPaddingLeft = (int)(18*dp);
        int subItemPaddingTopAndBottom = (int)(5*dp);
        switch (viewType) {
            case HEADER:
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.item_orderlist, parent, false);
                ViewHolder header = new ViewHolder(view);
                return header;
            case CHILD:
                TextView itemTextView = new TextView(context);
                itemTextView.setPadding(subItemPaddingLeft, subItemPaddingTopAndBottom, 0, subItemPaddingTopAndBottom);
                itemTextView.setTextColor(0x88000000);
                itemTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new OrderlistAdapter.ViewHolder(itemTextView) {};
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderlistAdapter.ViewHolder holder, int position) {
        final Item item = datalist.get(position);
        switch (item.type) {
            case HEADER:
                final ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.refferalItem = item;
                viewHolder.tv_ordernum.setText(item.text);
                if (item.invisibleChildren == null) {
                    viewHolder.iv_expand.setRotation(90);
//                    itemController.iv_expand.setImageResource(R.drawable.arrow_right);
                } else {
                    viewHolder.iv_expand.setRotation(270);
                }
                viewHolder.iv_expand.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<Item>();
                            int count = 0;
                            int pos = datalist.indexOf(viewHolder.refferalItem);

                            while (datalist.size() > pos + 1 && datalist.get(pos + 1).type == CHILD) {
                                item.invisibleChildren.add(datalist.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            viewHolder.iv_expand.setRotation(270);
                        } else {
                            int pos = datalist.indexOf(viewHolder.refferalItem);
                            int index = pos + 1;

                            for (Item i : item.invisibleChildren) {
                                datalist.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            viewHolder.iv_expand.setRotation(90);
                            item.invisibleChildren = null;
                        }
                    }
                });
                break;
            case CHILD:
                holder.tv_ordernum.setText(datalist.get(position).text);
                holder.btn_accept.setVisibility(View.INVISIBLE);
                holder.btn_decline.setVisibility(View.INVISIBLE);
                holder.iv_expand.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public Item getItem(int position) {
        return datalist.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        LinearLayout ll_item;
        public Item refferalItem;

        ImageView iv_userlevel;
        TextView tv_ordernum;
        Button btn_accept;
        Button btn_decline;
        ImageView iv_expand;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            iv_userlevel = itemView.findViewById(R.id.iv_userlevel);
//            tv_ordernum = itemView.findViewById(R.id.tv_ordernumber);
//            btn_accept = itemView.findViewById(R.id.btn_accept);
//            btn_decline = itemView.findViewById(R.id.btn_decline);
//            iv_expand = itemView.findViewById(R.id.iv_expand);

            btn_accept.setOnClickListener(this);
            btn_decline.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(view, getAdapterPosition());
        }
    }

    public static class Item {
        public int type;
        public String text;
        public List<Item> invisibleChildren;
        public ArrayList<OrderlistActivity.Order> orderlist;

        public Item() {

        }

        public Item(int type, String text) {
            this.type = type;
            this.text = text;
        }
    }*/

    private Context context;
    private ArrayList<OrderDetailInfoColumn> datalist;

    public OrderlistAdapter (Context context, ArrayList<OrderDetailInfoColumn> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    public void setDatalist(ArrayList<OrderDetailInfoColumn> datalist) {
        this.datalist = datalist;
    }

    public ArrayList<OrderDetailInfoColumn> getDatalist() {
        return datalist;
    }

    @NonNull
    @Override
    public OrderlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderlistAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderlist, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderlistAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public OrderDetailInfoColumn getItem(int position) {
        return datalist.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}