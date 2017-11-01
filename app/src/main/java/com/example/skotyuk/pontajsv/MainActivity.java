package com.example.skotyuk.pontajsv;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        setButtonText();
    }

    private  Spinner spinner;
    private Button buttonCheck;
    private DataBase dataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!isOnline()){
            NoInternetDialogFragment dialog = new NoInternetDialogFragment();
            dialog.show(getFragmentManager(),"noInternetDialog");
        }
        initSpinner();
        buttonCheck = (Button) findViewById(R.id.buttonInsertCheck);
        dataBase = new DataBase(this);

        setButtonText();

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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public void onClick(View view) throws IOException {
        switch (view.getId()){
            case R.id.buttonInsertCheck:
                dataBase.insertCheck(spinner.getSelectedItem().toString());
                Toast.makeText(this, "Done.. See Events In/Out", Toast.LENGTH_LONG).show();
                switchButtonText();
                break;
            case R.id.buttonEvents:
                Intent intent = new Intent(this, EventsFragment.class);
                intent.putExtra("USER_NAME", spinner.getSelectedItem().toString());
                startActivity(intent);
                break;
        }
    }

    private void setButtonText(){
        int numberOfChecks = 0;
        try {
            numberOfChecks = dataBase.getNumberOfChecks(spinner.getSelectedItem().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (numberOfChecks == 0 || numberOfChecks%2 == 0){
            buttonCheck.setText(R.string.CheckIn);
  /*          if (numberOfChecks == 2)
                buttonCheck.setText(R.string.CheckIn);
            else
            buttonCheck.setText(R.string.CheckIn);
 */       }else{
            buttonCheck.setText(R.string.CheckOut);
        }
    }

    private void switchButtonText(){
        if (buttonCheck.getText().equals("Check Out"))
            buttonCheck.setText("Check In");
        else
            buttonCheck.setText("Check Out");
    }
}
