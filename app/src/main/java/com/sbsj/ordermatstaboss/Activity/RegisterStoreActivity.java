package com.sbsj.ordermatstaboss.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.sbsj.ordermatstaboss.DB.StoreInfo;
import com.sbsj.ordermatstaboss.DB.StoreInfoColumn;
import com.sbsj.ordermatstaboss.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterStoreActivity extends BaseActivity implements View.OnClickListener {

	String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
	final int PERMISSIONS_REQUEST = 100;

	private final String TAG = "RegisterStore_Activity";

	private final String MASTER_ID = "master_id";
	private final String NAME = "name";
	private final String PHONE = "phone";
	private final String ADDRESS = "address";
	private final String IMAGE = "image";

	private int master_id;
	private int store_id;

	private Toolbar toolbar;
	private ImageView iv_image1;
	private ImageView iv_image2;
	private EditText et_name;
	private EditText et_address;
	private ImageView iv_address;
	private EditText et_address_detail;
	private EditText et_phone;
	private EditText et_explain;
	private Button btn_complete;

	private StoreInfoColumn storeinfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_store);

		master_id = BaseActivity.masterinfo.getId();
//        master_id = getIntent().getExtras().getInt("master_id");

		toolbar = findViewById(R.id.tb_rs);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeAsUpIndicator(R.drawable.btn_header_back);// 툴바 왼쪽 버튼 이미지

		iv_image1 = findViewById(R.id.iv_rs_image);
		iv_image1.setOnClickListener(this);
		iv_image2 = findViewById(R.id.iv_rs_image2);

		et_name = findViewById(R.id.et_rs_name);
		et_phone = findViewById(R.id.et_rs_phone);

		et_address = findViewById(R.id.et_rs_address);
		et_address.setOnClickListener(this);
		et_address_detail = findViewById(R.id.et_rs_address_detail);

		iv_address = findViewById(R.id.iv_rs_address);
		iv_address.setOnClickListener(this);

		et_explain = findViewById(R.id.et_rs_explain);

		btn_complete = findViewById(R.id.btn_rs_complete);
		btn_complete.setOnClickListener(this);

		store_id = getIntent().getIntExtra("store_id", -1);
		if (store_id != -1) {
			setTextview(store_id);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.iv_rs_image:
				setStoreImage();
				break;
			case R.id.iv_rs_image2:
				setStoreImage();
				break;
			case R.id.et_rs_address:
				setAddress();
				break;
			case R.id.iv_rs_address:
				setAddress();
				break;
			case R.id.btn_rs_complete:
				if (store_id != -1)
					updateStoreInfo();
				else
					insertStoreInfo();
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == ALBUMCODE) {
			if (resultCode == RESULT_OK) {
				try {
					InputStream inputStream = getContentResolver().openInputStream(data.getData());
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					inputStream.close();

					iv_image1.setImageBitmap(bitmap);
				} catch (Exception e) {
					e.printStackTrace();
					Log.i(TAG, "error : " + e.toString());
				}
			} else {
				Log.i(TAG, "error : ");
			}
		} else if (requestCode == ADDRESS_CODE) {
			if (resultCode == RESULT_OK) {
				String number = data.getExtras().getString("address1");
				String address = data.getExtras().getString("address2") + " ";
				address += data.getExtras().getString("address3");

				if (number == null || address == null) {
					Log.i(TAG, "result : " + address);
				} else {
					et_address.setText(number + " " + address);
					et_address_detail.setEnabled(true);
				}
			} else {
				Log.i(TAG, "error : ");
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case PERMISSIONS_REQUEST:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Intent intent = new Intent(Intent.ACTION_PICK);
					intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
					startActivityForResult(intent, BaseActivity.ALBUMCODE);
				} else {
					Toast.makeText(this, getString(R.string.permission_msg), Toast.LENGTH_LONG).show();
				}
				return;
		}
	}

	private void setStoreImage() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
				Toast.makeText(this, getString(R.string.permission_msg), Toast.LENGTH_LONG).show();
				ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_REQUEST);

			} else {
				ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_REQUEST);
			}
		} else {
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
			startActivityForResult(intent, ALBUMCODE);
		}
	}

	private void setAddress() {
		Intent i = new Intent(RegisterStoreActivity.this, AddressActivity.class);
		startActivityForResult(i, ADDRESS_CODE);
	}

	private void insertStoreInfo() {
		Map<String, String> postdata = makeMapInfo();
		if (postdata.get(ADDRESS).contains(getString(R.string.defend_word))) {
			return;
		}

		if (!networkConnect()) {
			Toast.makeText(this, getString(R.string.check_network), Toast.LENGTH_SHORT).show();
			return;
		}

		Call<String> call = getDbService().insertstoreinfo(postdata);
		call.enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				if (response.message().equals(getString(R.string.NetworkCheck))) {
					Log.i(TAG, "insertStoreInfo result : " + response.body());
					finish();
				} else {
					Log.i(TAG, "Fail : " + response.message());
				}
			}

			@Override
			public void onFailure(Call<String> call, Throwable t) {
				Log.i(TAG, "Error : " + t.getMessage());
			}
		});
	}

	private void updateStoreInfo() {
		Map<String, String> postdata = makeMapInfo();
		if (postdata.get(ADDRESS).contains(getString(R.string.defend_word))) {
			return;
		}

		if (!networkConnect()) {
			Toast.makeText(this, getString(R.string.check_network), Toast.LENGTH_SHORT).show();
			return;
		}

		Call<String> call = getDbService().updatestoreinfo(postdata);
		call.enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				if (response.message().equals(getString(R.string.NetworkCheck))) {
					Log.i(TAG, "updateStoreInfo result : " + response.body());
					finish();
				} else {
					Log.i(TAG, "updateStoreInfo Error : " + response.message());
				}
			}

			@Override
			public void onFailure(Call<String> call, Throwable t) {
				Log.i(TAG, "updateStoreInfo Fail : " + t.getMessage());
			}
		});
	}

	private Map<String, String> makeMapInfo() {
		HashMap<String, String> map = new HashMap<>();
		if(store_id == -1)
			map.put(MASTER_ID, Integer.toString(master_id));
		else
			map.put(getString(R.string.store_id), Integer.toString(store_id));

		map.put(NAME, et_name.getText().toString());
		map.put(PHONE, et_phone.getText().toString());
		map.put(ADDRESS, et_address.getText().toString());
		map.put("address_detail", et_address_detail.getText().toString());
		map.put(IMAGE, BitmapToString(resize(((BitmapDrawable) iv_image1.getDrawable()).getBitmap()), TAG));
		map.put("detail", et_explain.getText().toString());

		return map;
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

	private void setTextview(final int store_id) {
		if (!networkConnect()) {
			Toast.makeText(this, getString(R.string.check_network), Toast.LENGTH_SHORT).show();
			return;
		}

		Call<StoreInfo> call = getDbService().getstoreinfobystoreid(store_id);
		call.enqueue(new Callback<StoreInfo>() {
			@Override
			public void onResponse(Call<StoreInfo> call, Response<StoreInfo> response) {
				if (response.message().equals(getString(R.string.NetworkCheck))) {
					storeinfo = response.body().getStorelist().get(0);
					et_name.setText(storeinfo.getName());
					et_address.setText(storeinfo.getAddress());
					et_address_detail.setText(storeinfo.getAddress_detail());
					et_explain.setText(storeinfo.getDetail());
					et_phone.setText(storeinfo.getPhone());
					if (storeinfo.getImage() != null)
						iv_image1.setImageBitmap(StringtoBitmap(storeinfo.getImage(), TAG));
				} else {
					Log.i(TAG, "getstoreinfobystoreid error : " + response.message());
				}
			}

			@Override
			public void onFailure(Call<StoreInfo> call, Throwable t) {
				Log.i(TAG, "getstoreinfobystoreid error : " + t.getMessage());
			}
		});
	}
}