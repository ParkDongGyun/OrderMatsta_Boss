package com.sbsj.ordermatstaboss.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.sbsj.ordermatstaboss.DB.MasterDeviceInfo;
import com.sbsj.ordermatstaboss.DB.MasterDeviceInfoColumn;
import com.sbsj.ordermatstaboss.Fragment.HomeFragment;
import com.sbsj.ordermatstaboss.Fragment.MyinfoFragment;
import com.sbsj.ordermatstaboss.Fragment.OrderFragment;
import com.sbsj.ordermatstaboss.Fragment.SettingFragment;
import com.sbsj.ordermatstaboss.Util.OnItemClickListener;
import com.sbsj.ordermatstaboss.Adapter.StoreListAdapter;
import com.sbsj.ordermatstaboss.DB.MasterInfo;
import com.sbsj.ordermatstaboss.DB.StoreInfo;
import com.sbsj.ordermatstaboss.DB.StoreInfoColumn;
import com.sbsj.ordermatstaboss.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreActivity extends BaseActivity {
    String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE};

    private final String TAG = "Store_Activity";

    private Toolbar toolbar;
    private TextView tv_toolbar;
    private BottomNavigationView bottomNavigationView;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private HomeFragment homeFragment;
    private MenuItem itemHome;
    private OrderFragment orderFragment;
    private MenuItem itemOrder;
    private MyinfoFragment myinfoFragment;
    private SettingFragment settingFragment;

    private String device_id;
    private String fb_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        toolbar = findViewById(R.id.tb_store);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tv_toolbar = findViewById(R.id.tv_tb_store);

        fragmentManager = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        orderFragment = new OrderFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_store, homeFragment).commitAllowingStateLoss();

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "getInstanceId failed", task.getException());
                    fb_token = "fb_token Upload Error";

                    return;
                }
                // Get new Instance ID token
                fb_token = task.getResult().getToken();

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(StoreActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                        Toast.makeText(StoreActivity.this, getString(R.string.permission_msg), Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(StoreActivity.this, PERMISSIONS, PERMISSION_CODE_PHONE);
                    } else {
                        ActivityCompat.requestPermissions(StoreActivity.this, PERMISSIONS, PERMISSION_CODE_PHONE);
                    }
                } else {
                    device_id = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                }
            }
        });

        bottomNavigationView = findViewById(R.id.nv_store);
        Menu menu = bottomNavigationView.getMenu();
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.bn_home:
                        tv_toolbar.setText("점포 목록");
                        if (homeFragment == null) {
                            homeFragment = new HomeFragment();
                        }
                        itemHome.setVisible(true);
                        itemOrder.setVisible(false);
                        fragmentManager.beginTransaction().replace(R.id.fl_store, homeFragment).commit();
                        break;

                    case R.id.bn_order:
                        tv_toolbar.setText("주문 목록");
                        if (orderFragment == null) {
                            orderFragment = new OrderFragment();
                        }
                        itemHome.setVisible(false);
                        itemOrder.setVisible(true);
                        fragmentManager.beginTransaction().replace(R.id.fl_store, orderFragment).commit();
                        Toast.makeText(StoreActivity.this, getString(R.string.prepare_service), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.bn_myinfo:
                        tv_toolbar.setText("내 정보");
                        if (myinfoFragment == null) {
                            myinfoFragment = new MyinfoFragment();
                        }
                        itemHome.setVisible(false);
                        itemOrder.setVisible(false);
                        fragmentManager.beginTransaction().replace(R.id.fl_store, myinfoFragment).commit();
                        Toast.makeText(StoreActivity.this, "내정보", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.bn_setting:
                        tv_toolbar.setText("설정");
                        if (settingFragment == null) {
                            settingFragment = new SettingFragment();
                        }
                        itemHome.setVisible(false);
                        itemOrder.setVisible(false);
                        fragmentManager.beginTransaction().replace(R.id.fl_store, settingFragment).commit();
                        Toast.makeText(StoreActivity.this, "설정", Toast.LENGTH_SHORT).show();
                        break;

                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_CODE) {
            homeFragment.getMasterInfo();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tb_store, menu);
        itemHome = menu.findItem(R.id.menu_plus);
        itemHome.setVisible(true);
        itemOrder = menu.findItem(R.id.menu_refresh);
        itemOrder.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_plus:
                Intent intent = new Intent(StoreActivity.this, RegisterStoreActivity.class);
                intent.putExtra(getString(R.string.master_id), masterinfo.getId());
                startActivityForResult(intent, ADD_CODE);
                break;
            case R.id.menu_refresh:
                Toast.makeText(this, getString(R.string.prepare_service), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_CODE_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, getString(R.string.permission_msg), Toast.LENGTH_LONG).show();
                }
                return;
        }
    }


    public void getMasterDeviceInfo(final int master_id) {
        if (!networkConnect()) {
            Toast.makeText(this, getString(R.string.check_network), Toast.LENGTH_SHORT).show();
            return;
        }

        Call<MasterDeviceInfo> call = getDbService().getMasterDeviceInfo(master_id);
        call.enqueue(new Callback<MasterDeviceInfo>() {
            @Override
            public void onResponse(Call<MasterDeviceInfo> call, Response<MasterDeviceInfo> response) {
                if (response.message().equals(getString(R.string.NetworkCheck))) {
                    ArrayList<MasterDeviceInfoColumn> list = response.body().getMasterdevicelist();
                    if (list.get(0).getId() != -1 && device_id != null) {
                        for (int i = 0; i < list.size(); ++i) {
                            if (device_id.equals(list.get(i).getDevice_id())) {
                                return;
                            }
                        }
                    } else {
                        Log.i(TAG, "디바이스 정보 데이터가 없습니다.");
                        insertMasterDeviceInfo(masterinfo.getId(), device_id, fb_token);
                    }
                } else {
                    Log.i(TAG, "getMasterDeviceInfo Error : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MasterDeviceInfo> call, Throwable t) {
                Log.i(TAG, "getMasterDeviceInfo Fail : " + t.getMessage());
            }
        });
    }

    private void insertMasterDeviceInfo(final int master, String device_id, String fb_token) {
        if (device_id == null || fb_token == null)
            return;

        if (device_id.contains("Error") || fb_token.contains("Error"))
            return;

        Map<String, String> map = new HashMap<>();
        map.put("master_id", Integer.toString(master));
        map.put("device_id", device_id);
        map.put("fb_token", fb_token);

        if (!networkConnect()) {
            Toast.makeText(this, getString(R.string.check_network), Toast.LENGTH_SHORT).show();
            return;
        }

        Call<String> call = getDbService().insertMasterDeviceInfo(map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.message().equals(getString(R.string.NetworkCheck))) {
                    if (!response.body().contains("Error")) {
                        getMasterDeviceInfo(masterinfo.getId());
                        Log.i(TAG, "디바이스 정보 삽입 성공 : " + response.body());
                    } else
                        Log.i(TAG, "insertMasterDeviceInfo Error : " + response.body());
                } else {
                    Log.i(TAG, "insertMasterDeviceInfo Error : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "insertMasterDeviceInfo Fail : " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        quitActivity(getString(R.string.backAlert_quit_title), getString(R.string.backAlert_quit_msg));
    }
}
