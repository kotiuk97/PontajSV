package com.example.skotyuk.pontajsv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private String action;
    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            finishCheck();
            dataBase.setWebChromeClient(null);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            action = buttonCheck.getText().toString();
            buttonCheck.setText("Wait");
            buttonCheck.setEnabled(false);
        }

    }

    private class MyWebViewClientForConnection extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dataBase.setWebChromeClient(null);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

    }

    private Spinner spinner;
    private TextView connectionLabel;
    private Button buttonCheck;
    private Button buttonEvents;
    private DataBase dataBase;
    private Toolbar toolbar;
    private SharedPreferences sharedPref;

    @Override
    protected void onResume() {
        super.onResume();
//        setButtonText();
        initDefaultUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar();
        initSpinner();
        sharedPref = getBaseContext().getSharedPreferences(Keys.SETTINGS, Context.MODE_PRIVATE);
        initDefaultUser();
        initButtons();

        connectionLabel = (TextView) findViewById(R.id.connectionLabel);

        dataBase = new DataBase(this);
        if (!isOnline()){
            NoInternetDialogFragment dialog = new NoInternetDialogFragment();
            dialog.show(getFragmentManager(),"noInternetDialog");
        }else{
            buttonCheck.setEnabled(true);
            buttonEvents.setEnabled(true);
            connectionLabel.setVisibility(View.INVISIBLE);
        }

//        setButtonText();

    }

    private void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.settings:
                        Intent intent = new Intent(getApplicationContext(), SettingsFragment.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.menu);
    }

    private void initSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner_username);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.list_of_users, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    setButtonText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initButtons(){
        buttonCheck = (Button) findViewById(R.id.buttonInsertCheck);
        buttonEvents = (Button) findViewById(R.id.buttonEvents);
        buttonCheck.setEnabled(false);
        buttonEvents.setEnabled(false);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public void onClick(View view) throws IOException {
        switch (view.getId()){
            case R.id.buttonInsertCheck:
                insertCheck(spinner.getSelectedItem().toString());
                break;
            case R.id.buttonEvents:
                Intent intent = new Intent(this, EventsFragment.class);
                intent.putExtra("USER_NAME", spinner.getSelectedItem().toString());
//                buttonCheck.setEnabled(false);
                buttonEvents.setText("Processing");
//                buttonEvents.setEnabled(false);
                startActivityForResult(intent, EventsFragment.EVENTS_FRAGMENT);
                break;
        }
    }

    private void finishCheck(){
        Toast.makeText(MainActivity.this, "Done.. See Events In/Out", Toast.LENGTH_LONG).show();
        if (action.equals("Check In")){
            buttonCheck.setText("Check Out");
        }else{
            buttonCheck.setText("Check In");
        }
        buttonCheck.setEnabled(true);
    }


    private void insertCheck(String username) {
        try {
            dataBase.setWebChromeClient(new MyWebViewClient());
            dataBase.insertCheck(username);
        } catch (IOException e) {
            Toast.makeText(this, "Error..", Toast.LENGTH_LONG).show();
        }
    }


    private void setButtonText(){
        int numberOfChecks = 0;
        try {
            dataBase.setWebChromeClient(new MyWebViewClientForConnection());
            String user = spinner.getSelectedItem().toString();
            numberOfChecks = dataBase.getNumberOfChecks(user);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error getting number of checks.. try later", Toast.LENGTH_SHORT).show();
        }
        numberOfChecks += 2;
        String buttonText = "Error";
        if (numberOfChecks%2 == 0){
            buttonText = getResources().getString(R.string.CheckIn);
        }else{
            buttonText = getResources().getString(R.string.CheckOut);
        }
        buttonCheck.setText(buttonText);
    }

    private void initDefaultUser() {
        String username = sharedPref.getString(Keys.DEFAULT_USER_NAME, "");
        if (username.equals(""))
            return;

        for(int i= 0; i < spinner.getAdapter().getCount(); i++){
            if(spinner.getAdapter().getItem(i).toString().contains(username))
            {
                spinner.setSelection(i);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EventsFragment.EVENTS_FRAGMENT){
            buttonEvents.setText("See events");
//            buttonEvents.setEnabled(true);
//            buttonCheck.setEnabled(true);
        }
    }
}
