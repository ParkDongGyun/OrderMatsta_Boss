package com.sbsj.ordermatstaboss.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sbsj.ordermatstaboss.DB.API_Client;
import com.sbsj.ordermatstaboss.DB.DB_Service;
import com.sbsj.ordermatstaboss.DB.GoodsInfoColumn;
import com.sbsj.ordermatstaboss.DB.MasterDeviceInfoColumn;
import com.sbsj.ordermatstaboss.DB.MasterInfoColumn;
import com.sbsj.ordermatstaboss.DB.MenuCateInfoColumn;
import com.sbsj.ordermatstaboss.DB.StoreInfoColumn;
import com.sbsj.ordermatstaboss.Login.GlobalApplication;
import com.sbsj.ordermatstaboss.R;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public abstract class BaseActivity extends AppCompatActivity {

    public static final int PERMISSION_CODE_ALBUM = 1;
    public static final int PERMISSION_CODE_CAMERA = 2;
    public static final int PERMISSION_CODE_PHONE = 3;
    public static final int PERMISSION_CODE_WHITE = 3;
    public static final int ALBUMCODE = 4;
    public static final int ADDRESS_CODE = 5;
    public static final int ADD_CODE = 6;
    //임시데이터
    private final String tempDateName = "profile";
    public static String b_directory = "";
    public static String b_sns_id = "";
    private final String ISREGISTERED = "isregistered";
    protected Boolean b_regi_masterinfo = false;

    private final String GoodsItem = "goodsitem";

    private DB_Service dbService;

    public static MasterInfoColumn masterinfo;
    public static ArrayList<StoreInfoColumn> storeinfolist;
    public static ArrayList<MenuCateInfoColumn> menucateinfolist;
    public static ArrayList<GoodsInfoColumn> goodsinfolist;

    public static ArrayList<MasterDeviceInfoColumn> masterdevicelist = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbService = API_Client.getClient().create(DB_Service.class);
        loadShared();
    }

    public String getISREGISTERED() {
        return ISREGISTERED;
    }

    public String getGoodsItem() {
        return GoodsItem;
    }

    public DB_Service getDbService() {
        return dbService;
    }


    public void saveTempLogin(String directory, String sns_id) {
        SharedPreferences tempDate = getSharedPreferences(tempDateName, MODE_PRIVATE);
        SharedPreferences.Editor tempEdit = tempDate.edit();
        tempEdit.putString(getString(R.string.directory), directory);
        tempEdit.putString(getString(R.string.sns_id), sns_id);
        tempEdit.apply();
    }

    public void saveTempRegistered(Boolean registered) {
        SharedPreferences tempDate = getSharedPreferences(tempDateName, MODE_PRIVATE);
        SharedPreferences.Editor tempEdit = tempDate.edit();
        tempEdit.putBoolean(ISREGISTERED, registered);
        tempEdit.apply();
    }

    /*쉐어드값 불러오기*/
    public void loadShared() {
        SharedPreferences pref = getSharedPreferences(tempDateName, MODE_PRIVATE);
        b_directory = pref.getString(getString(R.string.directory), "");
        b_sns_id = pref.getString(getString(R.string.sns_id), "");
        b_regi_masterinfo = pref.getBoolean(ISREGISTERED, false);
    }


    //사진 사이즈 조절
    protected Bitmap resize(Bitmap bm) {
        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 800)
            bm = Bitmap.createScaledBitmap(bm, 400, 240, true);
        else if (config.smallestScreenWidthDp >= 600)
            bm = Bitmap.createScaledBitmap(bm, 300, 180, true);
        else if (config.smallestScreenWidthDp >= 400)
            bm = Bitmap.createScaledBitmap(bm, 200, 120, true);
        else if (config.smallestScreenWidthDp >= 360)
            bm = Bitmap.createScaledBitmap(bm, 180, 108, true);
        else
            bm = Bitmap.createScaledBitmap(bm, 160, 96, true);
        return bm;
    }

    public String BitmapToString(Bitmap bitmap, String TAG) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] arr = baos.toByteArray();
        String image = Base64.encodeToString(arr, Base64.DEFAULT);
        String temp = "";
        try {
            temp = "&imagedevice=" + URLEncoder.encode(image, "utf-8");
        } catch (Exception e) {
            Log.i(TAG, "Error BitmapToString: "+ e.getMessage());
        }

        //return temp;
        return image;
    }

    public Bitmap StringtoBitmap(String image, String TAG) {
        try {
            byte[] encodeByte = Base64.decode(image, Base64.NO_WRAP);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            Log.i(TAG, "Error StringtoBitmap: "+ e.getMessage());
            return null;
        }
    }

    public static String printPrice(String priceForm, int price) {
        DecimalFormat myFormatter = new DecimalFormat("###,###");
        return String.format(priceForm, myFormatter.format(price));
    }

    protected void quitActivity(String title, String msg) {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
    }
    public boolean networkConnect() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ninfo = cm.getActiveNetworkInfo();

        if(ninfo == null){
            Toast.makeText(this, "네트워크에 연결 할 수 없습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    public void progressON() {
        GlobalApplication.getInstance().progressON(this, null);
    }

    public void progressON(String message) {
        GlobalApplication.getInstance().progressON(this, message);
    }

    public void progressOFF() {
        GlobalApplication.getInstance().progressOFF();
    }
}
