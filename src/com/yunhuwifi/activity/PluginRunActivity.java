package com.yunhuwifi.activity;

import com.yunhuwifi.activity.R;
import com.yunhuwifi.util.CustomDialog;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PluginRunActivity extends HeaderActivity {

	private Dialog loading;
	private WebView wvPlugin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_plugin_run);

		this.setHeaderText("应用");
		this.setLeftButtonVisible(true);

		this.wvPlugin = (WebView) findViewById(R.id.wvPlugin);
		this.wvPlugin.getSettings().setJavaScriptEnabled(true);

		loading = CustomDialog.createLoadingDialog(PluginRunActivity.this, 
				"");
		loading.show();
		this.wvPlugin.loadUrl("http://gmu.baidu.com/demo");

		this.wvPlugin.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);

			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if (loading.isShowing())
					loading.dismiss();
			}
		});

	}
}
