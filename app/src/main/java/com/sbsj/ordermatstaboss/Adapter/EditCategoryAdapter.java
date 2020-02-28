package com.sbsj.ordermatstaboss.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sbsj.ordermatstaboss.DB.MenuCateInfoColumn;
import com.sbsj.ordermatstaboss.Util.OnItemClickListener;
import com.sbsj.ordermatstaboss.Util.OnItemTouchHelpListener;
import com.sbsj.ordermatstaboss.Util.OnStartDargListener;
import com.sbsj.ordermatstaboss.R;

import java.util.ArrayList;
import java.util.Collections;

public class EditCategoryAdapter extends RecyclerView.Adapter<EditCategoryAdapter.ViewHolder> implements OnItemTouchHelpListener {

    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnStartDargListener onStartDargListener;
    private ArrayList<MenuCateInfoColumn> datalist;
    private ArrayList<EditText> et_menuList = new ArrayList<>();

    public EditCategoryAdapter(Context context, ArrayList<MenuCateInfoColumn> datalist, OnItemClickListener onItemClickListener, OnStartDargListener onStartDargListener) {
        this.context = context;
        this.datalist = datalist;
        this.onItemClickListener = onItemClickListener;
        this.onStartDargListener = onStartDargListener;
    }

    public ArrayList<MenuCateInfoColumn> getDatalist() {
        return datalist;
    }
    public void setDatalist(ArrayList<MenuCateInfoColumn> datalist) {
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_edit_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final int dataposition = position;

        holder.tv_category.setText(Integer.toString(position + 1) +".");
        holder.et_category.setText(datalist.get(position).getName());
        holder.et_category.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                datalist.get(dataposition).setName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.iv_category.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN) {
                    onStartDargListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public MenuCateInfoColumn getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < 0 || fromPosition >= datalist.size() || toPosition < 0 || toPosition >= datalist.size())
            return false;

        Collections.swap(datalist, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);

        return true;
    }

    @Override
    public void onItemRemove(int position) {
        datalist.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout ll_category;
        TextView tv_category;
        EditText et_category;
        ImageView iv_category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ll_category = itemView.findViewById(R.id.ll_ec);
            tv_category = itemView.findViewById(R.id.tv_ec_num);
            et_category = itemView.findViewById(R.id.et_ec_categoryname);
            et_category.setHint("카테고리를 입력해주세요.");
            iv_category = itemView.findViewById(R.id.iv_ec_sequence);

            ll_category.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}