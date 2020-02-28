package com.sbsj.ordermatstaboss.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.util.helper.Utility;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import com.sbsj.ordermatstaboss.Login.GoogleLogin;
import com.sbsj.ordermatstaboss.Login.KakaoLogin;
import com.sbsj.ordermatstaboss.Login.NaverLogin;
import com.sbsj.ordermatstaboss.R;
import com.sbsj.ordermatstaboss.Util.CustomBtnLogin;
import com.sbsj.ordermatstaboss.Util.OnClickButtonListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements OnClickButtonListener {

    private final int RC_GOOGLE_LOGIN = 10;
    private final int RC_KAKAO_LOGIN = 11;
    private final int RC_NAVER_LOGIN = 12;

    private final String TAG = "Login_Activity";

    GoogleLogin googleLogin;
    NaverLogin naverLogin;
    KakaoLogin kakaoLogin;

    CustomBtnLogin cbtn_google;
    CustomBtnLogin cbtn_naver;
    CustomBtnLogin cbtn_kakao;

    SignInButton btn_google;
    OAuthLoginButton btn_naver;
    LoginButton btn_kakao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_google = findViewById(R.id.btn_google);
        googleLogin = new GoogleLogin(this, btn_google);
        cbtn_google = findViewById(R.id.btn_google_custom);
        cbtn_google.setOnCustomClickListener(this);

        btn_naver = findViewById(R.id.btn_naver);
        naverLogin = new NaverLogin(this, btn_naver);
        cbtn_naver = findViewById(R.id.btn_naver_custom);
        cbtn_naver.setOnCustomClickListener(this);

        kakaoLogin = new KakaoLogin(this);
        btn_kakao = findViewById(R.id.btn_kakao);
        cbtn_kakao = findViewById(R.id.btn_kakao_custom);
        cbtn_kakao.setOnCustomClickListener(this);

        if (!b_directory.equals("") && !b_sns_id.equals("")) {
            if(b_regi_masterinfo)
                redirectStorelistActivity();
            else
                redirectRegisterActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //구글 로그인 시도
        if (requestCode == RC_GOOGLE_LOGIN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                googleLogin.FirebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.i(TAG, "구글 로그인 시도 실패 APIException : " + e.toString());
            }
        } else {
            Toast.makeText(this, "다시 시도해주세요.", Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        kakaoLogin.removeSessionCallback();
    }

    public void checkLogin(String dir, String sns_id) {
        if (!networkConnect()) {
            Toast.makeText(this, getString(R.string.check_network), Toast.LENGTH_SHORT).show();
            return;
        }

        Call<String> call = getDbService().insertMaster(dir, sns_id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i(TAG, "result : " + response.message());
                switch (response.body()) {
                    case "최초 로그인 성공":
                        redirectRegisterActivity();
                        break;
                    case "최초 로그인 실패":
                        redirectLoginActivity();
                        break;
                    case "기존 사용자 로그인 성공":
                        redirectStorelistActivity();
                        saveTempRegistered(true);
                        break;
                    default:
                        redirectLoginActivity();
                        break;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "Insert_memberinfo error : "+t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_google_custom:
                googleLogin.loginGoogle(RC_GOOGLE_LOGIN);
                break;
            case R.id.btn_naver_custom:
                btn_naver.performClick();
                break;
            case R.id.btn_kakao_custom:
                btn_kakao.performClick();
                break;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    //액티비티 전환 함수
    ///////////////////////////////////////////////////////////////////////////////////////
    public void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
    public void redirectRegisterActivity() {
        startActivity(new Intent(this, RegisterMasterActivity.class));
        finish();
    }
    public void redirectStorelistActivity() {
        startActivity(new Intent(this, StoreActivity.class));
        finish();
    }
}
