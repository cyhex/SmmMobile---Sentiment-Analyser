package com.cyhex.smmMobile;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;

public class SmmMobileAboutActivity extends Activity{
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.webview);
    	WebView mWebView = (WebView) findViewById(R.id.webview);
    	mWebView.setBackgroundColor(Color.rgb(32, 32, 32));

        mWebView.loadUrl("file:///android_asset/about.html");

    }
	
}