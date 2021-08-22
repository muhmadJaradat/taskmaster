package com.example.taskmaster;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.predicate.QueryPredicateOperation;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AddTask extends AppCompatActivity {
    List<Team> teams;
    List<String> list;
    public Spinner spinner;
    ArrayAdapter<String> adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        teams = new ArrayList<>();
        gettingTeams();
        list = new ArrayList<>();
        list.add("");


        setContentView(R.layout.activity_add_task);
        spinner = findViewById(R.id.spinner);


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AddTask.this, "fffff", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void addLabel(View view) {
        TextView titleText = findViewById(R.id.add_task_db);
        String title = titleText.getText().toString();
        TextView bodyText = findViewById(R.id.add_task_discrption);
        String body = bodyText.getText().toString();
        String status = "new";
        int teamName =  spinner.getSelectedItemPosition();
        Team team=teams.get(teamName-1);

        Task item = Task.builder()
                .team(team)
                .title(title)
                .body(body)
                .state(status)
                .build();
        Amplify.API.mutate(
                ModelMutation.create(item),
                success -> Log.i("Amplify", "Saved item: " + success.getData().getTitle()),
                error -> Log.e("Amplify", "Could not save item to DataStore", error)
        );

        TextView text = findViewById(R.id.submitted);
        text.setVisibility(View.VISIBLE);

        Intent detailsIntent = new Intent(this, MainActivity.class);
        startActivity(detailsIntent);
    }


    public void gettingTeams() {
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
    }
}