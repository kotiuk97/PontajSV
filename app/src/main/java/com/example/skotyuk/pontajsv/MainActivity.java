package com.example.skotyuk.pontajsv;

import android.content.Context;
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
        int tmp = 0;
        try {
            tmp = dataBase.getNumberOfChecks(spinner.getSelectedItem().toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setButtonText(tmp);
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
                try {
                    int tmp = dataBase.getNumberOfChecks(spinner.getSelectedItem().toString());
                    setButtonText(tmp);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
     //   dataBase.insertCheck(spinner.getSelectedItem().toString());
    }

    private void setButtonText(int numberOfChecks){
        Toast.makeText(this, String.valueOf(numberOfChecks), Toast.LENGTH_LONG).show();
        if (numberOfChecks == 0 || numberOfChecks%2 == 0){
            if (numberOfChecks == 2)
                buttonCheck.setText(R.string.CheckOut);
            else
            buttonCheck.setText(R.string.CheckIn);
        }else{
            buttonCheck.setText(R.string.CheckOut);
        }
    }

}
