package com.sbsj.ordermatstaboss.Util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbsj.ordermatstaboss.R;

public class Custom_Textview_item extends LinearLayout {

	TextView tv_title;
	TextView tv_value;

	public Custom_Textview_item(Context context) {
		super(context);
		initView();
	}

	public Custom_Textview_item(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		getAttrs(attrs);
	}

	public Custom_Textview_item(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		initView();
		getAttrs(attrs, defStyle);
	}

	private void initView() {
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
		View v = li.inflate(R.layout.custom_textview_item, this, false);
		addView(v);

		tv_title = findViewById(R.id.tv_order_title);
		tv_value = findViewById(R.id.tv_order_value);
	}

	private void getAttrs(AttributeSet attrs) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomOrderItem);
		setTypeArray(typedArray);
	}

	private void getAttrs(AttributeSet attrs, int defStyle) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomOrderItem, defStyle, 0);
		setTypeArray(typedArray);
	}

	private void setTypeArray(TypedArray typedArray) {
		String text_string = typedArray.getString(R.styleable.CustomOrderItem_title);
		tv_title.setText(text_string);

		text_string = typedArray.getString(R.styleable.CustomOrderItem_value);
		tv_value.setText(text_string);

		int value_color = typedArray.getResourceId(R.styleable.CustomOrderItem_value_color, R.color.black);
		tv_value.setTextColor(getResources().getColor(value_color));

		int value_style = typedArray.getInteger(R.styleable.CustomOrderItem_value_style, Typeface.NORMAL);
		tv_value.setTypeface(null, value_style);

		typedArray.recycle();
	}

	public void setTv_title(TextView tv_title) {
		this.tv_title = tv_title;
	}

	public void setTv_value(TextView tv_value) {
		this.tv_value = tv_value;
	}
}
