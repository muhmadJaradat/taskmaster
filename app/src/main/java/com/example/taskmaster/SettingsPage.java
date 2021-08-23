package com.example.taskmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;

public class SettingsPage extends AppCompatActivity {
    List<Team> teams;
    List<String> list;
    public Spinner spinner;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        teams = new ArrayList<>();
        list = new ArrayList<>();
list.add("");
        Amplify.API.query(
                ModelQuery.list(Team.class),
                response -> {

                    for (Team team : response.getData()) {
                        Log.i("MyAmplifyApp", team.getName());
                        teams.add(team);
                        list.add(team.getName());
                    }
                },
                error -> Log.e("MyAmplifyApp", "Query failure", error)
        );

        spinner = findViewById(R.id.spinner_set);


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor userValues = sp.edit();

        Button savePreferenceButton = findViewById(R.id.setting_button);
        savePreferenceButton.setOnClickListener(v -> {
            EditText userNameView = findViewById(R.id.settingsUserName);
            String userName= userNameView.getText().toString();
            Team team=teams.get(spinner.getSelectedItemPosition()-1);

            userValues.putString("name",userName);
            userValues.putString("teamID",team.getId());
            userValues.apply();

            Intent mainActivityIntent = new Intent(SettingsPage.this, MainActivity.class);
            startActivity(mainActivityIntent);
        });


    }
}