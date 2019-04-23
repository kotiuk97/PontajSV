package com.example.skotyuk.pontajsv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;


public class EventsFragment extends Activity {


    public static int EVENTS_FRAGMENT = 101;

    private String userName;
    private WebView webView;
    private DataBase dataBase;
    private String htmlReply;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_fragment);
        userName = getIntent().getStringExtra("USER_NAME");
        dataBase = new DataBase(this);
        webView = (WebView) findViewById(R.id.webView);

        try {
            htmlReply = dataBase.getEventsHTMLTable(userName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        webView.loadDataWithBaseURL(null, htmlReply, "text/html", "utf-8", null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        setResult(EVENTS_FRAGMENT);
    }

}
