package com.example.skotyuk.pontajsv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
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

    public void insertCheck(View view) throws IOException {
         WebView webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        String url = createURLString(spinner.getSelectedItem().toString());
     //   webView.loadUrl(url);
        String name = spinner.getSelectedItem().toString();
        getNumberOfChecks(name);
        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
    }

    private String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        return dateFormat.format(date);
    }
    private String createURLString(String userName){
        String firstName, lastName;
        lastName = userName.split(" ")[0];
        firstName = userName.split(" ")[1];

        return "http://chreniuc:68S3PpmGaJfr6@pontaj.computervoice.ro:5544/files/evidenta_online.php?combo=" + lastName +"+" + firstName + "&checkday=on&data_combo=" + getCurrentDate() + "&insert_check=do+it!";
    }

    private void getNumberOfChecks(String userName) throws IOException {
        String firstName, lastName;
        lastName = userName.split(" ")[0];
        firstName = userName.split(" ")[1];

        URL url;
        url = new URL("http://chreniuc:68S3PpmGaJfr6@pontaj.computervoice.ro:5544/files/evidenta.php?combo=Kotyuk+Serghei&checkday=on&data_combo=2017-10-04");
     //   url = new URL("http://chreniuc:68S3PpmGaJfr6@pontaj.computervoice.ro:5544/files/evidenta.php?combo=" + lastName +"+" + firstName + "&checkday=on&data_combo=" + getCurrentDate());
      //  url = new URL("http://pontaj.computervoice.ro:5544/files/evidenta.php?combo=" + lastName +"+" + firstName + "&checkday=on&data_combo=" + getCurrentDate());
        String encoding = "Y2hyZW5pdWM6NjhTM1BwbUdhSmZyNg==";
        URLConnection uc = url.openConnection();
        uc.setRequestProperty("Authorization", String.format("Basic %s", encoding));
        BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));

        String inputLine;
        int numberOfCheckIns = -1;
        while ((inputLine = br.readLine()) != null) {
            if (inputLine.contains("<table id=\"table\"")){
                while (!(inputLine = br.readLine()).contains("</table>")){
                    if (inputLine.contains("</tr>")){
                        numberOfCheckIns++;
                    }
                }
                break;
            }
        }
        br.close();
        Toast.makeText(this, "Number-" + String.valueOf(numberOfCheckIns), Toast.LENGTH_SHORT).show();
    }

}
