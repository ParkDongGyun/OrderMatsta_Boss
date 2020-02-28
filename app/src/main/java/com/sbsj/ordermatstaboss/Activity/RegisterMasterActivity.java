package com.sbsj.ordermatstaboss.Activity;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sbsj.ordermatstaboss.Adapter.AgreeAdapter;
import com.sbsj.ordermatstaboss.R;
import com.sbsj.ordermatstaboss.Util.OnClickButtonListener;
import com.sbsj.ordermatstaboss.Util.OnItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterMasterActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "RegisterMem_Activity";

    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    Context context;

    private final String DIRECTORY = "directory";
    private final String SNS_ID = "sns_id";
    private final String NAME = "name";
    private final String PHONE = "phone";
    private final String GENDER = "gender";
    private final String BIRTHDAY = "birthday";

    private ImageView iv_profile;
    private ImageView iv_profile2;
    private EditText et_name;
    private EditText et_phone;
    private RadioGroup radioGroup;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private String curGender = "Male";
    /* TextView tv_birth;*/
    private ImageView iv_birth;
    private EditText et_birth;

    private LinearLayout ll_agree_all;
    private CheckBox cb_agree_all;
    private TextView tv_agree_all;

    private RecyclerView recyclerView;
    private AgreeAdapter agreeAdapter;

    private Button btn_complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_master);

        context = this;

        iv_profile = findViewById(R.id.iv_rm_profile1);
        iv_profile.setOnClickListener(this);
        iv_profile2 = findViewById(R.id.iv_rm_profile2);
        iv_profile2.setOnClickListener(this);

        et_name = findViewById(R.id.et_rm_name);
        et_name.setOnClickListener(this);

        et_phone = findViewById(R.id.et_rm_phone);
        et_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        radioGroup = findViewById(R.id.rg_rm_gender);
        rb_male = findViewById(R.id.rbtn_male);
        rb_female = findViewById(R.id.rbtn_female);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rbtn_male) {
                    curGender = "Male";
                    rb_male.setTextColor(getResources().getColor(R.color.ordermaster_color));
                    rb_female.setTextColor(getResources().getColor(R.color.half_tm_color));
                } else {
                    curGender = "Female";
                    rb_female.setTextColor(getResources().getColor(R.color.ordermaster_color));
                    rb_male.setTextColor(getResources().getColor(R.color.half_tm_color));
                }
            }
        });

        et_birth = findViewById(R.id.et_rm_birth);
        iv_birth = findViewById(R.id.iv_rm_birth);
        et_birth.setOnClickListener(this);
        iv_birth.setOnClickListener(this);

        ll_agree_all = findViewById(R.id.ll_agree_all);
        ll_agree_all.setOnClickListener(this);
        cb_agree_all = findViewById(R.id.cb_agree_all);
        cb_agree_all.setOnClickListener(this);
        tv_agree_all = findViewById(R.id.tv_agree_all);
        tv_agree_all.setOnClickListener(this);

        recyclerView = findViewById(R.id.rcv_agree);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        ArrayList<String> agreelist = new ArrayList<>();
        for (int i = 0; i < getResources().getStringArray(R.array.agreetitle_arr).length; ++i) {
            agreelist.add(getResources().getStringArray(R.array.agreetitle_arr)[i]);
        }
        agreeAdapter = new AgreeAdapter(this, agreelist, new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                switch (v.getId()) {
                    case R.id.cb_agree:
                        agreeitem(position);
                        break;
                    case R.id.tv_agree_detail:
                        agreeitem(position);
                        break;
                    default:
                        Toast.makeText(RegisterMasterActivity.this, getString(R.string.prepare_service), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        recyclerView.setAdapter(agreeAdapter);

        btn_complete = findViewById(R.id.btn_mastercomplete);
        btn_complete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_rm_birth:
                setBirthday();
                break;
            case R.id.iv_rm_birth:
                setBirthday();
                break;
            case R.id.btn_mastercomplete:
                if (isEmptyView())
                    updateDB();
                break;
            case R.id.iv_rm_profile1:
                setprofileImage();
                break;
            case R.id.iv_rm_profile2:
                setprofileImage();
                break;
            case R.id.ll_agree_all:
                agreeAll();
                break;
            case R.id.cb_agree_all:
                agreeAll();
                break;
            case R.id.tv_agree_all:
                agreeAll();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE_ALBUM:
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ALBUMCODE) {
            Uri photoUri = data.getData();
            Cursor cursor = null;

            try {
                String[] proj = {MediaStore.Images.Media.DATA};
                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);
                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap originalBm = BitmapFactory.decodeFile(new File(cursor.getString(column_index)).getAbsolutePath(), options);
                iv_profile.setImageBitmap(originalBm);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }

    private void setprofileImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, getString(R.string.permission_msg), Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_CODE_ALBUM);

            } else {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_CODE_ALBUM);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent, BaseActivity.ALBUMCODE);
        }
    }

    private void setBirthday() {
        Calendar c = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1 += 1;
                et_birth.setText(i + "." + i1 + "." + i2);
                /*  makeUnderLine(et_birth);*/
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        datePickerDialog.show();
    }

    private void updateDB() {
        if (!networkConnect()) {
            Toast.makeText(this, getString(R.string.check_network), Toast.LENGTH_SHORT).show();
            return;
        }

        Call<String> call = getDbService().updateMaster(makeMapInfo());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                switch (response.body()) {
                    case "사용자 정보 업데이트 성공":
                        saveTempRegistered(true);
                        redirectStorelistActivity();
                        break;
                    default:
                        Log.i(TAG, "업데이트 실패 : " + response.message());
                        Log.i(TAG, "업데이트 실패 : " + response.body());
                        redirectRegisterMaster();
                        break;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "updateDB Error : " + t.getMessage());
            }
        });
    }

    private Map<String, String> makeMapInfo() {
        HashMap<String, String> map = new HashMap<>();
        map.put(DIRECTORY, getString(R.string.directory));
        map.put(SNS_ID, getString(R.string.sns_id));
        map.put(NAME, et_name.getText().toString());
        map.put(PHONE, et_phone.getText().toString());
        map.put(GENDER, curGender);
        map.put(BIRTHDAY, et_birth.getText().toString());

        return map;
    }

    private void redirectRegisterMaster() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    public void redirectStorelistActivity() {
        startActivity(new Intent(this, StoreActivity.class));
        finish();
    }

    private void agreeAll() {
        boolean tempbool = agreeAdapter.checkAgree.get(0);
        for (int i = 0; i < agreeAdapter.getDatalist().size(); ++i) {
            if (tempbool != agreeAdapter.checkAgree.get(i)) {
                for (int j = 0; j < agreeAdapter.checkAgree.size(); ++j) {
                    agreeAdapter.checkAgree.set(j, true);
                }
                break;
            }

            if (i == agreeAdapter.checkAgree.size() - 1)
                for (int j = 0; j < agreeAdapter.checkAgree.size(); ++j) {
                    agreeAdapter.checkAgree.set(j, !tempbool);
                }
        }
        agreeAdapter.notifyDataSetChanged();
    }

    private void agreeitem(int position) {
        agreeAdapter.checkAgree.set(position, !agreeAdapter.checkAgree.get(position));
        agreeAdapter.notifyDataSetChanged();

        for (int i = 0; i < agreeAdapter.checkAgree.size(); ++i) {
            if (!agreeAdapter.checkAgree.get(i)) {
                cb_agree_all.setChecked(false);
                break;
            }

            if (i == agreeAdapter.checkAgree.size() - 1)
                cb_agree_all.setChecked(true);
        }
    }

    private boolean isEmptyView() {
        if (et_name.getText().toString().isEmpty()) {
            Toast.makeText(context, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            et_name.post(new Runnable() {
                @Override
                public void run() {
                    et_name.setFocusableInTouchMode(true);
                    et_name.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(et_name, 0);
                }
            });
            return false;
        } else if (et_phone.getText().toString().isEmpty()) {
            Toast.makeText(context, "휴대폰번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            et_phone.post(new Runnable() {
                @Override
                public void run() {
                    et_phone.setFocusableInTouchMode(true);
                    et_phone.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(et_phone, 0);
                }
            });
            return false;
        } else if (et_birth.getText().toString().isEmpty()) {
            Toast.makeText(context, "생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            et_birth.post(new Runnable() {
                @Override
                public void run() {
                    setBirthday();
                }
            });
            return false;
        } else if (!(rb_male.isChecked() || rb_female.isChecked())) {
            Toast.makeText(context, "성별을 입력해주세요.", Toast.LENGTH_SHORT).show();
            radioGroup.post(new Runnable() {
                @Override
                public void run() {
                    radioGroup.requestFocus();
                }
            });
            return false;
        } else if (!cb_agree_all.isChecked()) {
            Toast.makeText(context, "약관 동의를 확인해주세요.", Toast.LENGTH_SHORT).show();
            cb_agree_all.post(new Runnable() {
                @Override
                public void run() {
                    cb_agree_all.requestFocus();
                }
            });
            return false;
        } else return true;
    }
}
