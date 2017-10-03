package com.example.skotyuk.pontajsv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void initWebPage(){
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("http://www.example.com");
    }

    public void openSite(View view) {
        Intent myIntent = new Intent(this, WebPageWindow.class);
        EditText editText = (EditText) findViewById(R.id.webAddress);
        myIntent.putExtra("webAddress", editText.getText());
        startActivity(myIntent);
    }
}
