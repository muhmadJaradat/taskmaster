package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String TASK_TITLE ="taskTitle" ;
    public static final String TASK_BODY = "taskBody";
    public static final String TASK_STATUS = "taskState";
    public static final String TASK_FILE = "taskFile";
    List<Task> tasks;
    Handler handler;
  private static   TaskAdapter  adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        amplifyConf();
tasks=new ArrayList<>();

//        addingNewTeams();

        Amplify.Auth.fetchAuthSession(
                result -> Log.i("AmplifyQuickstart", "Succeed "+result.toString()),
                error -> Log.e("AmplifyQuickstart", error.toString())
        );


        findViewById(R.id.signUpMain).setOnClickListener(v ->{
            Intent signUp = new Intent(this, SignUp.class);
            startActivity(signUp);
        });

        findViewById(R.id.mainLoginButton).setOnClickListener(v ->{
            Intent LogIn = new Intent(this, LogIn.class);
            startActivity(LogIn);
        });

        getSupportActionBar().setTitle("Main Page");

        Button addTask = findViewById(R.id.add_task);

        addTask.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTask.class);
            startActivity(intent);
        });


        Button allTasks = findViewById(R.id.all_tasks);

        allTasks.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AllTasks.class);
            startActivity(intent);
        });

        findViewById(R.id.mainSignOut).setOnClickListener(v ->{
            Amplify.Auth.signOut(
                    () -> Log.i("AuthQuickstart", "Signed out successfully"),
                    error -> Log.e("AuthQuickstart", error.toString())
            );
        });


        TextView userViewName = findViewById(R.id.shared_user_name);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String name = sp.getString("name", "Username still not provided");
        userViewName.setText(name);
        String teamID=sp.getString("teamID","null");


        if (!teamID.equals("null"))gettingTasks(teamID);

        handler=new Handler();
        handler.postDelayed(() -> {
            RecyclerView recyclerView = findViewById(R.id.rvTasks);
            adapter = new TaskAdapter(tasks, new TaskAdapter.OnTaskItemClickListener() {
                @Override
                public void onItemClicked(int position) {
                    Intent goToDetailsIntent = new Intent(getApplicationContext(), TaskDetail.class);
                    goToDetailsIntent.putExtra(TASK_TITLE, tasks.get(position).getTitle());
                    goToDetailsIntent.putExtra(TASK_BODY, tasks.get(position).getBody());
                    goToDetailsIntent.putExtra(TASK_STATUS, tasks.get(position).getState());
                    goToDetailsIntent.putExtra(TASK_FILE, tasks.get(position).getAttachedFile());

                    startActivity(goToDetailsIntent);
                }


            });

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                    this,
                    LinearLayoutManager.VERTICAL,
                    false);            recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
                    },3000);




    }

    public void TaskDetailRedirect(View view) {
        Intent taskDetailIntent = new Intent(this, TaskDetail.class);
        Button button = (Button) view;
        String title = button.getText().toString();
        taskDetailIntent.putExtra("title", title);
        startActivity(taskDetailIntent);
    }


    public void goToSettingPage(View view) {
        Intent settingIntent = new Intent(this, SettingsPage.class);
        startActivity(settingIntent);
    }

    public void amplifyConf() {
        try {
            // Add these lines to add the AWSApiPlugin plugins
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.configure(getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
    }
public void addingNewTeams(){
    Team item = Team.builder()
            .name("Red Team")
            .build();
    Amplify.API.mutate(
            ModelMutation.create(item),
            success -> Log.i("Amplify", "Saved item: " + success.getData().getName()),
            error -> Log.e("Amplify", "Could not save item to DataStore", error)
    );

    Team item2 = Team.builder()
            .name("Blue Team")
            .build();
    Amplify.API.mutate(
            ModelMutation.create(item2),
            success -> Log.i("Amplify", "Saved item: " + success.getData().getName()),
            error -> Log.e("Amplify", "Could not save item to DataStore", error)
    );

    Team item3 = Team.builder()
            .name("Yellow Team")
            .build();
    Amplify.API.mutate(
            ModelMutation.create(item3),
            success -> Log.i("Amplify", "Saved item: " + success.getData().getName()),
            error -> Log.e("Amplify", "Could not save item to DataStore", error)
    );

}
public void gettingTasks(String teamID){
    Amplify.API.query(
            ModelQuery.list(Task.class,Task.TEAM.contains(teamID)),
            response->{
                for (Task task:response.getData()){
                    Log.i("MyAmplifyApp", task.getTeam().getName());
                    tasks.add(task);
                }
            },
            error -> Log.e("MyAmplifyApp", "Query failure", error)

    );
}

    @SuppressLint("NotifyDataSetChanged")
    private static void listItemDeleted() {
        adapter.notifyDataSetChanged();
    }
}