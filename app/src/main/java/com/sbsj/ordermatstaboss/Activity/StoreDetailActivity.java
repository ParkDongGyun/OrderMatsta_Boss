package com.sbsj.ordermatstaboss.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sbsj.ordermatstaboss.DB.OrderDetailInfoColumn;
import com.sbsj.ordermatstaboss.Util.OnItemClickListener;
import com.sbsj.ordermatstaboss.Adapter.StoreDetailAdapter;
import com.sbsj.ordermatstaboss.DB.GoodsInfo;
import com.sbsj.ordermatstaboss.DB.GoodsInfoColumn;
import com.sbsj.ordermatstaboss.DB.MenuCateInfo;
import com.sbsj.ordermatstaboss.DB.MenuCateInfoColumn;
import com.sbsj.ordermatstaboss.DB.StoreInfo;
import com.sbsj.ordermatstaboss.DB.StoreInfoColumn;
import com.sbsj.ordermatstaboss.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreDetailActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private final String TAG = "StoreDetail_Activity";
    private final int RequestCode_RegisterGoods = 10;
    private final int RequestCode_UpdateGoods = 15;
    private final int RequestCode_UpdateStore = 20;

    private String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private int STORE_ID = -1;

    private Toolbar toolbar;

    private ImageView iv_image;
    private ImageView iv_prepare;
    private TextView tv_name;
    private TextView tv_address;
    private TextView tv_explain;

    private RelativeLayout rl_open;
    private Switch sw_open;

    private RelativeLayout rl_qr;
    private TextView tv_qr;
    private ImageView iv_qr;

    private RelativeLayout rl_review;
    private TextView tv_review;
    private ImageView iv_review;

    private RecyclerView recyclerView;
    private TextView tv_nogoods;

    private Button btn_addmenu;

    private StoreDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        init_layout();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.btn_header_back);// 툴바 왼쪽 버튼 이미지
        actionBar.setDisplayShowTitleEnabled(true);

        STORE_ID = getIntent().getIntExtra(getString(R.string.store_id), STORE_ID);

        Log.i(TAG, getString(R.string.store_id) + " : " + STORE_ID);

        ArrayList<GoodsInfoColumn> arrayList = new ArrayList<>();
        adapter = new StoreDetailAdapter(this, arrayList, new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), RegisterGoodsActivity.class);
                intent.putExtra(getString(R.string.store_id), STORE_ID);
                intent.putExtra(getString(R.string.goods_id), adapter.getDatalist().get(position).getId());
                startActivityForResult(intent, RequestCode_UpdateGoods);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        getStoreInfo(STORE_ID);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_sd_qr:
                generateQRCode(STORE_ID, tv_address.getText().toString());
                break;
            case R.id.tv_sd_qr:
                generateQRCode(STORE_ID, tv_address.getText().toString());
                break;
            case R.id.iv_sd_qr:
                generateQRCode(STORE_ID, tv_address.getText().toString());
                break;

            case R.id.tv_sd_review:
                Toast.makeText(this, "준비중입니다.", Toast.LENGTH_LONG).show();
                break;

            case R.id.btn_add_menu:
                Intent intent_Goods = new Intent(getApplicationContext(), RegisterGoodsActivity.class);
                intent_Goods.putExtra(getString(R.string.store_id), STORE_ID);
                startActivityForResult(intent_Goods, RequestCode_RegisterGoods);
                break;

            case R.id.rl_sd_open:
                sw_open.setChecked(!sw_open.isChecked());
//				checkedswitch(sw_open.isChecked());
                break;
            default:
                Log.i(TAG, "default");
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        iv_prepare.setVisibility(b ? View.INVISIBLE : View.VISIBLE);
        checkedswitch(b);
        Log.i(TAG, "checked : " + Boolean.toString(b));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RequestCode_UpdateStore:
                getStoreInfo(STORE_ID);
                break;
            case RequestCode_RegisterGoods:
                adapter.getDatalist().clear();
                getGoodsInfo(STORE_ID);
                break;
            case RequestCode_UpdateGoods:
                adapter.getDatalist().clear();
                getGoodsInfo(STORE_ID);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.storedetail_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_editstore:
                Intent intent = new Intent(this, RegisterStoreActivity.class);
                intent.putExtra("store_id", STORE_ID);
                startActivityForResult(intent, RequestCode_UpdateStore);
                break;
            case android.R.id.home:
                finish();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getGoodsInfo(STORE_ID);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_CODE_WHITE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    generateQRCode(STORE_ID, tv_address.getText().toString());
                } else {
                    Toast.makeText(this, getString(R.string.permission_msg), Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    private void init_layout() {
        toolbar = findViewById(R.id.tb_storedetail);

        iv_image = findViewById(R.id.iv_sd_image);
        iv_prepare = findViewById(R.id.iv_sd_prepare);
        tv_name = findViewById(R.id.tv_sd_name);
        tv_address = findViewById(R.id.tv_sd_address);
        tv_explain = findViewById(R.id.tv_sd_explain);

        rl_open = findViewById(R.id.rl_sd_open);
        rl_open.setOnClickListener(this);
        sw_open = findViewById(R.id.sw_sd_open);
        sw_open.setOnCheckedChangeListener(this);

        rl_qr = findViewById(R.id.rl_sd_qr);
        tv_qr = findViewById(R.id.tv_sd_qr);
        iv_qr = findViewById(R.id.iv_sd_qr);
        rl_qr.setOnClickListener(this);
        tv_qr.setOnClickListener(this);
        iv_qr.setOnClickListener(this);

        tv_review = findViewById(R.id.tv_sd_review);
        rl_review = findViewById(R.id.rl_sd_review);
        iv_review = findViewById(R.id.iv_sd_review);
        rl_review.setOnClickListener(this);
        tv_review.setOnClickListener(this);
        iv_review.setOnClickListener(this);

        recyclerView = findViewById(R.id.rcv_sd);
        tv_nogoods = findViewById(R.id.tv_sd_nogoods);

        btn_addmenu = findViewById(R.id.btn_add_menu);
        btn_addmenu.setOnClickListener(this);
    }

    private void getStoreInfo(int store_id) {
        if (!networkConnect()) {
            Toast.makeText(this, getString(R.string.check_network), Toast.LENGTH_SHORT).show();
            return;
        }

        Call<StoreInfo> call = getDbService().getstoreinfobystoreid(store_id);
        call.enqueue(new Callback<StoreInfo>() {
            @Override
            public void onResponse(Call<StoreInfo> call, Response<StoreInfo> response) {
                if (response.message().equals(getString(R.string.NetworkCheck))) {
                    if (response.body().getStorelist().get(0).getId() != -1) {
                        StoreInfoColumn storeInfoColumn = response.body().getStorelist().get(0);

                        tv_name.setText(storeInfoColumn.getName());
                        tv_address.setText(storeInfoColumn.getAddress() + " " + storeInfoColumn.getAddress_detail());
                        tv_explain.setText(storeInfoColumn.getDetail());
                        iv_image.setImageBitmap(StringtoBitmap(storeInfoColumn.getImage(), TAG));
                        boolean k = true;
                        if (storeInfoColumn.getOpen() == 0) {
                            k = false;
                        }
                        sw_open.setChecked(k);
                    }
                } else {
                    Log.i(TAG, "getstoreinfobystoreid_Error : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<StoreInfo> call, Throwable t) {
                Log.i(TAG, "getstoreinfobystoreid_Fail : " + t.getMessage());
            }
        });
    }

    private void getGoodsInfo(final int store_id) {
        Call<GoodsInfo> call = getDbService().getGoodsInfo(store_id);
        call.enqueue(new Callback<GoodsInfo>() {
            @Override
            public void onResponse(Call<GoodsInfo> call, Response<GoodsInfo> response) {
                if (response.message().equals(getString(R.string.NetworkCheck))) {
                    ArrayList<GoodsInfoColumn> list = response.body().getGoodsinfolist();
                    if (BaseActivity.goodsinfolist != null)
                        BaseActivity.goodsinfolist.clear();
                    BaseActivity.goodsinfolist = list;
                    updateAdapter(list);
                } else {
                    Log.i(TAG, "getGoodsInfo Error : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<GoodsInfo> call, Throwable t) {
                Log.i(TAG, "getGoodsInfo Fail : " + t.getMessage());
            }
        });
    }

    private void updateAdapter(ArrayList<GoodsInfoColumn> list) {
        if (list.get(0).getId() != -1) {
            adapter.getDatalist().clear();
            adapter.setDatalist(list);
            tv_nogoods.setVisibility(View.INVISIBLE);
        } else {
            adapter.getDatalist().clear();
            tv_nogoods.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    private void generateQRCode(int store_id, String address) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(StoreDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(StoreDetailActivity.this, getString(R.string.permission_msg), Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(StoreDetailActivity.this, PERMISSIONS, PERMISSION_CODE_WHITE);
            } else {
                ActivityCompat.requestPermissions(StoreDetailActivity.this, PERMISSIONS, PERMISSION_CODE_WHITE);
            }
        } else {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            String qrName = store_id + getString(R.string.defend_word) + address;
            try {
                String qrName2 = new String(qrName.getBytes("UTF-8"), "ISO-8859-1");
                Bitmap bitmap = BitMatrixtoBitmap(qrCodeWriter.encode(qrName2, BarcodeFormat.QR_CODE, 200, 200));
                SaveBitmapToFileCache(bitmap, qrName);
                Toast.makeText(this, "QR코드를 갤러리에서 확인해보세요.", Toast.LENGTH_SHORT).show();
            } catch (WriterException e) {
                e.printStackTrace();
                Log.e(TAG, "WriteException : " + e.toString());
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "UnsupportedEncodingException : " + e.toString());
                e.printStackTrace();
            }
        }
    }

    public static Bitmap BitMatrixtoBitmap(BitMatrix bitMatrix) {
        int height = bitMatrix.getHeight();
        int width = bitMatrix.getWidth();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
            }
        }
        return bitmap;
    }

    private void SaveBitmapToFileCache(Bitmap bitmap, String filename) {
        String exStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folderName = exStorage + "/DCIM/OrderMatstaBoss/";

        filename = filename + ".jpg";
        FileOutputStream out = null;

        File file_path = new File(folderName);
        try {
            if (!file_path.mkdir()) {
                file_path.mkdir();
            }
            //fileCacheItem.createNewFile();
            out = new FileOutputStream(folderName + filename);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "SaveBitmapToFileCache1 : " + e.toString());
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "SaveBitmapToFileCache2 : " + e.toString());
            }
        }
    }

    private void checkedswitch(final boolean isopen) {
        Log.i(TAG, "isopen : " + Boolean.toString(isopen));
        Log.i(TAG, "store_ID : " + STORE_ID);
        progressON("Loading...");
        Call<String> call = getDbService().updatestoreinfo_open(STORE_ID, isopen ? 1 : 0);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.message().equals(getString(R.string.NetworkCheck))) {
                    Log.i(TAG, "updatestoreinfo_open result : " + response.body());

                } else {
                    Log.i(TAG, "updatestoreinfo_open error : " + response.message());
                }
                progressOFF();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "updatestoreinfo_open fail : " + t.getMessage());
            }
        });
    }
}
