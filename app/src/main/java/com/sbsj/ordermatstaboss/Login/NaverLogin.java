package com.sbsj.ordermatstaboss.Login;

import android.content.Context;
import android.util.Log;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import com.sbsj.ordermatstaboss.Activity.BaseActivity;
import com.sbsj.ordermatstaboss.Activity.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class NaverLogin {

    private final String DIRECTORY = "Naver";
    // ->네이버
    private static String OAUTH_CLIENT_ID = "dJzcLVfGsNBRxiAfyoqV";
    private static String OAUTH_CLIENT_SECRET = "FUBq8ZIzEg";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

    //private OAuthLoginButton mOAuthLoginButton;
    private static OAuthLogin mOAuthLoginInstance;
    String id;
    Context mContext;

    //생성자
    public NaverLogin(Context context, OAuthLoginButton mOAuthLoginButton) {
        // ->네이버 초기화
        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.showDevelopersLog(true);
        mOAuthLoginInstance.init(context, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);
        // ->네이버 로그인 버튼 셋팅
        /**/
        //mOAuthLoginButton = findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
        this.mContext = context;
    }


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }*/


    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                Thread thread = new Thread() {
                    public void run() {
                        String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
                        String data = mOAuthLoginInstance.requestApi(mContext, accessToken, "https://openapi.naver.com/v1/nid/me");
                        try {
                            JSONObject result = new JSONObject(data);
                            id = result.getJSONObject("response").getString("id");
                            Log.i("error",id);
                        } catch (JSONException e) {
                            Log.i("error", "error : "+e.toString());
                        }
                    }
                };
                thread.start();
                try {
                    thread.join();
                    ((BaseActivity) mContext).saveTempLogin(DIRECTORY, id);
                    ((LoginActivity)mContext).checkLogin(DIRECTORY, id);
                } catch (InterruptedException e) {
                    Log.i("error", "error : "+e.toString());
                }
            }
        }

    };
}
