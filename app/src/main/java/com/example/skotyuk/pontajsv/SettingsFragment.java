package com.example.skotyuk.pontajsv;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


public class SettingsFragment extends Activity{

    private  Spinner spinner;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_fragment);
        sharedPref = getBaseContext().getSharedPreferences(Keys.SETTINGS, Context.MODE_PRIVATE);
        initSpinner();
        initDefaultUser();
    }

    private void initSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner_username_default);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.list_of_users, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    private void setDefaultUser(String userName) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Keys.DEFAULT_USER_NAME, userName);
        editor.commit();
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
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

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_save_settings:
                setDefaultUser(spinner.getSelectedItem().toString());
                finish();
                break;
        }
    }
}
