package com.sbsj.ordermatstaboss.Util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbsj.ordermatstaboss.R;

public class CustomBtnLogin extends LinearLayout implements View.OnClickListener {

	public RelativeLayout relativeLayout;
	ImageView imageView;
	TextView textView;

	OnClickButtonListener onCustomClickListener;

	public CustomBtnLogin(Context context) {
		super(context);
		initView();
	}

	public CustomBtnLogin(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		getAttrs(attrs);
	}

	public CustomBtnLogin(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		initView();
		getAttrs(attrs, defStyle);
	}

	private void initView() {
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
		View v = li.inflate(R.layout.custom_btn_login, this, false);
		addView(v);

		relativeLayout = findViewById(R.id.rl_login);
		imageView = findViewById(R.id.iv_login);
		textView = findViewById(R.id.tv_login);
	}

	private void getAttrs(AttributeSet attrs) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomBtnLogin);
		setTypeArray(typedArray);
	}

	private void getAttrs(AttributeSet attrs, int defStyle) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomBtnLogin, defStyle, 0);
		setTypeArray(typedArray);
	}

	private void setTypeArray(TypedArray typedArray) {
		relativeLayout.setOnClickListener(this);
		int bg_resID = typedArray.getResourceId(R.styleable.CustomBtnLogin_bg, R.color.btn_google_bg);
		relativeLayout.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(bg_resID)));
//		relativeLayout.setbackground(getResources().getColor(bg_resID));
		relativeLayout.setOnClickListener(this);


		int image_resID = typedArray.getResourceId(R.styleable.CustomBtnLogin_symbol, R.drawable.ic_google);
		imageView.setImageResource(image_resID);
		imageView.setOnClickListener(this);

		String text_string = typedArray.getString(R.styleable.CustomBtnLogin_text);
		textView.setText(text_string);
		textView.setOnClickListener(this);

		typedArray.recycle();
	}

	public void setimage(int image_resID) {
		imageView.setImageResource(image_resID);
	}

	public void setText(String text_string) {
		textView.setText(text_string);
	}

	public void setText(int text_resID) {
		textView.setText(text_resID);
	}

	public void setOnCustomClickListener(OnClickButtonListener onCustomClickListener) {
		this.onCustomClickListener = onCustomClickListener;
	}

	@Override
	public void onClick(View view) {
		onCustomClickListener.onClick((View)view.getParent());
	}
}