package com.sbsj.ordermatstaboss.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.sbsj.ordermatstaboss.DB.GoodsInfo;
import com.sbsj.ordermatstaboss.DB.GoodsInfoColumn;
import com.sbsj.ordermatstaboss.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterGoodsActivity extends BaseActivity implements View.OnClickListener {

	private final String TAG = "RegisterGoods_Activity";
	private final int RequestCode_Album = 1000;

	private Toolbar toolbar;

	private ImageView iv_goods;
	private ImageView iv_goods2;

	private EditText et_name;
	private EditText et_detail;
	private EditText et_price;

	private Button btn_complete;

	private int store_id;
	private int goods_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_goods);

		initlayout();

		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeAsUpIndicator(R.drawable.btn_header_back);// 툴바 왼쪽 버튼 이미지

		goods_id = getIntent().getIntExtra(getString(R.string.goods_id), -1);
		if (goods_id != -1) {
			setupLayout();
		}
		store_id = getIntent().getIntExtra(getString(R.string.store_id), -1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RequestCode_Album) {
			if (resultCode == RESULT_OK) {
				try {
					InputStream inputStream = getContentResolver().openInputStream(data.getData());
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					inputStream.close();

					iv_goods.setImageBitmap(bitmap);
				} catch (Exception e) {
					e.printStackTrace();
					Log.i(TAG, "error : " + e.toString());
				}
			} else {
				Log.i(TAG, "error : ");
			}
		}
	}

	@Override
	public void onBackPressed() {
		quitActivity(getString(R.string.backAlert_register_title), getString(R.string.backAlert_register_msg));
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			quitActivity(getString(R.string.backAlert_register_title), getString(R.string.backAlert_register_msg));
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.iv_rg_image:
				goGallery();
				break;
			case R.id.iv_rg_image2:
				goGallery();
				break;
			case R.id.btn_rg_complete:
				if (goods_id == -1)
					insertGoodsInfo();
				else
					updateGoodsInfo();
		}
	}

	private void initlayout() {
		toolbar = findViewById(R.id.tb_rg);

		iv_goods = findViewById(R.id.iv_rg_image);
		iv_goods.setOnClickListener(this);
		iv_goods2 = findViewById(R.id.iv_rg_image2);
		iv_goods2.setOnClickListener(this);

		et_name = findViewById(R.id.et_rg_name);
		et_detail = findViewById(R.id.et_rg_detail);
		et_price = findViewById(R.id.et_rg_price);

		btn_complete = findViewById(R.id.btn_rg_complete);
		btn_complete.setOnClickListener(this);
	}

	private void insertGoodsInfo() {
		if (!networkConnect()) {
			Toast.makeText(this, getString(R.string.check_network), Toast.LENGTH_SHORT).show();
			return;
		}

		Call<String> call = getDbService().insertgoodsinfo(makeMap_Column(getResources().getStringArray(R.array.goodsinfo_column), makeGoodsinfoColumn()));
		call.enqueue(new Callback() {
			@Override
			public void onResponse(retrofit2.Call call, Response response) {
				if (response.message().equals(getString(R.string.NetworkCheck))) {
					Log.i(TAG, "insertGoodsInfo message : " + response.message());
					Log.i(TAG, "insertGoodsInfo result : " + response.body());

					finish();
				} else {
					Log.i(TAG, "insertGoodsInfo error : " + response.message());
				}
			}

			@Override
			public void onFailure(retrofit2.Call call, Throwable t) {
				Log.i(TAG, "insertGoodsInfo fail : " + t.getMessage());
			}
		});
	}

	private void updateGoodsInfo() {
		if (!networkConnect()) {
			Toast.makeText(this, getString(R.string.check_network), Toast.LENGTH_SHORT).show();
			return;
		}
		Call<String> call = getDbService().updategoodsinfo(makeMap_Column(getResources().getStringArray(R.array.goodsinfo_column), makeGoodsinfoColumn()));
		call.enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				if (response.message().equals(getString(R.string.NetworkCheck))) {
					Log.i(TAG, "updateGoodsInfo message : " + response.message());
					Log.i(TAG, "updateGoodsInfo result : " + response.body());

					finish();
				} else {
					Log.i(TAG, "updateGoodsInfo error : " + response.message());
				}
			}

			@Override
			public void onFailure(retrofit2.Call call, Throwable t) {
				Log.i(TAG, "updateGoodsInfo fail : " + t.getMessage());
			}
		});
	}

	private void setupLayout() {
		Call<GoodsInfo> call = getDbService().getGoodInfo(goods_id);
		call.enqueue(new Callback<GoodsInfo>() {
			@Override
			public void onResponse(Call<GoodsInfo> call, Response<GoodsInfo> response) {
				if(response.message().equals(getString(R.string.NetworkCheck))) {
					GoodsInfoColumn column = response.body().getGoodsinfolist().get(0);
					et_name.setText(column.getName());
					et_price.setText(Integer.toString(column.getPrice()));
					et_detail.setText(column.getDetail());
					if(column.getImage() != null)
						iv_goods.setImageBitmap(StringtoBitmap(column.getImage(), TAG));
				} else {
					Log.i(TAG, "getgoodinfo error : "+ response.message());
				}
			}

			@Override
			public void onFailure(Call<GoodsInfo> call, Throwable t) {
				Log.i(TAG, "getgoodinfo fail : "+ t.getMessage());
			}
		});
	}

	public static HashMap<String, String> makeMap_Column(String[] field, GoodsInfoColumn column) {
		HashMap<String, String> map = new HashMap<>();

		map.put(field[0], Integer.toString(column.getId()));
		map.put(field[1], Integer.toString(column.getStore_id()));
		map.put(field[2], column.getName());
		map.put(field[3], Integer.toString(column.getPrice()));
		map.put(field[4], column.getDetail());
		map.put(field[5], column.getImage());

		return map;
	}

	private GoodsInfoColumn makeGoodsinfoColumn() {
		GoodsInfoColumn column = new GoodsInfoColumn();
		column.setId(goods_id);
		column.setStore_id(store_id);
		column.setName(et_name.getText().toString());
		if(et_price.getText().toString().equals(""))
			column.setPrice(0);
		else
			column.setPrice(Integer.parseInt(et_price.getText().toString()));
		column.setDetail(et_detail.getText().toString());
		column.setImage(BitmapToString(resize(((BitmapDrawable) iv_goods.getDrawable()).getBitmap()), TAG));

		return column;
	}

	private void goGallery() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
		startActivityForResult(intent, RequestCode_Album);
	}
}
