package com.sbsj.ordermatstaboss.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.sbsj.ordermatstaboss.R;

public class AddressActivity extends AppCompatActivity {

	private final String TAG = "Address_Activity";
	private WebView webView;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		init_webview();

		handler = new Handler();
	}

	public void init_webview() {
		webView = findViewById(R.id.webview_id);
		webView.setWebChromeClient(new WebChromeClient());
		webView.getSettings().setJavaScriptEnabled(true);

		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.addJavascriptInterface(new AndroidBridge(), "OrderMatsta");

		webView.getSettings().setDatabaseEnabled(false);
		webView.getSettings().setAllowFileAccess(false);
		webView.getSettings().setDomStorageEnabled(false);
		webView.getSettings().setAppCacheEnabled(false);

		webView.setWebChromeClient(new WebChromeClient());
//		webView.loadUrl("https://www.naver.com");

		webView.loadUrl("https://dongkyun5654.cafe24.com/OrderMatsta_Boss/getAddress.php");
		Log.i(TAG,"enter here");
	}
	private class AndroidBridge extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			Log.i(TAG,"URL : "+url);
			return true;
		}
		@JavascriptInterface
		public void setAddress(final String arg1, final String arg2, final String arg3) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					Log.i(TAG,"1 : " + arg1+" / 2 : " + arg2+" / 3 : " + arg3);

					// WebView를 초기화 하지않으면 재사용할 수 없음
					init_webview();

					Intent intent = new Intent();
					intent.putExtra("address1",arg1);
					intent.putExtra("address2", arg2);
					intent.putExtra("address3", arg3);
					setResult(RESULT_OK, intent);
					finish();
				}
			});
		}
	}
}
