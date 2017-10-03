package com.example.skotyuk.pontajsv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

/**
 * Created by skotyuk on 10/3/2017.
 */

public class WebPageWindow extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_page_window);
        Intent intent = getIntent();
        WebView myWebView = (WebView) findViewById(R.id.webview);
        String address = intent.getStringExtra("webAddress");
        myWebView.loadUrl(address);
    }


}
