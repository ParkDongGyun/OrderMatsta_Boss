package com.sbsj.ordermatstaboss.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sbsj.ordermatstaboss.Adapter.EditCategoryAdapter;
import com.sbsj.ordermatstaboss.Util.ItemTouchHelperCallback;
import com.sbsj.ordermatstaboss.Util.OnItemClickListener;
import com.sbsj.ordermatstaboss.DB.MenuCateInfo;
import com.sbsj.ordermatstaboss.DB.MenuCateInfoColumn;
import com.sbsj.ordermatstaboss.Util.OnStartDargListener;
import com.sbsj.ordermatstaboss.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//public class EditCategoryActivity extends BaseActivity implements View.OnClickListener, OnStartDargListener {
public class EditCategoryActivity extends BaseActivity  {

//    private final String TAG = "EditCategory_Activity";
//    private int STORE_ID = -1;
//
//    private Toolbar toolbar;
//    private RecyclerView recyclerView;
//    private Button btn_save;
//    private Button btn_cancel;
//
//    EditCategoryAdapter adapter;
//
//    private int originalCount = 0;
//    ItemTouchHelper itemTouchHelper;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_category);
//
//        findviewid();
//        STORE_ID = BaseActivity.storeinfolist.get(0).getId();
//
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
//
//        ArrayList<MenuCateInfoColumn> arrayList = new ArrayList<>();
//        adapter = new EditCategoryAdapter(this, arrayList, new OnItemClickListener() {
//            @Override
//            public void onItemClick(View v, int position) {
//
//            }
//        }, this);
//
//        if (!networkConnect()) {
//            Toast.makeText(this, getString(R.string.check_network), Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Call<MenuCateInfo> call = getDbService().getCategoryInfo(STORE_ID);
//        call.enqueue(new Callback<MenuCateInfo>() {
//            @Override
//            public void onResponse(Call<MenuCateInfo> call, Response<MenuCateInfo> response) {
//                if (response.message().equals(getString(R.string.NetworkCheck))) {
//                    adapter.setDatalist(response.body().getMenuCatelist());
//                    adapter.notifyDataSetChanged();
//
//                    originalCount = adapter.getItemCount();
//                } else {
//                    Log.i(TAG, "Error : " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MenuCateInfo> call, Throwable t) {
//                Log.i(TAG, "Fail : " + t.getMessage());
//            }
//        });
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(adapter);
//        itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);
//
//        recyclerView.setAdapter(adapter);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("뒤로 가기").setMessage("정보가 저장되지 않습니다.")
//                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                EditCategoryActivity.super.onBackPressed();
//                            }
//                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                }).create().show();
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onBackPressed() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("뒤로 가기").setMessage("정보가 저장되지 않습니다.")
//                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        EditCategoryActivity.super.onBackPressed();
//                    }
//                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        }).create().show();
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btn_ec_save:
//                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("저장").setMessage("정말로 저장하시겠습니까?")
//                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                saveCategory();
//                            }
//                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                }).create().show();
//                break;
//            case R.id.btn_ec_add:
//                addCategory();
//                break;
//        }
//    }
//
//    @Override
//    public void onStartDrag(RecyclerView.ViewHolder holder) {
//        itemTouchHelper.startDrag(holder);
//    }
//
//    private void findviewid() {
//        toolbar = findViewById(R.id.tb_ec);
//        recyclerView = findViewById(R.id.rcv_ec);
//        btn_save = findViewById(R.id.btn_ec_save);
//        btn_save.setOnClickListener(this);
//        btn_cancel = findViewById(R.id.btn_ec_add);
//        btn_cancel.setOnClickListener(this);
//    }
//
//    private void addCategory() {
//        MenuCateInfoColumn cateInfoColumn = new MenuCateInfoColumn();
//        adapter.getDatalist().add(cateInfoColumn);
//        adapter.notifyDataSetChanged();
//    }
//
//    private void saveCategory() {
//        ArrayList<Integer> update_idlist = new ArrayList<>();
//        ArrayList<String> update_namelist = new ArrayList<>();
//
//        for (int i = 0; i < originalCount; ++i) {
//            update_idlist.add(adapter.getDatalist().get(i).getId());
//            update_namelist.add(adapter.getDatalist().get(i).getName());
//
//        }
//
//        if (!networkConnect()) {
//            Toast.makeText(this, getString(R.string.check_network), Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Call<String> call = getDbService().updateCategoryInfo(update_idlist, update_namelist);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                Log.i(TAG, "message : " + response.message());
//                Log.i(TAG, "body : " + response.body());
//
//                if (originalCount == adapter.getItemCount()) {
//                    finish();
//                    return;
//                }
//                insert_category(adapter.getItemCount());
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.i(TAG, "Fail : " + t.getMessage());
//            }
//        });
//    }
//
//    void insert_category(int count) {
//        ArrayList<Integer> insert_sequencelist = new ArrayList<>();
//        ArrayList<String> insert_namelist = new ArrayList<>();
//
//        for (int i = originalCount; i < count; ++i) {
//            insert_namelist.add(adapter.getDatalist().get(i).getName());
//            insert_sequencelist.add(originalCount + 1);
//        }
//
//        if (!networkConnect()) {
//            Toast.makeText(this, getString(R.string.check_network), Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Call<String> call_insert = getDbService().insertCategoryInfo(STORE_ID, insert_namelist, originalCount + 1);
//        call_insert.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                Log.i(TAG, "message : " + response.message());
//                Log.i(TAG, "body : " + response.body());
//                originalCount = adapter.getItemCount();
//                finish();
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.i(TAG, "Fail : " + t.getMessage());
//            }
//        });
//    }
}