package android.bigplan.lego.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.bigplan.lego.R;
import android.bigplan.lego.util.Logger;;

/**
 * 加载网页Activity
 */
public class WebActivity extends BaseTActivity {

	public static final String WEB_KEY_TITLE = "title";
	public static final String WEB_KEY_URL = "url";
	private WebView mWebView;
	private String url;
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		
		initData();
		initView();
		loadData();
	}

	private void initData() {
		url = getIntent().getStringExtra(WEB_KEY_URL);
		title = getIntent().getStringExtra(WEB_KEY_TITLE);
	}

	protected String getToolBarTitle(){
		return  getIntent().getStringExtra(WEB_KEY_TITLE);
	}
	
	private void initView() {
		mWebView = (WebView) findViewById(R.id.wv_web);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void loadData() {
		mWebView.getSettings().setBuiltInZoomControls(true);//缩放
		mWebView.getSettings().setJavaScriptEnabled(true);//开启Javascript
		mWebView.getSettings().setSupportZoom(false);//双击缩放
		mWebView.getSettings().setDomStorageEnabled(true); 
		mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		mWebView.setWebChromeClient(new MWebChromeClient());
		mWebView.setWebViewClient(new WebViewClient());
		mWebView.loadUrl(url);
	}
	
	private class MWebChromeClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			Logger.i("FingerArt", "WebView加载进度: "+newProgress);
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			if(TextUtils.isEmpty(WebActivity.this.title)) {
				setToolBarTitle(title);
			}
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {// Back键 && 网页还能够返回
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

















