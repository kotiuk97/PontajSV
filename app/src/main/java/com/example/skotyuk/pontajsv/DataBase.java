package com.example.skotyuk.pontajsv;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

/**
 * Created by triven on 10/5/2017.
 */

public class DataBase {


    private Context context;
    private WebView webView;
    private final String DBAdminLoginPass = "chreniuc:68S3PpmGaJfr6";
    private final String DBAdminLoginPassEncoded = "Y2hyZW5pdWM6NjhTM1BwbUdhSmZyNg==";

    public DataBase(Context context) {
        this.context = context;
        initWebView();
    }

    private void initWebView(){
        webView = new WebView(context);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
    }

    private String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        return dateFormat.format(date);
    }

    private String getURLCheckRequest(String userName){
        String firstName, lastName;
        lastName = userName.split(" ")[0];
        firstName = userName.split(" ")[1];

        return "http://" + DBAdminLoginPass + "@pontaj.computervoice.ro:5544/files/evidenta_online.php?combo=" + lastName +"+" + firstName + "&checkday=on&data_combo=" + getCurrentDate() + "&insert_check=do+it!";
    }

    public void insertCheck(String userName) throws IOException {
        String url = getURLCheckRequest(userName);
        webView.loadUrl(url);
    }

    public int getNumberOfChecks(String userName) throws IOException, ExecutionException, InterruptedException {
        String firstName, lastName;
        lastName = userName.split(" ")[0];
        firstName = userName.split(" ")[1];

        URL url;
        url = new URL("http://" + DBAdminLoginPass + "@pontaj.computervoice.ro:5544/files/evidenta.php?combo=" + lastName + "+" + firstName + "&checkday=on&data_combo=" + getCurrentDate());
        //   url = new URL("http://chreniuc:68S3PpmGaJfr6@pontaj.computervoice.ro:5544/files/evidenta.php?combo=" + lastName +"+" + firstName + "&checkday=on&data_combo=" + getCurrentDate());
        //  url = new URL("http://pontaj.computervoice.ro:5544/files/evidenta.php?combo=" + lastName +"+" + firstName + "&checkday=on&data_combo=" + getCurrentDate());

        MySocket socket = new MySocket();
        socket.execute(new URL[]{url});
        return  socket.get();
    }

    class MySocket extends AsyncTask<URL, Void, Integer>{

        int result = -1;

        @Override
        protected Integer doInBackground(URL... urls) {
            try {
                URLConnection uc = urls[0].openConnection();
                uc.setRequestProperty("Authorization", String.format("Basic %s", DBAdminLoginPassEncoded));
                BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    if (inputLine.contains("<table id=\"table\"")){
                        while (!(inputLine = br.readLine()).contains("</table>")){
                            if (inputLine.contains("</tr>")){
                                result++;
                            }
                        }
                        break;
                    }
                }
                br.close();
            }catch (Exception e){
                result = -1;
            }
            return result;
        }

    }
}
