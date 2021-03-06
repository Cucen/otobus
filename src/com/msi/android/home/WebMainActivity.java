package com.msi.android.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebMainActivity extends Activity {

	final Activity activity = this;
	public String url = "";
	WebView webView;

	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.webview);
		webView = (WebView) findViewById(R.id.webView1);
		
		WebSettings settings = webView.getSettings();
		settings.setBuiltInZoomControls(true);
		settings.setUseWideViewPort(true);
		settings.setJavaScriptEnabled(true);
		settings.setSupportMultipleWindows(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setLoadsImagesAutomatically(true);
		settings.setLightTouchEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setLoadWithOverviewMode(true);
		

		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				activity.setTitle("Loading...");
				activity.setProgress(progress * 100);

				if (progress == 100)
					activity.setTitle(R.string.app_name);
			}
		});

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// Handle the error
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// ortalama 1024
				if (url.equals("http://www.google.com.tr/intl/tr/ads/")) {
					url = "http://www.hotmail.com";
				}
				view.loadUrl(url);
				return true;
			}
		});
		

		webView.loadUrl(url);
	}
	
	public void onButtonClick(final View view) {
		switch (view.getId()) {
		case R.id.backButtonWeb:
			finish();
			break;
		case R.id.homeButtonWeb:
			Intent homeIntent = new Intent(this, AndroidActivity.class);
			homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			finish();
			startActivity(homeIntent);
			break;
		}
	}
}