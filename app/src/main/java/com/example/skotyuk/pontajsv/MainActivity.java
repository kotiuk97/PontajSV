package com.example.skotyuk.pontajsv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    private  Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSpinner();
        System.out.println("That didn't work!");
    }

    private void initSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner_username);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.list_of_users, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void insertCheck(View view){
         WebView webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        String url = createURLString(spinner.getSelectedItem().toString());
        webView.loadUrl(url);
        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
    }

    private String createURLString(String userName){
        String firstName, lastName;
        lastName = userName.split(" ")[0];
        firstName = userName.split(" ")[1];
        return "http://chreniuc:68S3PpmGaJfr6@pontaj.computervoice.ro:5544/files/evidenta_online.php?combo=" + lastName +"+" + firstName + "&checkday=on&data_combo=2017-10-04&insert_check=do+it!";
    }
}
