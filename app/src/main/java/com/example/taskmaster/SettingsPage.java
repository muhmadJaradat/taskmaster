package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor userValues = sp.edit();

        Button savePreferenceButton = findViewById(R.id.setting_button);
        savePreferenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userNameView = findViewById(R.id.settingsUserName);
                String userName= userNameView.getText().toString();

                userValues.putString("name",userName);
                userValues.apply();

                Intent mainActivityIntent = new Intent(SettingsPage.this, MainActivity.class);
                startActivity(mainActivityIntent);
            }
        });


    }
}